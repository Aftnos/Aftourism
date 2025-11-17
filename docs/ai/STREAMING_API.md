# AI 流式对话接口说明

## 接口列表

| 模块 | 路径 | 方法 | 访问角色 |
| ---- | ---- | ---- | -------- |
| AI 功能 | `/ai/conversations/chat` | POST | 管理员 (AI_ASSIST:USE) |
| AI 功能 | `/ai/conversations/chat/stream` | POST (text/event-stream) | 管理员 (AI_ASSIST:USE) |

## 调用示例

### 普通对话
```http
POST /ai/conversations/chat
Content-Type: application/json
Authorization: Bearer <JWT>

{
  "conversationId": "可选",
  "message": "你好",
  "stream": false
}
```

### 流式对话
```
POST /ai/conversations/chat/stream
Accept: text/event-stream
```

服务端返回 `\n` 分隔的 JSON：
```json
{"type":"start","data":{"conversationId":null}}
{"type":"chunk","data":{"content":"你好"}}
{"type":"end","data":{...AiChatResponse}}
```
客户端可在任意时刻中断请求以终止流式输出。
