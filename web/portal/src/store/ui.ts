import { defineStore } from 'pinia';

export const useUiStore = defineStore('ui', {
  state: () => ({
    isNavigating: false,
  }),
  actions: {
    startNavigate() {
      this.isNavigating = true;
    },
    endNavigate() {
      this.isNavigating = false;
    },
  },
});

