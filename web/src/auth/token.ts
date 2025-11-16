const TOKEN_KEY = 'aftourism_admin_token';

// 读取 token
export function getToken() {
  return window.localStorage.getItem(TOKEN_KEY) || '';
}

// 持久化 token
export function setToken(token: string) {
  window.localStorage.setItem(TOKEN_KEY, token);
}

// 清理 token
export function clearToken() {
  window.localStorage.removeItem(TOKEN_KEY);
}
