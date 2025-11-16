import { nanoid } from 'nanoid';

// 生成操作追踪号，所有执行类操作都需要打印
export function createTraceId(prefix = 'op') {
  return `${prefix}_${nanoid(10)}`;
}
