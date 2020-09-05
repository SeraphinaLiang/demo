<!--个人中心-->
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
        <el-menu default-active="2" class="el-menu-vertical-demo"
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
        <h1 class="main-form-title-with-aside">个人中心</h1>
        <br>
        <div>
          <el-tabs tab-position="right" style="margin-right: 10%">
            <el-tab-pane label="用户信息">
              <!-- 个人信息 表单 -->
              <el-form ref="ruleFormPersonalInfo" :model="ruleFormPersonalInfo" :rules="rulesPersonalInfo" label-width="80px">
                <!-- 用户头像 -->
                <el-form-item prop="url">
                    <!-- 头像显示 -->
                    <div class="demo-image">
                      <div class="block">
                        <!-- fit值：cover 或 contain -->
                        <el-image id="image" style="width: 100px; height: 100px" :src="urlBase+ruleFormPersonalInfo.url+'?'+Date()" fit="cover"></el-image>
                        <el-button type="text" title="注意！一旦上传则直接修改" onmouseover="" @click="uploadProfile = true">修改头像</el-button>
                      </div>
                    </div>
                    <!-- 上传头像 dialog弹窗-->
                    <el-dialog title="上传头像" width="420px" :visible.sync="uploadProfile" :before-close="beforeDialogClose">
                      <!-- drag upload -->
                      <el-upload
                        class="upload-demo"
                        ref="upload"
                        drag
                        accept=".jpg,.jpeg,.png,.JPG,.JPEG"
                        list-type="picture"
                        :multiple="false"
                        :auto-upload="false"
                        action="no_use"
                        :http-request="uploadPicturePost"
                        :before-upload="beforeAvatarUpload"
                        :on-change="onchangeUpload"
                      >
                        <i class="el-icon-upload"></i>
                        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
                      </el-upload>
                      <!-- 头像预览子弹窗 -->
                      <el-dialog width="30%" title="预览头像" :visible.sync="confirmProfile" append-to-body :before-close="beforeDialogClose">
                        确认更改头像如下吗？<br>
                        <div align="center">
                          <el-image style="width: 200px; height: 200px" :src="previewImgURL" fit="cover"></el-image>
                        </div>
                        <div slot="footer" class="dialog-footer">
                          <el-button @click="confirmCancel">换一个</el-button>
                          <el-button type="primary" @click="confirmSubmit">确认</el-button>
                        </div>
                      </el-dialog>
                      <div slot="footer" class="dialog-footer">
                        <el-button @click="cancelAvatarUpload">取 消</el-button>
                        <!-- <el-button type="primary" @click="confirmProfile = true">打开内层 Dialog</el-button>-->
                      </div>
                    </el-dialog>
                </el-form-item>
                <el-form-item label="用户名" prop="name">
                  <el-input v-model="ruleFormPersonalInfo.name"></el-input>
                </el-form-item>
                <el-form-item label="个人简介" prop="info">
                  <el-input type="textarea" placeholder="请输入个人简介，200字以内" v-model="ruleFormPersonalInfo.info"
                            :autosize="{ minRows: 5, maxRows: 5}"
                            maxlength="200" show-word-limit
                  ></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="personalInfoSubmit('ruleFormPersonalInfo')">确认修改</el-button>
