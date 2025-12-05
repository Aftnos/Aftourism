import type { App, DirectiveBinding } from 'vue';
import { useAuthStore } from '@/store/auth';

// v-can 指令：基于权限隐藏元素
export function registerGuardDirective(app: App) {
  app.directive('can', {
    mounted(el: HTMLElement, binding: DirectiveBinding<string | string[]>) {
      const auth = useAuthStore();
      const code = binding.value;
      const allow = auth.allow(code);
      if (!allow) {
        el.style.display = 'none';
      }
    }
  });
}
