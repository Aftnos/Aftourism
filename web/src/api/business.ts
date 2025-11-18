import request from './request';
import type { PageInfo } from './admin';

export interface NewsItem {
  id?: number;
  title: string;
  content: string;
  coverUrl: string;
  author?: string;
  status: number;
  statusText?: string;
  publishTime?: string;
  viewCount?: number;
  createTime?: string;
  updateTime?: string;
}

export interface NoticeItem {
  id?: number;
  title: string;
  content: string;
  author?: string;
  status: number;
  publishTime?: string;
  statusText?: string;
  viewCount?: number;
  createTime?: string;
  updateTime?: string;
}

export interface ScenicItem {
  id?: number;
  name: string;
  imageUrl?: string;
  level?: string;
  ticketPrice?: number;
  address?: string;
  openTime?: string;
  intro?: string;
  phone?: string;
  website?: string;
  longitude?: number;
  latitude?: number;
  sort?: number;
  createTime?: string;
  updateTime?: string;
}

export interface VenueItem {
  id?: number;
  name: string;
  imageUrl?: string;
  category?: string;
  isFree: number;
  ticketPrice?: number;
  address?: string;
  openTime?: string;
  description?: string;
  phone?: string;
  website?: string;
  longitude?: number;
  latitude?: number;
  sort?: number;
  createTime?: string;
  updateTime?: string;
}

export type RecycleType = 'NEWS' | 'NOTICE' | 'SCENIC' | 'VENUE' | 'ACTIVITY';

export interface RecycleItem {
  id: number;
  type: RecycleType;
  title: string;
  deletedTime: string;
  operator?: string;
  extraInfo?: string;
}

export interface ActivitySummary {
  id: number;
  name: string;
  coverUrl?: string;
  startTime?: string;
  endTime?: string;
  category?: string;
  venueId?: number;
  venueName?: string;
  addressCache?: string;
  onlineStatus?: number;
  applyStatus?: number;
}

export interface ActivityAuditDetail extends ActivitySummary {
  organizer?: string;
  contactPhone?: string;
  intro?: string;
  applyUserId?: number;
  rejectReason?: string;
  submitTime?: string;
  auditRemark?: string;
}

export interface ActivityManagePayload {
  name: string;
  coverUrl?: string;
  startTime: string;
  endTime: string;
  category?: string;
  venueId: number;
  organizer?: string;
  contactPhone?: string;
  intro?: string;
  addressCache?: string;
  onlineStatus?: number;
}

export interface ActivityManageItem extends ActivitySummary {
  viewCount?: number;
  favoriteCount?: number;
  createTime?: string;
  updateTime?: string;
}

export interface ActivityManageDetail extends ActivityManageItem {
  organizer?: string;
  contactPhone?: string;
  intro?: string;
}

export interface ActivityCommentItem {
  id: number;
  activityId: number;
  userId?: number;
  userNickname?: string;
  userAvatar?: string;
  content: string;
  parentId?: number;
  childCount?: number;
  likeCount?: number;
  createTime?: string;
}

export function fetchNews(params: Record<string, any>) {
  return request.get<PageInfo<NewsItem>>('/admin/news/page', { params });
}

export function createNews(payload: NewsItem) {
  return request.post<number>('/admin/news', payload);
}

export function updateNews(id: number, payload: NewsItem) {
  return request.put<void>(`/admin/news/${id}`, payload);
}

export function deleteNews(id: number) {
  return request.del<void>(`/admin/news/${id}`);
}

export function fetchNotices(params: Record<string, any>) {
  return request.get<PageInfo<NoticeItem>>('/admin/notice/page', { params });
}

export function createNotice(payload: NoticeItem) {
  return request.post<number>('/admin/notice', payload);
}

export function updateNotice(id: number, payload: NoticeItem) {
  return request.put<void>(`/admin/notice/${id}`, payload);
}

export function deleteNotice(id: number) {
  return request.del<void>(`/admin/notice/${id}`);
}

export function fetchScenicList(params: Record<string, any>) {
  return request.get<PageInfo<ScenicItem>>('/admin/scenic/page', { params });
}

export function createScenic(payload: ScenicItem) {
  return request.post<number>('/admin/scenic', payload);
}

export function updateScenic(id: number, payload: ScenicItem) {
  return request.put<void>(`/admin/scenic/${id}`, payload);
}

export function deleteScenic(id: number) {
  return request.del<void>(`/admin/scenic/${id}`);
}

export function fetchVenues(params: Record<string, any>) {
  return request.get<PageInfo<VenueItem>>('/admin/venue/page', { params });
}

export function createVenue(payload: VenueItem) {
  return request.post<number>('/admin/venue', payload);
}

export function updateVenue(id: number, payload: VenueItem) {
  return request.put<void>(`/admin/venue/${id}`, payload);
}

export function deleteVenue(id: number) {
  return request.del<void>(`/admin/venue/${id}`);
}

export function fetchRecycleItems(params: Record<string, any>) {
  return request.get<PageInfo<RecycleItem>>('/admin/recycle/page', { params });
}

export function restoreRecycle(type: string, id: number) {
  return request.put<void>(`/admin/recycle/${type}/${id}/restore`);
}

export function removeRecycle(type: string, id: number) {
  return request.del<void>(`/admin/recycle/${type}/${id}`);
}

export function fetchPortalActivities(params: Record<string, any>) {
  return request.get<PageInfo<ActivitySummary>>('/portal/activity/page', { params });
}

export function fetchAuditActivities(params: Record<string, any>) {
  return request.get<PageInfo<ActivitySummary>>('/admin/activity/audit/page', { params });
}

export function fetchAuditActivityDetail(id: number) {
  return request.get<ActivityAuditDetail>(`/admin/activity/audit/${id}`);
}

export function approveActivity(id: number) {
  return request.put<void>(`/admin/activity/${id}/approve`);
}

export function rejectActivity(id: number, rejectReason: string) {
  return request.put<void>(`/admin/activity/${id}/reject`, { rejectReason });
}

export function updateActivityAuditRemark(id: number, auditRemark: string) {
  return request.put<void>(`/admin/activity/${id}/remark`, { auditRemark });
}

export function fetchManagedActivities(params: Record<string, any>) {
  return request.get<PageInfo<ActivityManageItem>>('/admin/activity/manage/page', { params });
}

export function fetchActivityManageDetail(id: number) {
  return request.get<ActivityManageDetail>(`/admin/activity/manage/${id}`);
}

export function createActivityManage(payload: ActivityManagePayload) {
  return request.post<number>('/admin/activity/manage', payload);
}

export function updateActivityManage(id: number, payload: ActivityManagePayload) {
  return request.put<void>(`/admin/activity/manage/${id}`, payload);
}

export function deleteActivityManage(id: number) {
  return request.del<void>(`/admin/activity/manage/${id}`);
}

export function fetchActivityComments(activityId: number, params: Record<string, any>) {
  return request.get<PageInfo<ActivityCommentItem>>(`/admin/activity/manage/${activityId}/comment/page`, { params });
}

export function deleteActivityComment(commentId: number) {
  return request.del<void>(`/admin/activity/manage/comment/${commentId}`);
}

export interface SystemMetricItem {
  host: string;
  cpuUsage: number;
  memoryUsage: number;
  diskUsage: number;
  loadAvg?: string;
  createTime?: string;
}

export function fetchSystemMetrics(params: Record<string, any>) {
  return request.get<PageInfo<SystemMetricItem>>('/admin/monitor/metrics/page', { params });
}
