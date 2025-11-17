import request from './request';

export interface AiMessage {
  role: string;
  content: string;
  timestamp: string;
}

export interface AiActionSuggestion {
  type: string;
  detail: string;
}

export interface AiStructuredReply {
  reply?: string;
  actions?: AiActionSuggestion[];
  needsConfirmation?: boolean;
  pendingToolId?: string;
  securityNotice?: string;
}

export interface AiPendingTool {
  toolCallId: string;
  toolName: string;
  description?: string;
  summary?: string;
  status: string;
  createdAt: string;
  riskLevel?: 'L1' | 'L2' | 'L3';
}

export interface AiChatResponse {
  conversationId: string;
  closed: boolean;
  closeReason?: string;
  structured?: AiStructuredReply;
  history: AiMessage[];
  pendingTool?: AiPendingTool;
}

export interface ConfirmSheet {
  action: string;
  summary: string;
  params: Record<string, any>;
  riskLevel: 'L1' | 'L2' | 'L3';
}

export function chatApi(payload: { conversationId?: string; message: string; stream?: boolean }) {
  return request.post<AiChatResponse>('/ai/conversations/chat', payload);
}

export function fetchConversation(conversationId: string) {
  return request.get<AiChatResponse>(`/ai/conversations/${conversationId}`);
}

export function confirmTool(conversationId: string, payload: { toolCallId: string; approved?: boolean; comment?: string }) {
  return request.post<{ success: boolean; message: string; pendingTool?: AiPendingTool }>(
    `/ai/conversations/${conversationId}/confirm`,
    payload
  );
}

export function createConfirmSheetFromTool(tool: AiPendingTool, structured?: AiStructuredReply): ConfirmSheet {
  const keyword = `${tool.toolName || ''} ${tool.description || ''} ${tool.summary || ''}`.toLowerCase();
  const danger = /delete|remove|disable|offline|reject|danger/.test(keyword);
  const templates: Record<string, (t: AiPendingTool) => ConfirmSheet> = {
    AdminActivityApprovalTool: (t) => ({
      action: '活动审核通过',
      summary: t.summary || '将把选定活动标记为“已通过审核”并允许上线',
      params: { 工具: t.toolName, 描述: t.description, 创建时间: t.createdAt },
      riskLevel: 'L2'
    }),
    UserFeedbackTool: (t) => ({
      action: '记录用户反馈',
      summary: t.summary || '将把当前反馈内容持久化到后台反馈系统',
      params: { 工具: t.toolName, 描述: t.description, 创建时间: t.createdAt },
      riskLevel: 'L1'
    })
  };
  const factory = templates[tool.toolName];
  if (factory) return factory(tool);
  return {
    action: tool.toolName,
    summary: tool.summary || structured?.reply || 'AI 建议执行操作',
    params: { 描述: tool.description, 工具: tool.toolName, 状态: tool.status, 生成时间: tool.createdAt },
    riskLevel: tool.riskLevel || (danger ? 'L3' : 'L2')
  };
}

// AI 管理接口
export interface AiSettingsDTO {
  enabled: boolean;
  providers: { name: string; apiKey: string; timeoutMs?: number; endpoint?: string; enabled?: boolean }[];
  models: { name: string; provider: string; enabled?: boolean }[];
  defaultModel?: string;
  backupModel?: string;
}

export function getAiSettings() {
  return request.get<AiSettingsDTO>('/admin/ai/settings');
}

export function updateAiSettings(payload: AiSettingsDTO) {
  return request.put<void>('/admin/ai/settings', payload);
}

export function testAiProvider(payload: { name: string; apiKey: string; timeoutMs?: number; endpoint?: string }) {
  return request.post<{ success: boolean; message: string }>('/admin/ai/providers/test', payload);
}

export function getAiStats() {
  return request.get<{ models: Record<string, number>; alerts: Record<string, number> }>('/admin/ai/stats');
}
