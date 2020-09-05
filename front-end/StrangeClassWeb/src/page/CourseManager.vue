<!--课程管理页面-->
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
        <el-menu default-active="3" class="el-menu-vertical-demo"
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
        <h1 class="main-form-title-with-aside">课程管理</h1>
        <el-table
          ref="multipleTable"
          :data="courseView"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            prop="name"
            label="课程名称"
            width="200"
            show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            prop="classification"
            label="课程分类"
            width="500">
          </el-table-column>
          <el-table-column
            label="课程操作"
            width="120">
            <template slot-scope="scope">
            <el-button type="primary" @click="courseModify(scope.row)">修改课程</el-button>
            </template>
          </el-table-column>
          <el-table-column width="120">
            <template slot-scope="scope">
              <el-button type="success" @click="openCourseLive(scope.row)">课程直播</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 20px">
          <el-button type="primary" @click="addNewCourse()">添加课程</el-button>
          <el-button type="danger" @click="deleteCourse()">删除课程</el-button>
          <el-button type="warning" @click="clearAllSelection()">取消选择</el-button>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
export default {
  name: 'Login1',
  inject: ['reload'],
  data () {
    return {
      courseView: [],
      multipleSelection: [],
      image: this.$store.state.image,
      name: this.$store.state.name,
      isCollapse: true
    }
  },
  methods: {
    openCourseLive (row) {
      this.$store.dispatch('SetCurrentCourse', row.id)
      this.$router.push({path: '/TeacherLive'})
    },
    courseModify (row) {
      this.$store.dispatch('SetCurrentCourseMsg', row)
      this.$router.push({path: '/CourseModify'})
    },
    addNewCourse () {
      if (this.$store.state.role === 'Teacher') {
        let notify = this.$notify
        let _this = this
        this.$axios.post('/course/createCourse',
          {
            'classification': '未定义分类',
            'name': '未定义课程'
          })
          .then(function (res) {
            console.log(res)
            // Post成功，但创建课程成功
            if (res.data.successful) {
              notify.success(
                {
                  message: '添加课程成功'
                })
              _this.reload()
              return true
            } else {
              // Post成功，但创建课程失败，返回相应的错误信息
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
      }
    },
    deleteCourse () {
      // console.log(this.multipleSelection[0].id)
      if (this.multipleSelection) {
        this.multipleSelection.forEach(row => {
          this.deleteCoursePost(row.id)
        })
      }
    },
    clearAllSelection () {
      this.$refs.multipleTable.clearSelection()
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    handleCommand (command) {
      if (command === 'a') {
        this.LogoutPost()
      } else {
        return false
      }
    },
    deleteCoursePost (id) {
      let notify = this.$notify
      let _this = this
      this.$axios.post('/course/deleteCourse', {
        'id': id
      })
        .then(function (res) {
          console.log(res)
          // Post成功，课程删除成功
          if (res.data.successful) {
            _this.reload()
            return true
          } else {
            // Post成功，但课程删除失败，返回相应的错误信息
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
    mountViewCourse () {
      let notify = this.$notify
      let store = this.$store
      let _this = this
      this.$axios.post('/course/viewCourse', {
        'id': store.state.id
      })
        .then(function (res) {
          console.log(res)
          // Post成功，获取课程列表成功
          if (res.data.successful) {
            _this.courseView = res.data.data.courseVoList
            console.log(_this.courseView)
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
    // window.addEventListener('beforeunload', e => this.LogoutPost())
    this.mountViewCourse()
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
</style>
