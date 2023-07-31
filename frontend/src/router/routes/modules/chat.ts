import { AppRouteRecordRaw } from '../types';

const CONVERSATION: AppRouteRecordRaw = {
  component: undefined,
  path: '/chat/conversation',
  name: 'conversation',
  meta: {
    locale: 'menu.chat',
    requiresAuth: true,
    icon: 'icon-message',
    order: 0,
  },
};

export default CONVERSATION;
