// 中文注释：模拟后端数据，便于前台快速联调
export interface NewsItem {
  id: number;
  title: string;
  content: string;
  publishTime: string;
  cover?: string;
}

export interface NoticeItem {
  id: number;
  title: string;
  content: string;
  publishTime: string;
}

export interface ScenicSpot {
  id: number;
  name: string;
  level: string;
  ticket: string;
  openTime: string;
  address: string;
  phone: string;
  website: string;
  description: string;
  cover: string;
}

export interface Venue {
  id: number;
  name: string;
  category: string;
  free: boolean;
  openTime: string;
  address: string;
  phone: string;
  website: string;
  description: string;
  cover: string;
}

export interface ActivityItem {
  id: number;
  name: string;
  startTime: string;
  endTime: string;
  category: string;
  venueId: number;
  venueName: string;
  organizer: string;
  phone: string;
  address: string;
  summary: string;
  cover: string;
  status: 'approved' | 'pending';
}

export const newsList: NewsItem[] = [
  {
    id: 1,
    title: '5A 级景区智慧化改造完成',
    content: '引入智能导览、无人售检票等新能力，全面提升游客体验。',
    publishTime: '2024-12-20',
    cover: 'https://images.unsplash.com/photo-1505764706515-aa95265c5abc?auto=format&fit=crop&w=800&q=60'
  },
  {
    id: 2,
    title: '冬季文旅消费券开领',
    content: '市文旅局联合多家景区发放惠民消费券，拉动冬季旅游。',
    publishTime: '2024-12-15'
  },
  {
    id: 3,
    title: '文化馆推出夜场开放试点',
    content: '文化馆试点夜间开放，为市民提供更多公共文化服务时段。',
    publishTime: '2024-12-10'
  },
  {
    id: 4,
    title: '体育馆新增亲子运动区',
    content: '亲子运动区上线，提升亲子互动体验。',
    publishTime: '2024-12-05'
  },
  {
    id: 5,
    title: '非遗主题活动走进校园',
    content: '非遗传承人走进校园开展体验课，弘扬传统文化。',
    publishTime: '2024-12-02'
  }
];

export const noticeList: NoticeItem[] = [
  {
    id: 101,
    title: '元旦期间景区限流公告',
    content: '请提前预约入园，遵循现场导引错峰游览。',
    publishTime: '2024-12-18'
  },
  {
    id: 102,
    title: '博物馆临时闭馆通知',
    content: '由于设备维护，12 月 25 日暂不开放。',
    publishTime: '2024-12-14'
  },
  {
    id: 103,
    title: '寒潮天气安全提示',
    content: '请注意保暖，冰雪景区游览遵循安全指引。',
    publishTime: '2024-12-08'
  }
];

export const scenicSpots: ScenicSpot[] = [
  {
    id: 201,
    name: '云岭国家森林公园',
    level: '5A',
    ticket: '120 元',
    openTime: '08:00-18:00',
    address: '云岭市云谷路 88 号',
    phone: '0898-1234567',
    website: 'https://scenic.example.com',
    description: '集峡谷、瀑布、森林于一体的生态景区，设有科普与研学线路。',
    cover: 'https://images.unsplash.com/photo-1526772662000-3f88f10405ff?auto=format&fit=crop&w=800&q=60'
  },
  {
    id: 202,
    name: '海潮古城',
    level: '4A',
    ticket: '80 元',
    openTime: '09:00-20:00',
    address: '海潮区古城街 66 号',
    phone: '0898-2233445',
    website: 'https://oldtown.example.com',
    description: '保存完好的古城街区，展示地方民俗与非遗手作。',
    cover: 'https://images.unsplash.com/photo-1541417904950-b855846fe074?auto=format&fit=crop&w=800&q=60'
  },
  {
    id: 203,
    name: '星河天文主题公园',
    level: '5A',
    ticket: '150 元',
    openTime: '10:00-22:00',
    address: '星河镇观星大道 9 号',
    phone: '0898-5566778',
    website: 'https://astropark.example.com',
    description: '融合科普展览、露营与夜观星空的复合型主题公园。',
    cover: 'https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=800&q=60'
  }
];

export const venues: Venue[] = [
  {
    id: 301,
    name: '城市博物馆',
    category: '博物馆',
    free: true,
    openTime: '09:00-17:00（周一闭馆）',
    address: '中心大道 1 号',
    phone: '0898-11112222',
    website: 'https://museum.example.com',
    description: '展示城市历史、民俗与科技发展，设有青少年研学课程。',
    cover: 'https://images.unsplash.com/photo-1505663912202-ac22d3f1b4a4?auto=format&fit=crop&w=800&q=60'
  },
  {
    id: 302,
    name: '未来文化馆',
    category: '文化馆',
    free: true,
    openTime: '10:00-18:00',
    address: '未来路 33 号',
    phone: '0898-33334444',
    website: 'https://culture.example.com',
    description: '提供展览、培训与演出的综合文化公共空间。',
    cover: 'https://images.unsplash.com/photo-1529429617124-aee1f1650a5b?auto=format&fit=crop&w=800&q=60'
  },
  {
    id: 303,
    name: '极动体育中心',
    category: '体育馆',
    free: false,
    openTime: '07:00-22:00',
    address: '体育大道 68 号',
    phone: '0898-55556666',
    website: 'https://sport.example.com',
    description: '包含室内游泳、羽毛球、篮球与综合训练场地。',
    cover: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=800&q=60'
  }
];

export const activities: ActivityItem[] = [
  {
    id: 401,
    name: '云岭森林乐跑',
    startTime: '2025-01-05 09:00',
    endTime: '2025-01-05 12:00',
    category: '体育',
    venueId: 303,
    venueName: '极动体育中心',
    organizer: '云岭文旅集团',
    phone: '0898-88880001',
    address: '体育大道 68 号',
    summary: '穿越森林跑道，体验自然与运动结合的乐趣。',
    cover: 'https://images.unsplash.com/photo-1508602632584-3e4eaf6ab1f2?auto=format&fit=crop&w=800&q=60',
    status: 'approved'
  },
  {
    id: 402,
    name: '非遗手作体验周',
    startTime: '2025-01-12 10:00',
    endTime: '2025-01-19 18:00',
    category: '文化',
    venueId: 302,
    venueName: '未来文化馆',
    organizer: '市文化馆',
    phone: '0898-88880002',
    address: '未来路 33 号',
    summary: '邀请非遗传承人现场教学，支持家庭亲子体验。',
    cover: 'https://images.unsplash.com/photo-1489515217757-5fd1be406fef?auto=format&fit=crop&w=800&q=60',
    status: 'approved'
  },
  {
    id: 403,
    name: '星河天文研学营',
    startTime: '2025-02-01 14:00',
    endTime: '2025-02-03 14:00',
    category: '研学',
    venueId: 203,
    venueName: '星河天文主题公园',
    organizer: '天文协会',
    phone: '0898-88880003',
    address: '星河镇观星大道 9 号',
    summary: '夜观星空、天文讲座与望远镜实操结合的研学体验。',
    cover: 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=800&q=60',
    status: 'pending'
  }
];
