import request from './request';
import { getToken } from '@/auth/token';

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
  params?: Record<string, any>;
  operationGuide?: OperationGuide;
}

export interface OperationGuide {
  title?: string;
  description?: string;
  operationType?: string;
  riskLevel?: 'L0' | 'L1' | 'L2' | 'L3';
  parameters?: Record<string, any>;
}

export interface AiChatResponse {
  conversationId: string;
  closed: boolean;
  closeReason?: string;
  structured?: AiStructuredReply;
  history: AiMessage[];
  pendingTool?: AiPendingTool;
  modelName?: string;
  providerName?: string;
  streaming?: boolean;
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

export type ChatStreamEvent = { type: 'start' | 'chunk' | 'end' | 'error'; data: any };

export function startChatStream(payload: { conversationId?: string; message: string }, onMessage: (event: ChatStreamEvent) => void) {
  const controller = new AbortController();
  const base = import.meta.env.VITE_API_BASE || '';
  const token = getToken();
  fetch(`${base}/ai/conversations/chat/stream`, {
    method: 'POST',
    signal: controller.signal,
    headers: {
      'Content-Type': 'application/json',
      Authorization: token ? `Bearer ${token}` : ''
    },
    body: JSON.stringify(payload)
  }).then(async (resp) => {
    const reader = resp.body?.getReader();
    if (!reader) return;
    const decoder = new TextDecoder();
    let buffer = '';
    while (true) {
      const { value, done } = await reader.read();
      if (done) break;
      buffer += decoder.decode(value, { stream: true });
      let index = buffer.indexOf('\n');
      while (index >= 0) {
        const line = buffer.slice(0, index).trim();
        buffer = buffer.slice(index + 1);
        if (line) {
          try {
            onMessage(JSON.parse(line));
          } catch (err) {
            console.warn('无法解析流式消息', err, line);
          }
        }
        index = buffer.indexOf('\n');
      }
    }
  });
  return () => controller.abort();
}

export function fetchConversation(conversationId: string) {
  return request.get<AiChatResponse>(`/ai/conversations/${conversationId}`);
}

export function confirmTool(
  conversationId: string,
  payload: { toolCallId: string; approved?: boolean; comment?: string }
) {
  return request.post<{ success: boolean; message: string; pendingTool?: AiPendingTool }>(
    `/ai/conversations/${conversationId}/confirm`,
    payload
  );
}

export interface AiSettingsDTO {
  enabled: boolean;
  providers: { name: string; apiKey: string; timeoutMs?: number; endpoint?: string; enabled?: boolean; code?: string }[];
  models: { name: string; provider: string; enabled?: boolean; type?: string; description?: string }[];
  defaultChatModel?: string;
  fallbackChatModel?: string;
  defaultRerankModel?: string;
  defaultEmbeddingModel?: string;
  scenarioFallbacks?: Record<string, string>;
  alertRules?: { code: string; metric: string; comparator?: string; threshold?: number; targetModel?: string; message?: string }[];
}

export interface AiServiceStatusDTO {
  aiEnabled: boolean;
  globalSwitch: boolean;
  activeChatModel?: string;
  fallbackChatModel?: string;
  providers: { provider: string; endpoint?: string; status?: string; latencyMs?: number; checkedAt?: string }[];
  recentErrors: { provider?: string; model?: string; message?: string; occurredAt?: string }[];
}

export interface AiModelStatDTO {
  model: string;
  provider?: string;
  total: number;
  success: number;
  failure: number;
  avgLatencyMs: number;
  errorRate: number;
}

export interface AiAlertEventDTO {
  code: string;
  model: string;
  provider?: string;
  metric: string;
  value: number;
  threshold: number;
  comparator: string;
  message?: string;
  occurredAt: string;
}

export interface AiAuthorizationLogDTO {
  id: string;
  userId: string;
  role: string;
  conversationId: string;
  toolCallId: string;
  toolName: string;
  operation?: string;
  approved: boolean;
  comment?: string;
  occurredAt: string;
  params?: Record<string, any>;
  result?: string;
}

export interface AiModelDefaultRequest {
  defaultChatModel?: string;
  fallbackChatModel?: string;
  defaultRerankModel?: string;
  defaultEmbeddingModel?: string;
}

export function getAiSettings() {
  return request.get<AiSettingsDTO>('/admin/ai/settings');
}

export function updateAiSettings(payload: AiSettingsDTO) {
  return request.put<void>('/admin/ai/settings', payload);
}

export function getAiServiceStatus() {
  return request.get<AiServiceStatusDTO>('/admin/ai/status');
}

export function getAiStats(params?: { model?: string; provider?: string }) {
  return request.get<AiModelStatDTO[]>('/admin/ai/stats', { params });
}

export function getAiAlerts() {
  return request.get<AiAlertEventDTO[]>('/admin/ai/alerts');
}

export function getAiProviders() {
  return request.get<AiSettingsDTO['providers']>('/admin/ai/providers');
}

export function saveAiProvider(payload: AiSettingsDTO['providers'][number]) {
  return payload.code
    ? request.put<void>(`/admin/ai/providers/${payload.code}`, payload)
    : request.post<void>('/admin/ai/providers', payload);
}

export function deleteAiProvider(code: string) {
  return request.del<void>(`/admin/ai/providers/${code}`);
}

export function testAiProvider(code: string, payload: AiSettingsDTO['providers'][number]) {
  const id = code || payload.code || 'test';
  return request.post<{ success: boolean; message: string }>(`/admin/ai/providers/${id}/test`, payload);
}

export function getAiModels() {
  return request.get<AiSettingsDTO['models']>('/admin/ai/models');
}

export function saveAiModel(payload: AiSettingsDTO['models'][number]) {
  return payload.name
    ? request.put<void>(`/admin/ai/models/${payload.name}`, payload)
    : request.post<void>('/admin/ai/models', payload);
}

export function deleteAiModel(name: string) {
  return request.del<void>(`/admin/ai/models/${name}`);
}

export function toggleAiModel(name: string, enabled: boolean) {
  return request.put<void>(`/admin/ai/models/${name}/status`, null, { params: { enabled } });
}

export function updateModelDefaults(payload: AiModelDefaultRequest) {
  return request.put<void>('/admin/ai/models/defaults', payload);
}

export function updateScenarioFallbacks(payload: Record<string, string>) {
  return request.put<void>('/admin/ai/scenarios/fallbacks', payload);
}

export function getAuditLogs() {
  return request.get<AiAuthorizationLogDTO[]>('/admin/ai/audit/logs');
}

export function createConfirmSheetFromTool(tool: AiPendingTool, structured?: AiStructuredReply): ConfirmSheet {
  const summary = tool.operationGuide?.description || tool.summary || structured?.reply || 'AI 建议执行操作';
  const params = tool.operationGuide?.parameters || tool.params || { 描述: tool.description, 工具: tool.toolName };
  return {
    action: tool.operationGuide?.title || tool.toolName,
    summary,
    params,
    riskLevel: (tool.operationGuide?.riskLevel as ConfirmSheet['riskLevel']) || tool.riskLevel || 'L2'
  };
}
