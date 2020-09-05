<!--注册页面-->
<template>
  <el-container>
    <el-header style="text-align: right; font-size: 12px">
      <h1 id="title">StrangeClass</h1>
      <el-dropdown @command="handleCommand">
        <i class="el-icon-setting" style="margin-right: 15px"></i>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="a">登录</el-dropdown-item>
          <el-dropdown-item command="b">注册</el-dropdown-item>
          <el-dropdown-item v-if="this.$store.state.hasLogin" command="c">课程列表</el-dropdown-item>
          <el-dropdown-item v-if="this.$store.state.hasLogin" command="d">退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <span>{{userName}}</span>
      <el-avatar v-if="!this.$store.state.hasLogin" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" @error="errorHandler">
        <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
      </el-avatar>
      <el-avatar v-if="this.$store.state.hasLogin" :src="urlBase + image" @error="errorHandler">
        <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
      </el-avatar>
    </el-header>

    <el-container style="margin-top: 20px">
      <el-main>
        <h1 class="main-form-title">注册</h1>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="用户名" prop="name">
            <el-input v-model="ruleForm.name"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="ruleForm.password"></el-input>
          </el-form-item>
          <el-form-item label="密码重复" prop="passwordAgain">
            <el-input type="password" v-model="ruleForm.passwordAgain"></el-input>
          </el-form-item>
          <el-form-item label="角色" prop="role">
            <el-radio-group v-model="ruleForm.role">
              <el-radio label="Teacher">讲师</el-radio>
              <el-radio label="Student">学生</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
            <el-button type="success" @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Login4',
  data () {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.ruleForm.passwordAgain !== '') {
          this.$refs.ruleForm.validateField('passwordAgain')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      userName: '',
      image: this.$store.state.image,
      ruleForm: {
        name: '',
        password: '',
        passwordAgain: '',
        role: ''
      },
      rules: {
        name: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 2, max: 12, message: '长度在 2 到 12 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, validator: validatePass, trigger: 'blur'},
          {validator: validatePass, trigger: 'blur'},
          {max: 12, message: '长度小于等于 12 个字符', trigger: 'blur'}
        ],
        passwordAgain: [
          {required: true, validator: validatePass2, trigger: 'blur'},
          {validator: validatePass2, trigger: 'blur'},
          {max: 12, message: '长度小于等于 12 个字符', trigger: 'blur'}
        ],
        role: [
          {required: true, message: '请选择角色', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let form = this.ruleForm
          let notify = this.$notify
          let ref = this.$refs
          this.$axios.post('/user/register', {
            'name': form.name,
            'password': form.password,
            'role': form.role
          })
            .then(function (res) {
              if (res.data.successful) {
                notify.success(
                  {
                    message: '注册成功'
                  })
                ref[formName].resetFields() // 重置表单
                return true
              } else {
                // 显示错误信息
                notify.error(
                  {
                    message: res.data.message
                  })
              }
            })
            .catch(function (err) {
              // 与后台Post错误
              notify.error(
                {
                  message: err.toString()
                })
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
    handleCommand (command) {
      if (command === 'a') {
        this.$router.push({path: '/Login'})
      } else if (command === 'b') {
        this.$router.push({path: '/Register'})
      } else if (command === 'c') {
        this.$router.push({path: '/CourseList'})
      } else if (command === 'd') {
        this.LogoutPost()
        this.$router.push({path: '/Login'})
      }
      return false
    },
    errorHandler () {
      return true
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
#title{
  float: left;
  margin: 0px 0px 0px 0px;
  color: #000000;
}
.main-form-title{
  margin-top: 5%;
  text-align: center;
  font-size: xx-large;
}
.el-form{
  width: 400px;
  margin: auto;
}
.el-avatar{
  margin-top: 8px;
  margin-left: 8px;
  float: right;
}
</style>
