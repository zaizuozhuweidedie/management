import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/layout/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      component: () => import('@/views/login/index.vue')
    },
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/',
      component: Layout,
      children: [
        {
          path: 'index',
          component: () => import('@/views/index/index.vue')
        },
        {
          path: 'clazz',
          component: () => import('@/views/clazz/index.vue')
        },
        {
          path: 'stu',
          component: () => import('@/views/stu/index.vue')
        },
        {
          path: 'dept',
          component: () => import('@/views/dept/index.vue')
        },
        {
          path: 'emp',
          component: () => import('@/views/emp/index.vue')
        },
        {
          path: 'log',
          component: () => import('@/views/log/index.vue')
        },
        {
          path: 'report/emp',
          component: () => import('@/views/report/emp/index.vue')
        },
        {
          path: 'report/stu',
          component: () => import('@/views/report/stu/index.vue')
        }
      ]
    }
  ]
})

export default router

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.path === '/login') {
    if (token) {
      next('/index')
    } else {
      next()
    }
    return
  }

  if (!token) {
    next('/login')
    return
  }

  next()
})
