import { createPinia } from 'pinia';
import useChatStore from './modules/chat';
import useAppStore from './modules/app';
import useUserStore from './modules/user';
import useTabBarStore from './modules/tab-bar';

const pinia = createPinia();

export { useAppStore, useUserStore, useTabBarStore, useChatStore };
export default pinia;