<!--                  <el-button>取消</el-button>-->
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="修改密码">
              <el-form :model="ruleFormPass" :rules="rulesPass" ref="ruleFormPass" label-width="100px" class="demo-ruleForm">
                <el-form-item label="旧密码" prop="oldPassword">
                  <el-input type="password" v-model="ruleFormPass.oldPassword"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input type="password" v-model="ruleFormPass.newPassword"></el-input>
                </el-form-item>
                <el-form-item label="重复新密码" prop="newPasswordAgain">
                  <el-input type="password" v-model="ruleFormPass.newPasswordAgain"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitFormPass('ruleFormPass')">提交</el-button>
                  <el-button type="success" @click="resetFormPass('ruleFormPass')">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="我的收藏">
              <el-card :body-style="{ padding: '0px' }" v-for="(item, index) in courseCollectionList" :key="index">
                <img style="width: 300px; height: 200px;" src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image">
                <div style="display: inline-table; vertical-align: top;">
                  <span style="display: block; margin-left: 10px">{{item.name}}</span>
                  <span style="display: block; margin-left: 10px">讲师：{{item.teacher.name}}</span>
                  <span style="display: block; margin-left: 10px">分类：{{item.classification}}</span>
                </div>
                <div style="position: absolute; left: 80%; bottom: 10%">
                  <el-button v-if="item.status === 1" type="danger" @click="jumpToLive(item.teacher.id, item.id, item)" style="margin: 0px 10px 0px 0px">进入课堂</el-button>
                </div>
              </el-card>
            </el-tab-pane>
            <el-tab-pane label="历史浏览">
              <el-card :body-style="{ padding: '0px' }" v-for="(item, index) in courseHistoryList" :key="index">
                <img style="width: 300px; height: 200px;" src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image">
                <div style="display: inline-table; vertical-align: top;">
                  <span style="display: block; margin-left: 10px">{{item.name}}</span>
                  <span style="display: block; margin-left: 10px">讲师：{{item.teacher.name}}</span>
                  <span style="display: block; margin-left: 10px">分类：{{item.classification}}</span>
                </div>
                <div style="position: absolute; left: 80%; bottom: 10%">
                  <el-button v-if="item.status === 1" type="danger" @click="jumpToLive(item.teacher.id, item.id, item)" style="margin: 0px 10px 0px 0px">进入课堂</el-button>
                </div>
              </el-card>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Personal',
  inject: ['reload'],
  data () {
    var validatePass = (rule, value, callback) => { // 新密码输入检验
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else {
        if (this.ruleFormPass.newPasswordAgain !== '') {
          this.$refs.ruleFormPass.validateField('passwordAgain')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => { // 重复新密码输入检验
      if (value === '') {
        callback(new Error('请再次输入新密码'))
      } else if (value !== this.ruleFormPass.newPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }

    return {
      image: this.$store.state.image,
      name: this.$store.state.name,
      isCollapse: true,
      currentDate: new Date(),
      courseHistoryList: [],
      courseCollectionList: [],

      // 个人中心-用户资料修改
      ruleFormPersonalInfo: { // 用户资料表单
        name: this.$store.state.name,
        info: this.$store.state.info,
        url: this.$store.state.image
      },
      rulesPersonalInfo: {
        name: [
          {required: true, message: '用户名不能为空', trigger: 'blur'},
          {min: 2, max: 12, message: '长度在 2 到 12 个字符', trigger: 'blur'}
        ],
        info: [
          {required: false, message: '请输入个人简介，200字以内', trigger: 'blur'},
          {max: 200, message: '字数已经超出200！请删减', trigger: 'blur'}
        ],
        url: [
          {required: false}
        ]
      },
      // 上传头像对话框显示与否
      uploadProfile: false,
      confirmProfile: false,
      // upload-demo
      previewImgURL: '',
      urlBase: 'http://localhost:8080',

      ruleFormPass: {
        oldPassword: '',
        newPassword: '',
        newPasswordAgain: ''
      },
      rulesPass: {
        oldPassword: [
          {required: true, message: '请输入原密码', trigger: 'blur'},
          {max: 12, message: '长度小于等于 12 个字符', trigger: 'blur'}
        ],
        newPassword: [
          {required: true, validator: validatePass, trigger: 'blur'},
          {validator: validatePass, trigger: 'blur'},
          {max: 12, message: '长度小于等于 12 个字符', trigger: 'blur'}
        ],
        newPasswordAgain: [
          {required: true, validator: validatePass2, trigger: 'blur'},
          {validator: validatePass2, trigger: 'blur'},
          {max: 12, message: '长度小于等于 12 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    cancelCourseCreate () {
      alert('跳转回课程管理页面')
    },
    /* 个人信息修改表单 */
    InfoPost () {
      let notify = this.$notify
      let store = this.$store
      let _this = this
      this.$axios.post('/user/changeInformation',
        {
          'info': this.ruleFormPersonalInfo.info.toString(),
          'name': this.ruleFormPersonalInfo.name.toString()
        })
        .then(function (res) {
          console.log(res)
          // Post成功，但修改信息成功，返回相应的用户信息
          if (res.data.successful) {
            // 把res.data中的数据写入vuex
            store.dispatch('SetUserInfo', res.data.data)
            console.log(store.state)
            alert('修改信息成功！')
            // 刷新页面
            _this.reload()
            return true
          } else {
            // Post成功，但修改信息失败，返回相应的错误信息
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
              message: err.toString() + '失败post'
            })
        })
    },
    // 表单提交
    personalInfoSubmit (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.InfoPost()
        } else {
          this.$message.error('信息填写错误！')
          return false
        }
      })
    },
    /* 上传头像对话框 */
    beforeDialogClose (done) { // 用户临时退出上传头像，应清空
      this.$refs.upload.clearFiles()
      done()
    },
    cancelAvatarUpload () { // 用户临时退出上传头像，应清空
      this.uploadProfile = false
      this.$refs.upload.clearFiles()
    },
    beforeAvatarUpload (file) { // 大小限制
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      // this.previewImgURL = URL.createObjectURL(file.raw)
      // this.confirmProfile = true // 预览图片
      // alert('' + this.confirmProfile)
      return isLt2M
    },
    onchangeUpload (file) {
      // 预保存上传的图片
      this.previewImgURL = URL.createObjectURL(file.raw)
      this.confirmProfile = true // 预览图片
    },
    // 自定义上传方法
    uploadPicturePost (file) {
      let notify = this.$notify
      let img = new FormData()
      let _this = this
      img.append('image', file.file)
      this.$axios.post('/user/uploadImage', img, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(function (res) {
          // Post成功，上传图片成功
          if (res.data.successful) {
            _this.image = res.data.data.imageUrl
            _this.$store.dispatch('SetImage', res.data.data.imageUrl)
            _this.ruleFormPersonalInfo.url = res.data.data.imageUrl
            notify.success(
              {
                message: '成功修改头像'
              })
            // 关闭，并清空列表
            _this.confirmProfile = false
            _this.uploadProfile = false
            _this.$refs.upload.clearFiles()
            _this.$router.push({path: '/Personal'})
            return true
          } else {
            // Post成功，但上传图片失败
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
    confirmCancel () {
      this.confirmProfile = false
      this.uploadProfile = true
      this.previewImgURL = null
      this.$refs.upload.clearFiles()
    },
    confirmSubmit () {
      // post上传头像 存到数据库，显示在个人中心
      this.$refs.upload.submit()
    },

    /* 修改密码表单 */
    submitFormPass (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let form = this.ruleFormPass
          let notify = this.$notify
          let ref = this.$refs
          this.$axios.post('/user/changePassword', {
            'oldPassword': form.oldPassword,
            'newPassword': form.newPassword
          })
            .then(function (res) {
              if (res.data.successful) {
                notify.success(
                  {
                    message: '修改密码成功'
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
    resetFormPass (formName) {
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
    mountCourseHistoryList () {
      let notify = this.$notify
      let _this = this
      this.$axios.post('/historyCourse/getHistoryCourseList')
        .then(function (res) {
          // Post成功，获取个人的课程历史成功
          if (res.data.successful) {
            // 把个人的课程历史信息写入变量
            _this.courseHistoryList = res.data.data.courseVoList
            return true
          } else {
            // Post成功，但获取个人的课程历史失败
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
    mountCourseCollectionList () {
      let notify = this.$notify
      let _this = this
      this.$axios.post('/collectionCourse/getCollectionCourseList')
        .then(function (res) {
          // Post成功，获取个人的收藏记录成功
          if (res.data.successful) {
            // 把个人的收藏记录信息写入变量
            _this.courseCollectionList = res.data.data.courseVoList
            return true
          } else {
            // Post成功，但获取个人的收藏记录失败
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
    jumpToLive (teacherID, courseID, courseMsg) {
      this.$store.dispatch('SetCurrentTeacher', teacherID)
      this.$store.dispatch('SetCurrentCourse', courseID)
      this.$store.dispatch('SetCurrentCourseMsg', courseMsg)
      this.$router.push({path: '/StudentLive'})
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
    console.log(this.urlBase + this.url)
    this.mountCourseHistoryList()
    this.mountCourseCollectionList()
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
  margin-top: 3%;
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
.el-card{
  margin-bottom: 15px;
  margin-right: 50px;
}
.el-form{
  width: 500px;
  margin: 0px 50px 0px 0px;
}
</style>
