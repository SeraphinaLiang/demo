<!--课程列表页面-->
<template>
  <el-container>
    <el-header style="text-align: right; font-size: 12px">
      <h1 id="title">StrangeClass</h1>
      <el-dropdown @command="handleCommand">
        <i class="el-icon-setting" style="margin-right: 15px"></i>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="a">退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <span>{{name}}</span>
      <el-avatar :src="urlBase + image" @error="errorHandler">
        <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
      </el-avatar>
    </el-header>

    <el-container style="margin-top: 20px">
      <el-aside>
        <el-radio-group v-model="isCollapse" style="margin-bottom: 20px;">
          <el-radio-button :label="false">展开</el-radio-button>
          <el-radio-button :label="true">收起</el-radio-button>
        </el-radio-group>
        <el-menu default-active="1" class="el-menu-vertical-demo"
                 background-color="#545c64"
                 text-color="#fff"
                 active-text-color="#ffd04b"
                 @open="handleOpen" @close="handleClose" :collapse="isCollapse"
        @select="selectJump">
          <el-menu-item index="1">
            <i class="el-icon-menu"></i>
            <span slot="title">课程列表</span>
          </el-menu-item>
          <el-menu-item index="2">
            <i class="el-icon-setting"></i>
            <span slot="title">个人中心</span>
          </el-menu-item>
          <el-menu-item index="3" v-if="this.$store.state.role === 'Teacher'">
            <i class="el-icon-document"></i>
            <span slot="title">课程管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <h1 class="main-form-title-with-aside">课程列表</h1>
        <el-row>
           <!--:offset="index = 0 ? 2 : 0"-->
          <el-col :span="6" v-for="(item, index) in courseList" :key="index">
            <el-card :body-style="{ padding: '0px' }">
              <img v-if="item.image === undefined" src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image">
              <img v-else :src="item.image" class="image">
              <div style="padding: 14px;">
                <span>{{item.name}}</span>
                <span style="margin-left: 10px">讲师：{{item.teacher.name}}</span>
                <div class="bottom clearfix">
                  <span>分类：{{item.classification}}</span>
                  <el-button  v-if="item.status === 1" type="primary" @click="jumpToLive(item.teacher.id, item.id, item)" size="small" class="button">进入课堂</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Login7',
  data () {
    return {
      image: this.$store.state.image,
      name: this.$store.state.name,
      isCollapse: true,
      currentDate: new Date(),
      courseList: []
    }
  },
  methods: {
    handleCommand (command) {
      if (command === 'a') {
        this.LogoutPost()
      } else {
        return false
      }
    },
    jumpToLive (teacherID, courseID, courseMsg) {
      this.$store.dispatch('SetCurrentTeacher', teacherID)
      this.$store.dispatch('SetCurrentCourse', courseID)
      this.$store.dispatch('SetCurrentCourseMsg', courseMsg)
      this.$router.push({path: '/StudentLive'})
    },
    LogoutPost () {
      let router = this.$router
      let notify = this.$notify
      let store = this.$store
      this.$axios.post('/user/logout')
        .then(function (res) {
          console.log(res)
          // Post成功，退出成功
          if (res.data.successful) {
            // 清除vuex和sessionStorage中的数据
            store.dispatch('Logout')
            console.log(store.state)
            // 转跳至登录
            router.push({path: '/Login'})
            return true
          } else {
            // Post成功，但退出失败，返回相应的错误信息
            notify.error(
              {
                message: res.data.message
              })
          }
        })
        // Post失败
        .catch(function (err) {
          notify.error(
            {
              message: err.toString()
            })
        })
      return false
    },
    mountCourseList () {
      let notify = this.$notify
      let _this = this
      this.$axios.post('/course/getAllCourse')
        .then(function (res) {
          console.log(res)
          // Post成功，获取全部课程成功
          if (res.data.successful) {
            // 把课程信息写入变量
            _this.courseList = res.data.data.courseVoList
            console.log(_this.courseList)
            return true
          } else {
            // Post成功，但获取全部课程失败
            notify.error(
              {
                message: res.data.message
              })
          }
        })
        // Post失败
        .catch(function (err) {
          if (err.response.status === 400) {
            _this.$store.dispatch('Logout')
            _this.$router.push({path: '/Login'})
          } else {
            notify.error(
              {
                message: err.toString()
              })
          }
        })
      return false
    },
    selectJump (key, keyPath) {
      switch (key) {
        case '1': this.$router.push({path: '/CourseList'})
          break
        case '2': this.$router.push({path: '/Personal'})
          break
        case '3': this.$router.push({path: '/CourseManager'})
          break
      }
    },
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    },
    errorHandler () {
      return true
    }
  },
  mounted () {
    this.mountCourseList()
  }
}
</script>

<style scoped>
.el-header {
  background-color: #409EFF;
  color: #000000;
  line-height: 60px;
}
.main-form-title-with-aside{
  margin-top: 5%;
  text-align: left;
  font-size: xx-large;
}
.el-aside {
  color: #409EFF;
}
#title{
  float: left;
  margin: 0px 0px 0px 0px;
  color: #000000;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
.el-avatar{
  margin-top: 8px;
  margin-left: 8px;
  float: right;
}
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  /*line-height: 12px;*/
  height: 30px;
}

.button {
  padding: 8px 13px 8px 13px;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
.el-card{
  margin-bottom: 15px;
  margin-right: 50px;
}
</style>
