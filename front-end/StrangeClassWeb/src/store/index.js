// store index.js
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
// 初始化时用sessionStore.getItem('token'),这样子刷新页面就无需重新登录
const state = {
  id: window.localStorage.getItem('id'),
  image: window.localStorage.getItem('image'),
  info: window.localStorage.getItem('info'),
  name: window.localStorage.getItem('name'),
  role: window.localStorage.getItem('role'),
  appId: window.localStorage.getItem('appId'),
  sdkId: window.localStorage.getItem('sdkId'),
  sdkToken: window.localStorage.getItem('sdkToken'),
  hasLogin: window.localStorage.getItem('hasLogin'),
  currentCourse: window.localStorage.getItem('currentCourse'),
  currentTeacher: window.localStorage.getItem('currentTeacher'),
  currentCourseMsg: window.localStorage.getItem('currentCourseMsg')
}
const mutations = {
  SET_ID: (state, data) => {
    state.id = data
    window.localStorage.setItem('id', data)
  },
  SET_IMAGE: (state, data) => {
    state.image = data
    window.localStorage.setItem('image', data)
  },
  SET_INFO: (state, data) => {
    state.info = data
    window.localStorage.setItem('info', data)
  },
  SET_NAME: (state, data) => {
    state.name = data
    window.localStorage.setItem('name', data)
  },
  SET_ROLE: (state, data) => {
    state.role = data
    window.localStorage.setItem('role', data)
  },
  SET_APPID: (state, data) => {
    state.appId = data
    window.localStorage.setItem('appId', data)
  },
  SET_SDKID: (state, data) => {
    state.sdkId = data
    window.localStorage.setItem('sdkId', data)
  },
  SET_SDKTOKEN: (state, data) => {
    state.sdkToken = data
    window.localStorage.setItem('sdkToken', data)
  },
  SET_HASLOGIN: (state, data) => {
    state.hasLogin = data
    window.localStorage.setItem('hasLogin', data)
  },
  SET_CURRENTCOURSE: (state, data) => {
    state.currentCourse = data
    window.localStorage.setItem('currentCourse', data)
  },
  SET_CURRENTTEACHER: (state, data) => {
    state.currentTeacher = data
    window.localStorage.setItem('currentTeacher', data)
  },
  SET_CURRENTCOURSEMSG: (state, data) => {
    state.currentCourseMsg = data
    window.localStorage.setItem('currentCourseMsg', data)
  }
}

const actions = {
  Login (context, userData) {
    // if (context.state.hasLogin === undefined || context.state.hasLogin === false ||
    //   context.state.hasLogin === null) {
    //   context.commit('SET_ID', userData.id)
    //   context.commit('SET_IMAGE', userData.image)
    //   context.commit('SET_INFO', userData.info)
    //   context.commit('SET_NAME', userData.name)
    //   context.commit('SET_ROLE', userData.role)
    //   context.commit('SET_APPID', userData.sdkAppId)
    //   context.commit('SET_SDKID', userData.sdkId)
    //   context.commit('SET_SDKTOKEN', userData.sdkToken)
    //   context.commit('SET_HASLOGIN', true)
    // }
    context.commit('SET_ID', userData.id)
    context.commit('SET_IMAGE', userData.image)
    context.commit('SET_INFO', userData.info)
    context.commit('SET_NAME', userData.name)
    context.commit('SET_ROLE', userData.role)
    context.commit('SET_APPID', userData.sdkAppId)
    context.commit('SET_SDKID', userData.sdkId)
    context.commit('SET_SDKTOKEN', userData.sdkToken)
    context.commit('SET_HASLOGIN', true)
  },
  Logout () {
    if (state.hasLogin) {
      this.commit('SET_ID', null)
      this.commit('SET_IMAGE', null)
      this.commit('SET_INFO', null)
      this.commit('SET_NAME', null)
      this.commit('SET_ROLE', null)
      this.commit('SET_APPID', null)
      this.commit('SET_SDKID', null)
      this.commit('SET_SDKTOKEN', null)
      this.commit('SET_HASLOGIN', false)
      this.commit('SET_CURRENTCOURSE', null)
      this.commit('SET_CURRENTTEACHER', null)
      this.commit('SET_CURRENTCOURSEMSG', null)
    }
  },
  SetUserInfo (context, userData) {
    context.commit('SET_INFO', userData.info)
    context.commit('SET_NAME', userData.name)
  },
  SetCurrentCourse (context, userData) {
    context.commit('SET_CURRENTCOURSE', userData)
  },
  SetCurrentTeacher (context, userData) {
    context.commit('SET_CURRENTTEACHER', userData)
  },
  SetCurrentCourseMsg (context, userData) {
    context.commit('SET_CURRENTCOURSEMSG', userData)
  },
  SetImage (context, userData) {
    context.commit('SET_IMAGE', userData)
  }
}
export default new Vuex.Store({
  state,
  mutations,
  actions
})
