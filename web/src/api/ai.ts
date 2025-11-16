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

export function confirmTool(conversationId: string, payload: { toolCallId: string; approved: boolean; comment?: string }) {
  return request.post<{ success: boolean; message: string; pendingTool?: AiPendingTool }>(
    `/ai/conversations/${conversationId}/confirm`,
    payload
  );
}

export function createConfirmSheetFromTool(tool: AiPendingTool, structured?: AiStructuredReply): ConfirmSheet {
  const keyword = `${tool.toolName || ''} ${tool.description || ''} ${tool.summary || ''}`.toLowerCase();
  const danger = /delete|remove|disable|offline|reject|danger/.test(keyword);
  return {
    action: tool.toolName,
    summary: tool.summary || structured?.reply || 'AI 建议执行操作',
    params: {
      描述: tool.description,
      工具: tool.toolName,
      状态: tool.status,
      生成时间: tool.createdAt
    },
    riskLevel: tool.riskLevel || (danger ? 'L3' : 'L2')
  };
}
