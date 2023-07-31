import { AppRouteRecordRaw } from '../types';

const KNOWLEDGE: AppRouteRecordRaw = {
  path: '/knowledge/list',
  name: 'knowledge-list',
  meta: {
    locale: 'menu.knowledge',
    requiresAuth: true,
    icon: 'icon-layers',
    order: 0,
  },
};

export default KNOWLEDGE;
