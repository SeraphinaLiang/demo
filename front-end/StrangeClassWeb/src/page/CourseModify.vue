<!--课程信息修改页面-->
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
          <el-menu class="el-menu-vertical-demo"
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
        <h1 class="main-form-title-with-aside">课程修改</h1>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="课程名称" prop="name">
            <el-input v-model="ruleForm.name"></el-input>
          </el-form-item>
          <el-form-item label="课程分类" prop="group">
            <el-cascader v-model="ruleForm.group"
              :options="options"
              :props="props"
              collapse-tags
              clearable></el-cascader>
          </el-form-item>
          <el-form-item label="课程信息" prop="info">
            <el-input type="textarea" v-model="ruleForm.info"
                      :autosize="{ minRows: 5, maxRows: 5}"
                      maxlength="50" show-word-limit
                      placeholder="请输入内容"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
            <el-button type="success" @click="resetForm('ruleForm')">重置</el-button>
            <el-button type="danger" @click="cancelCourseCreate">取消</el-button>
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Login2',
  data () {
    return {
      image: this.$store.state.image,
      name: this.$store.state.name,
      ruleForm: {
        name: this.$store.state.currentCourseMsg.name,
        group: this.$store.state.currentCourseMsg.classification,
        info: this.$store.state.currentCourseMsg.info
      },
      rules: {
        name: [
          {required: true, message: '请输入课程名称', trigger: 'blur'},
          {min: 5, max: 12, message: '长度在 5 到 12 个字符', trigger: 'blur'}
        ],
        group: [
          {required: true, message: '请选择一个课程分类', trigger: 'change'}
        ],
        info: [
          {required: false, message: '请课程信息', trigger: 'blur'}
        ]
      },
      isCollapse: true,
      props: { multiple: false }, // 课程分类的多选设置
      options: [{
        value: 1,
        label: '编程',
        children: [{
          value: 2,
          label: 'Java',
          children: [
            { value: 'JavaEE', label: 'JavaEE' },
            { value: 'SpringMVC', label: 'SpringMVC' },
            { value: 'SpringBoot', label: 'SpringBoot' }
          ]
        }, {
          value: 7,
          label: 'C#',
          children: [
            { value: 'C#基础', label: 'C#基础' },
            { value: '.NET Core', label: '.NET Core' },
            { value: 'C#网络编程', label: 'C#网络编程' }
          ]
        }, {
          value: 12,
          label: 'Vue',
          children: [
            { value: 'mpVue', label: 'mpVue' },
            { value: 'Vue基础', label: 'Vue基础' }
          ]
        }]
      }, {
        value: 16,
        label: '中小教育',
        children: [{
          value: 17,
          label: '数学',
          children: [
            { value: '初等数学', label: '初等数学' },
            { value: '中等数学', label: '中等数学' },
            { value: '高等数学', label: '高等数学' }
          ]
        }, {
          value: 21,
          label: '英语',
          children: [
            { value: '初等英语', label: '初等英语' },
            { value: '中等英语', label: '中等英语' },
            { value: '高等英语', label: '高等英语' }
          ]
        }]
      }],
      selectGroup: ''
    }
  },
  methods: {
    cancelCourseCreate () {
      this.$router.push({path: '/CourseManager'})
    },
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let form = this.ruleForm
          let notify = this.$notify
          let store = this.$store
          let router = this.$router
          let _this = this
          this.$axios.post('/course/changeUserInformation',
            {
              'classification': _this.classifiction,
              'name': form.name.toString(),
              'id': store.state.currentCourseMsg.id,
              'info': form.info
            })
            .then(function (res) {
              console.log(res)
              // Post成功，但创建课程成功
              if (res.data.successful) {
                // 转跳至课程列表
                notify.success(
                  {
                    message: '修改课程成功'
                  })
                router.push({path: '/CourseManager'})
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
        } else {
          this.$notify.error(
            {
              message: '表单检验不通过！'
            }
          )
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    handleCommand (command) {
      if (command === 'a') {
        this.LogoutPost()
      } else {
        return false
      }
    },
    LogoutPost () {
      let router = this.$router
      let notify = this.$notify
      let store = this.$store
      let _this = this
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
  computed: {
    // 计算属性的 getter
    classifiction: function () {
      // `this` 指向 vm 实例
      if (this.ruleForm.group instanceof Array) {
        return this.ruleForm.group[this.ruleForm.group.length - 1]
      } else {
        return this.ruleForm.group
      }
    }
  }
}
</script>

<style scoped>
.el-header {
  background-color: #409EFF;
  color: #000000;
  line-height: 60px;
}

.el-aside {
  color: #409EFF;
}
#title{
  float: left;
  margin: 0px 0px 0px 0px;
  color: #000000;
}
.main-form-title-with-aside{
  margin-top: 5%;
  font-size: xx-large;
  margin-left: 120px;
}
.el-form{
  width: 500px;
  margin: 0px 50px 0px 100px;
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
