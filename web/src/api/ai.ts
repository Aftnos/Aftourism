import request from './request';

export interface ChatPayload {
  message: string;
  ragEnabled: boolean;
  mode: 'DIRECT' | 'PLAN';
}

export interface ChatResult {
  answer: string;
  citations?: Array<{ title: string; url: string }>;
}

export function chatApi(data: ChatPayload) {
  return request.post<ChatResult>('/ai/chat', data);
}

export interface ConfirmSheet {
  action: string;
  summary: string;
  params: Record<string, any>;
  riskLevel: 'L0' | 'L1' | 'L2' | 'L3';
}

export function applyPlan(data: { content: string; mode: 'PLAN' }) {
  return request.post<{ confirmSheet: ConfirmSheet }>('/ai/apply', data);
}

export function applyExecute(data: { content: string; ticketNo?: string }) {
  return request.post<{ ticketNo: string; status: string }>('/ai/apply/execute', data);
}
