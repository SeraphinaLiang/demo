<template>
  <div class="topnav">
    <div class='logo'>{{ title }}</div>
    <span>{{ currentArea }}</span>
    <ul>
      <li class="first"><router-link to="register">首页</router-link></li>
      <li v-if="this.$store.state.hasLogin"><router-link to="manager">课程管理</router-link></li>
      <li v-if="this.$store.state.hasLogin"><router-link to="personal">个人中心</router-link></li>
    </ul>
    <div class="login">
      <span v-if="!this.$store.state.hasLogin" @click="setLoginHas"><router-link to="login">登录</router-link></span>
      <span v-if="this.$store.state.hasLogin" @click="setLoginHasNot"><router-link to="login">退出</router-link></span>
      <span>|</span>
      <span><router-link to="register">注册</router-link></span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    currentArea: String
  },
  name: 'topnav',
  data () {
    return {
      title: 'Online Course'
    }
  },
  methods: {
    setLoginHas () {
      return this.$store.commit('SET_LOGINSTATE', true)
    },
    setLoginHasNot () {
      return this.$store.commit('SET_LOGINSTATE', false)
    }
  }
}
</script>

<style>
/*设置整体*/
*{
  padding: 0;
  margin: 0;
  /*font-family: "微软雅黑";*/
}
/*设置header的div区*/
.topnav{
  height: 72px ;
  background: #dd6161 ;
}
/*设置logo的div区*/
.topnav .logo{
  color: #fff ;
  font-size: 30px ;
  font-weight:500 ;
  line-height: 72px ;
  margin-left: 20px ;
  display:inline-block ;
  float: left ;/*左浮动*/
}
/*设置li标签里*/
.topnav ul li{
  float: left ;
  color: #fff ;
  display: inline-block ;
  width: 112px ;
  height: 72px ;
  text-align: center ;
  line-height:72px ;
  cursor: pointer ;/*光标显示成小手型*/
}
/*细节调整*/
ul li{
  list-style: none ;/*li的小圆点去掉*/
}
.topnav ul li.first{
  margin-left: 30px ;/*设置“首页”与“原创文字”间距*/
  background:#74b0e2 ;/*“首页”设置默认背景*/
}
/*设置a链接标签*/
a{
  color: #fff ;
  text-decoration: none ;
}
/*设置导航添加hover事件*/
.topnav ul li:hover{
  background:#74b0e2 ;
}
/*设置登录、注册按钮*/
.topnav .login{
  float: right ;
  color: #fff ;
  line-height: 72px ;
  margin-right: 20px ;
}
/*.topnav .login button{*/
  /*background: #458fce;*/
  /*border: #458fce;*/
/*}*/
</style>
