import http, { type PageResult } from './http';

// 中文注释：门户前台接口定义，复用 OpenAPI 文档字段
export interface NewsItem {
  id: number;
  title: string;
  summary?: string;
  content?: string;
  coverUrl?: string;
  publishTime: string;
  author?: string;
  viewCount?: number;
}

export interface NoticeItem {
  id: number;
  title: string;
  content?: string;
  publishTime: string;
}

export interface ScenicItem {
  id: number;
  name: string;
  level?: string;
  ticketPrice?: number;
  address: string;
  openTime?: string;
  phone?: string;
  website?: string;
  description?: string;
  imageUrl?: string;
}

export interface VenueItem {
  id: number;
  name: string;
  category?: string;
  free?: boolean;
  openTime?: string;
  address: string;
  phone?: string;
  website?: string;
  description?: string;
  imageUrl?: string;
}

export interface ActivityItem {
  id: number;
  name: string;
  coverUrl?: string;
  startTime: string;
  endTime: string;
  category?: string;
  venueId: number;
  venueName?: string;
  organizer?: string;
  contactPhone?: string;
  address?: string;
  intro?: string;
  status?: string;
}

export interface AuthResult {
  token: string;
  refreshToken: string;
}

// 首页内容
export interface HomeBannerItem {
  id?: number;
  title?: string;
  imageUrl: string;
  linkUrl?: string;
  sort?: number;
}

export interface HomeIntroItem {
  title?: string;
  content?: string;
  coverUrl?: string;
  coverType?: 'IMAGE' | 'VIDEO';
}

export interface HomeScenicItem {
  id?: number;
  name?: string;
  imageUrl?: string;
  description?: string;
  sort?: number;
}

export interface HomeContent {
  banners: HomeBannerItem[];
  intro?: HomeIntroItem;
  scenics?: HomeScenicItem[];
  scenicLimit?: number;
}

export interface UserInfo {
  userId?: number;
  userName?: string;
  nickName?: string;
  phone?: string;
  avatar?: string;
  gender?: string;
  email?: string;
  remark?: string;
  roles?: string[];
}

export interface FavoriteItem {
  id: number;
  targetType: string;
  targetId: number;
  targetName: string;
  targetCover: string;
  createTime: string;
}

export interface FileUploadVO {
  url: string;
  fileName: string;
  originalName: string;
  size: number;
}

// 登录与用户信息
export const login = (payload: { userName: string; password: string }) =>
  http.post<AuthResult, AuthResult>('/auth/login', payload);
export const fetchUserInfo = () => http.get<UserInfo, UserInfo>('/auth/info');
export const updateUserInfo = (payload: Partial<UserInfo>) => http.put<void, void>('/auth/info', payload);
export const uploadFile = (file: File, bizTag?: string) => {
  const formData = new FormData();
  formData.append('file', file);
  return http.post<FileUploadVO, FileUploadVO>('/file/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    params: { bizTag }
  });
};

// 新闻、公告
export const fetchNewsPage = (params: { current?: number; size?: number; keyword?: string }) =>
  http.get<PageResult<NewsItem>, PageResult<NewsItem>>('/portal/news/page', { params });
export const fetchNewsDetail = (id: number) => http.get<NewsItem, NewsItem>(`/portal/news/${id}`);
export const fetchNoticePage = (params: { current?: number; size?: number; keyword?: string }) =>
  http.get<PageResult<NoticeItem>, PageResult<NoticeItem>>('/portal/notice/page', { params });
export const fetchNoticeDetail = (id: number) => http.get<NoticeItem, NoticeItem>(`/portal/notice/${id}`);

// 景区与场馆
export const fetchScenicPage = (params: { current?: number; size?: number; name?: string; address?: string }) =>
  http.get<PageResult<ScenicItem>, PageResult<ScenicItem>>('/portal/scenic/page', { params });
export const fetchScenicDetail = (id: number) => http.get<ScenicItem, ScenicItem>(`/portal/scenic/${id}`);
export const fetchVenuePage = (params: { current?: number; size?: number; name?: string; address?: string; category?: string }) =>
  http.get<PageResult<VenueItem>, PageResult<VenueItem>>('/portal/venue/page', { params });
export const fetchVenueDetail = (id: number) => http.get<VenueItem, VenueItem>(`/portal/venue/${id}`);

// 活动
export const fetchActivityPage = (params: { current?: number; size?: number; name?: string; venueId?: number }) =>
  http.get<PageResult<ActivityItem>, PageResult<ActivityItem>>('/portal/activity/page', { params });
export const fetchActivityDetail = (id: number) => http.get<ActivityItem, ActivityItem>(`/portal/activity/${id}`);
export const applyActivity = (payload: {
  name: string;
  coverUrl?: string;
  startTime: string;
  endTime: string;
  category?: string;
  venueId: number;
  organizer?: string;
  contactPhone?: string;
  intro?: string;
  auditRemark?: string;
}) => http.post<number, number>('/portal/activity/apply', payload);

// 首页内容聚合
export const fetchHomeContent = () => http.get<HomeContent, HomeContent>('/portal/home/content');

// 收藏
export const addFavorite = (type: string, id: number) => http.post<number, number>(`/portal/fav/${type}/${id}`);
export const removeFavorite = (type: string, id: number) => http.delete<string, string>(`/portal/fav/${type}/${id}`);
export const fetchFavoritePage = (params: { current?: number; size?: number; type?: string }) =>
  http.get<PageResult<FavoriteItem>, PageResult<FavoriteItem>>(
    '/portal/fav/page',
    { params }
  );
