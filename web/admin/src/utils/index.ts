// 简易的防抖实现
export function debounce<T extends (...args: any[]) => void>(fn: T, wait = 300) {
  let timer: number | undefined;
  return (...args: Parameters<T>) => {
    if (timer) window.clearTimeout(timer);
    timer = window.setTimeout(() => fn(...args), wait);
  };
}

// 简易的节流实现
export function throttle<T extends (...args: any[]) => void>(fn: T, wait = 300) {
  let timer: number | undefined;
  return (...args: Parameters<T>) => {
    if (timer) return;
    timer = window.setTimeout(() => {
      timer = undefined;
      fn(...args);
    }, wait);
  };
}

// 下载工具，用于导出类操作
export function downloadByUrl(url: string, filename: string) {
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  a.click();
}
