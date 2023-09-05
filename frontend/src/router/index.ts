import { createRouter, createWebHistory } from 'vue-router';
import NProgress from 'nprogress'; // progress bar
import 'nprogress/nprogress.css';

import { appRoutes } from './routes';
import { REDIRECT_MAIN, NOT_FOUND_ROUTE, DEFAULT_LAYOUT } from './routes/base';
import createRouteGuard from './guard';

NProgress.configure({ showSpinner: false }); // NProgress Configuration

const chatRouters = {
  path: '/conversation',
  name: 'chat',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.chat',
    requiresAuth: true,
    icon: 'icon-dashboard',
    order: 0,
  },
  children: [
    {
      path: ':applicationId',
      name: 'conversation',
      component: () => import('@/views/chat/index.vue'),
      meta: {
        locale: 'menu.conversation',
        requiresAuth: true,
        roles: ['*'],
      },
      children: [
        {
          path: ':id',
          name: 'message',
          component: () => import('@/views/chat/index.vue'),
          meta: {
            locale: 'menu.conversation',
            requiresAuth: true,
            roles: ['*'],
          },
        },
      ],
    },
  ],
};
const knowledgeRouters = {
  path: '/knowledge',
  name: 'knowledge',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.knowledge',
    requiresAuth: true,
    icon: 'icon-dashboard',
    order: 0,
  },
  children: [
    {
      path: 'list',
      name: 'knowledge-list',
      component: () => import('@/views/knowledge/index.vue'),
      meta: {
        locale: 'menu.knowledge',
        requiresAuth: true,
        roles: ['*'],
      },
    },
  ],
};

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: 'login',
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login/index.vue'),
      meta: {
        requiresAuth: false,
      },
    },
    ...appRoutes,
    chatRouters,
    knowledgeRouters,
    REDIRECT_MAIN,
    NOT_FOUND_ROUTE,
  ],
  scrollBehavior() {
    return { top: 0 };
  },
});

createRouteGuard(router);

export default router;
