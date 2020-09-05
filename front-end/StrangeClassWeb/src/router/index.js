import Vue from 'vue'
import Router from 'vue-router'
// import store from '../store/index'
import CourseManager from '../page/CourseManager'
import CourseModify from '../page/CourseModify'
import Login from '../page/Login'
import Register from '../page/Register'
import TeacherLive from '../page/TeacherLive'
import StudentLive from '../page/StudentLive'
import CourseList from '../page/CourseList'
import Personal from '../page/Personal'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Index',
      component: Login
    },
    {
      path: '/CourseManager',
      name: 'CourseManager',
      component: CourseManager
    },
    {
      path: '/CourseModify',
      name: 'CourseModify',
      component: CourseModify
    },
    {
      path: '/Login',
      name: 'Login',
      component: Login
    },
    {
      path: '/Register',
      name: 'Register',
      component: Register
    },
    {
      path: '/TeacherLive',
      name: 'TeacherLive',
      component: TeacherLive
    },
    {
      path: '/StudentLive',
      name: 'StudentLive',
      component: StudentLive
    },
    {
      path: '/CourseList',
      name: 'CourseList',
      component: CourseList
    },
    {
      path: '/Personal',
      name: 'Personal',
      component: Personal
    }
    // {
    //   path: '/activity',
    //   name: 'activity',
    //   component: Activity,
    //   meta: {
    //     requireAuth: true // 添加该字段，表示进入这个路由是需要登录的
    //   }
    // }
  ]
})

// 注册全局钩子用来拦截导航
// router.beforeEach((to, from, next) => {
//   const token = store.state.token
//   if (to.meta.requireAuth) { // 判断该路由是否需要登录权限
//     if (token) { // 通过vuex state获取当前的token是否存在
//       next()
//     } else {
//       console.log('该页面需要登陆')
//       next({
//         path: '/login'
//         // query: {redirect: to.fullPath} // 将跳转的路由path作为参数，登录成功后跳转到该路由
//       })
//     }
//   } else {
//     next()
//   }
// })

export default router
