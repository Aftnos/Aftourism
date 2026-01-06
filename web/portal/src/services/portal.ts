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
  amapId?: string;
  tags?: string;
  level?: string;
  ticketPrice?: number;
  address: string;
  province?: string;
  city?: string;
  district?: string;
  openTime?: string;
  phone?: string;
  website?: string;
  intro?: string;
  imageUrl?: string;
  imageUrls?: string;
}

export interface VenueItem {
  id: number;
  name: string;
  amapId?: string;
  tags?: string;
  category?: string;
  isFree?: number;
  ticketPrice?: number;
  openTime?: string;
  address: string;
  province?: string;
  city?: string;
  district?: string;
  phone?: string;
  website?: string;
  description?: string;
  imageUrl?: string;
  imageUrls?: string;
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
  // 中文注释：兼容新接口字段，用于前端上线状态过滤与展示缓存地址
  addressCache?: string;
  onlineStatus?: number;
  viewCount?: number;
  favoriteCount?: number;
}

export interface ActivityComment {
  id: number;
  activityId: number;
  userId: number;
  userNickname?: string;
  userAvatar?: string;
  content: string;
  parentId?: number;
  childCount?: number;
  likeCount?: number;
  createTime: string;
  children?: ActivityComment[];
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
  advancedUser?: boolean;
  qualificationStatus?: string;
  qualificationRemark?: string;
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

export interface QualificationStatus {
  status: string;
  auditRemark?: string;
  applyTime?: string;
}

// 登录与用户信息
export const login = (payload: { userName: string; password: string }) =>
  http.post<AuthResult, AuthResult>('/portal/auth/login', payload);
export const register = (payload: {
  username: string;
  password: string;
  nickname?: string;
  phone?: string;
  email?: string;
  avatar?: string;
  remark?: string;
}) => http.post<void, void>('/portal/auth/register', payload);
export const fetchUserInfo = () => http.get<UserInfo, UserInfo>('/portal/auth/info');
export const updateUserInfo = (payload: Partial<UserInfo>) => http.put<void, void>('/portal/auth/info', payload);
export const applyQualification = (payload: {
  realName: string;
  organization: string;
  contactPhone: string;
  applyReason: string;
  attachmentUrl?: string;
}) => http.post<void, void>('/portal/auth/qualification/apply', payload);
export const fetchQualificationStatus = () =>
  http.get<QualificationStatus, QualificationStatus>('/portal/auth/qualification/status');
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
export const fetchScenicPage = (params: {
  current?: number;
  size?: number;
  name?: string;
  address?: string;
  tags?: string;
  city?: string;
  district?: string;
}) =>
  http.get<PageResult<ScenicItem>, PageResult<ScenicItem>>('/portal/scenic/page', { params });
export const fetchScenicDetail = (id: number) => http.get<ScenicItem, ScenicItem>(`/portal/scenic/${id}`);
export const fetchVenuePage = (params: {
  current?: number;
  size?: number;
  name?: string;
  address?: string;
  category?: string;
  tags?: string;
  city?: string;
  district?: string;
}) =>
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

// 活动留言
export const fetchActivityComments = (
  activityId: number,
  params: { current?: number; size?: number; parentId?: number }
) => http.get<PageResult<ActivityComment>, PageResult<ActivityComment>>(`/portal/activity/${activityId}/comment/page`, { params });
export const postActivityComment = (
  activityId: number,
  payload: { content: string; parentId?: number }
) => http.post<number, number>(`/portal/activity/${activityId}/comment`, payload);
export const likeActivityComment = (commentId: number) =>
  http.post<string, string>(`/portal/activity/comment/${commentId}/like`);
export const deleteActivityComment = (commentId: number) =>
  http.delete<string, string>(`/portal/activity/comment/${commentId}`);

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
