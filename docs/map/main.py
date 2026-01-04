import csv
import re
import argparse
from pathlib import Path
import pymysql

# ------------------ MySQL 配置 ------------------
MYSQL_CONFIG = {
    "host": "localhost",
    "user": "root",
    "password": "123456",
    "database": "aftourism_server",
    "charset": "utf8mb4",
}
# -----------------------------------------------


def norm(v: str):
    """统一空值：'' / '[]' / 'null' / 'None' -> None，其余 strip 后返回"""
    if v is None:
        return None
    v = str(v).strip()
    if v == "" or v == "[]" or v.lower() in ("null", "none"):
        return None
    return v


def to_float(v: str):
    v = norm(v)
    if v is None:
        return None
    try:
        return float(v)
    except ValueError:
        return None


def clean_phone(phone: str):
    """清洗电话：取第一个；去除非数字；保留手机号/座机（7~12位数字）"""
    phone = norm(phone)
    if phone is None:
        return None

    phone = phone.split("/")[0].split(";")[0].strip()
    phone = re.sub(r"[^\d]", "", phone)

    if re.fullmatch(r"1\d{10}", phone):   # 手机
        return phone
    if re.fullmatch(r"\d{7,12}", phone):  # 座机/服务热线等
        return phone
    return None


def pick_image_urls_field(row: dict):
    """兼容列名：图片链接(分号分隔) / 图片链接 等"""
    for k in row.keys():
        if "图片链接" in k:
            return norm(row.get(k))
    return None


def cover_from_image_urls(image_urls: str):
    """封面图取第一张有效 URL"""
    image_urls = norm(image_urls)
    if image_urls is None:
        return None
    parts = [p.strip() for p in image_urls.split(";") if p.strip()]
    return parts[0] if parts else None


def derive_category_from_tags(tags: str):
    """场馆 category：从 tags 推断，取最后一个分号段（如 博物馆）"""
    tags = norm(tags)
    if tags is None:
        return None
    parts = [p.strip() for p in tags.split(";") if p.strip()]
    return parts[-1] if parts else None


def detect_table_type(headers):
    """自动判断 scenic / venue：有“等级”列则 scenic，否则 venue"""
    return "scenic" if headers and ("等级" in headers) else "venue"


def open_csv(path: Path):
    """优先 utf-8-sig，失败则回退 gb18030"""
    try:
        f = open(path, newline="", encoding="utf-8-sig")
        f.readline()
        f.seek(0)
        return f
    except UnicodeDecodeError:
        return open(path, newline="", encoding="gb18030")


def scenic_sql():
    return """
    INSERT INTO t_scenic_spot
    (name, amap_id, level, tags, address, longitude, latitude,
     phone, province, city, district, website, open_time,
     image_url, image_urls, intro)
    VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)
    """


def venue_sql():
    return """
    INSERT INTO t_venue
    (name, amap_id, category, tags, address, longitude, latitude,
     phone, province, city, district, website, open_time,
     image_url, image_urls, description)
    VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)
    """


def import_one_csv(csv_path: Path, table_type: str = "auto", batch: int = 500):
    if not csv_path.exists():
        print(f"未找到文件：{csv_path}")
        return

    with open_csv(csv_path) as f:
        reader = csv.DictReader(f)
        headers = reader.fieldnames or []
        real_table_type = detect_table_type(headers) if table_type == "auto" else table_type

        sql = scenic_sql() if real_table_type == "scenic" else venue_sql()

        conn = pymysql.connect(**MYSQL_CONFIG)
        cursor = conn.cursor()

        buffer = []
        imported = 0
        row_idx = 1  # 含表头计数仅用于报错提示

        try:
            for row in reader:
                row_idx += 1

                name = norm(row.get("名称"))
                if not name:
                    continue

                amap_id = norm(row.get("高德ID"))
                tags = norm(row.get("类型标签"))
                address = norm(row.get("详细地址"))
                longitude = to_float(row.get("经度"))
                latitude = to_float(row.get("纬度"))
                phone = clean_phone(row.get("电话号码"))
                province = norm(row.get("省份"))
                city = norm(row.get("城市"))
                district = norm(row.get("区县"))
                website = norm(row.get("官方网站"))
                open_time = norm(row.get("营业时间"))

                image_urls = pick_image_urls_field(row)
                image_url = cover_from_image_urls(image_urls)

                intro_or_desc = norm(row.get("介绍"))

                if real_table_type == "scenic":
                    level = norm(row.get("等级"))
                    params = (
                        name, amap_id, level, tags, address, longitude, latitude,
                        phone, province, city, district, website, open_time,
                        image_url, image_urls, intro_or_desc
                    )
                else:
                    category = derive_category_from_tags(tags)
                    params = (
                        name, amap_id, category, tags, address, longitude, latitude,
                        phone, province, city, district, website, open_time,
                        image_url, image_urls, intro_or_desc
                    )

                buffer.append(params)

                if len(buffer) >= batch:
                    cursor.executemany(sql, buffer)
                    conn.commit()
                    imported += len(buffer)
                    buffer.clear()

            if buffer:
                cursor.executemany(sql, buffer)
                conn.commit()
                imported += len(buffer)

            print(f"导入完成：{csv_path.name} -> {real_table_type}，共 {imported} 条")

        except Exception as e:
            conn.rollback()
            print(f"导入失败：{csv_path.name} 第 {row_idx} 行附近可能有问题。错误：{e}")
            raise
        finally:
            cursor.close()
            conn.close()


def main():
    # 这两份 CSV 与 main.py 在同一目录下
    base_dir = Path(__file__).resolve().parent
    default_scenic = base_dir / "西藏A级景区.csv"
    default_venue = base_dir / "西藏场馆.csv"

    parser = argparse.ArgumentParser(description="CSV 导入 MySQL（景区/场馆）")
    # 现在 --csv 不再必填：不传就默认导入同目录下两份
    parser.add_argument("--csv", default=None, help="CSV 文件路径（不传则导入默认两份）")
    parser.add_argument("--table", choices=["auto", "scenic", "venue"], default="auto",
                        help="表类型：auto/scenic/venue（默认 auto）")
    parser.add_argument("--batch", type=int, default=500, help="批量提交大小（默认 500）")
    args = parser.parse_args()

    if args.csv:
        import_one_csv(Path(args.csv), table_type=args.table, batch=args.batch)
    else:
        # 默认：一次导入两份
        import_one_csv(default_scenic, table_type="scenic", batch=args.batch)
        import_one_csv(default_venue, table_type="venue", batch=args.batch)


if __name__ == "__main__":
    main()
