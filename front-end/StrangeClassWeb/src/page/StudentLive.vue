<!--学生观看直播页面-->
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
      <span>{{name}}</span>name
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
      <el-main style="min-width: 1280px">
        <el-row :gutter="20">
          <el-col :span="12">
            <span style="font-size: 18px">标题：{{this.courseInfo}}</span>
          </el-col>
          <el-col :span="2">
            <el-button v-if="collected" type="primary" size="small" @click="cancelCollectionCoursePost">取消收藏</el-button>
            <el-button v-else type="primary" size="small" @click="addCollectionCoursePost">收藏</el-button>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="14">
            <div id="video_wrap">
              <video id="remote_video"></video>
            </div>
          </el-col>
          <div class="message_list">
              <ul class="im-log-box">
                <li v-for="msg in msgs" :key="msg.index">
                  <span class="send">{{msg.send}}</span>
                  <span class="content">{{msg.content}}</span>
                </li>
              </ul>
              <el-input type="textarea" :rows="2" style="width: 70%" placeholder="请输入内容" v-model="imMsg.common.data" ></el-input>
              <el-button type="primary" size="small" @click="sendMsg" style="margin-top: 5px; display: block">发送消息</el-button>
          </div>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="14"><div id="paint_box"></div></el-col>
        </el-row>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
// import {TEST_ACCOUNT} from '../utils/account_dev'
export default {
  name: 'Login6',
  data () {
    return {
      image: this.$store.state.image,
      name: this.$store.state.name,
      isCollapse: true,
      sdkAppId: this.$store.state.appId, // TEST_ACCOUNT.sdkappid, // this.$store.state.appId,
      // users: TEST_ACCOUNT.users,
      account: this.$store.state.sdkId, // TEST_ACCOUNT.users[1]['userId'], // localStorage.getItem('IIC_USERID') ||
      sdkToken: this.$store.state.sdkToken, // 对应sdkId的Token
      isTeacher: 0,
      // userID: '2222' || sessionStorage.getItem('IIC_USERNAME'),
      // nickName: 'n2222' || sessionStorage.getItem('IIC_NICKNAME'),
      roomID: '11599' + this.$store.state.currentTeacher + '95' + this.$store.state.currentCourse, // 12345432,
      collected: this.$store.state.currentCourseMsg.collected,
      courseInfo: this.$store.state.currentCourseMsg.info,
      // roomInfo: '',
      userSig: '',
      // isPushing: 1, // 是否正在推流
      // isPushCamera: 1, // 是否推摄像头流
      imMsg: {
        common: {},
        custom: {}
      },
      msgs: [] // 消息列表
      // remoteVideos: {}
    }
  },
  methods: {
    // 创建或者进入课堂
    start () {
      console.log(this.roomID)
      if (!this.roomID) {
        this.$notify.error({
          title: '错误',
          message: '房间号不能为空'
        })
        return
      }
      // this.step = 'second'
      this.init() // 使用自定义的init
    },

    // 初始化业务数据
    initData () {
      this.msgs = [] // 信息列表
      // this.isPushing = 0 // 是否正在推流
      // this.isPushCamera = 0 // 是否推摄像头流

      // this.devices = { // 清空设备记录
      //   camera: [], // 摄像头
      //   mic: [] // 录音设备
      // }
      // this.cameraIndex = 0 // 选择的摄像头
      // this.micIndex = 0 // 选择的录音设备
      this.imMsg = { // 初始化发送设置
        common: {
          data: '',
          toUser: ''
        },
        custom: {
          data: '',
          toUser: ''
        }
      }
      // localStorage.setItem('IIC_USERID', this.account) // 缓存
    },

    // 初始化SDK
    init () {
      this.initData()
      this.tic = null
      // eslint-disable-next-line
      this.tic = new TIC({})
      this.tic.init(this.sdkAppId, res => { // 使用SDK中的init
        if (res.code) {
          this.$notify.error({
            title: '错误',
            message: '初始化失败，code:' + res.code + ' msg:' + res.info
          })
        } else {
          this.login() // 使用自定义的login
        }
      })
    },

    // 登录
    login () {
      this.tic.login({ // 使用SDK中的login
        userId: this.account,
        userSig: this.sdkToken // this.findUserSig(this.account) // 找Token
      }, (res) => {
        if (res.code) {
          this.$notify.error({
            title: '错误',
            message: '登录失败'
          })
          console.error(res)
        } else {
          this.$notify.success({
            title: '成功',
            message: '登录成功'
          })
          // 增加事件监听
          this.addTICMessageListener() // 自定义的事件监听添加方法
          this.addTICEventListener()
          this.addTICStatusListener()

          this.joinClassroom() // 自定义的joinClassroom（加入房间）
        }
      })
    },

    // 登出
    logout () {
      this.quitClassroom(res => {
        this.logout_internal()
      })
    },

    logout_internal () {
      this.tic.logout((res) => {
        if (res.code) {
          this.$notify.error({
            title: '错误',
            message: '登出失败'
          })
          console.error(res)
        } else {
          // this.initData()
          this.status = this.STATUS_UNLOGIN
          this.$notify.success({
            title: '成功',
            message: '登出成功'
          })
          // 删除事件监听
          this.tic.removeTICMessageListener()
          this.tic.removeTICEventListener()
          this.tic.removeTICStatusListener()
        }
      })
    },

    // 进入房间（学生使用）
    joinClassroom () {
      this.roomID && this.tic.joinClassroom(this.roomID, {}, {
        id: 'paint_box', // 指定html页面中的白板标签
        boardContentFitMode: 0, // 白板的model
        drawEnable: false
      }, res => {
        if (res.code) {
          this.$notify.error({
            title: '错误',
            message: '加入课堂失败'
          })
          console.error(res)
          // this.step = 'first' // 失败返回第一页面状态（没用）
        } else {
          this.$notify.success({
            title: '成功',
            message: '加入课堂成功'
          })
          // 不明白
          window.teduBoard = this.teduBoard = this.tic.getBoardInstance() // 获取白板实例以便设置事件监听
          this.initBoardEvent()

          window.TRTC = this.TRTC = this.tic.getWebRTCInstance() // 获取TRTC实例以便设置事件
          this.initTRTCEvent()

          // // 如果是主动推流
          // if (this.pushModel === 1) {
          //   this.startRTC()
          // }
        }
      })
    },

    // 监听白板事件（按需监听）
    initBoardEvent () {
      var teduBoard = this.teduBoard
      // 撤销状态改变
      // eslint-disable-next-line
      teduBoard.on(TEduBoard.EVENT.TEB_OPERATE_CANUNDO_STATUS_CHANGED, (enable) => {
        console.log('======================:  ', 'TEB_OPERATE_CANUNDO_STATUS_CHANGED', enable ? '可撤销' : '不可撤销')
      })

      // 重做状态改变
      // eslint-disable-next-line
      teduBoard.on(TEduBoard.EVENT.TEB_OPERATE_CANREDO_STATUS_CHANGED, (enable) => {
        console.log('======================:  ', 'TEB_OPERATE_CANREDO_STATUS_CHANGED', enable ? '可恢复' : '不可恢复')
      })

      // 新增白板
      // teduBoard.on(TEduBoard.EVENT.TEB_ADDBOARD, (boardId, fid) => {
      //   console.log('======================:  ', 'TEB_ADDBOARD', ' boardId:', boardId, ' fid:', fid)
      //   this.proBoardData()
      // })

      // 白板同步数据回调(收到该回调时需要将回调数据通过信令通道发送给房间内其他人，
      // 接受者收到后调用AddSyncData接口将数据添加到白板以实现数据同步)
      // TIC已经处理好了，可忽略该事件
      // eslint-disable-next-line
      teduBoard.on(TEduBoard.EVENT.TEB_SYNCDATA, (data) => {
        console.log('======================:  ', 'TEB_SYNCDATA')
      })

      // 收到白板初始化完成事件后，表示白板已处于正常工作状态（此时白板为空白白板，历史数据尚未拉取完成）
      // eslint-disable-next-line
      teduBoard.on(TEduBoard.EVENT.TEB_INIT, () => {
        console.log('======================:  ', 'TEB_INIT')
      })

      // eslint-disable-next-line
      teduBoard.on(TEduBoard.EVENT.TEB_HISTROYDATA_SYNCCOMPLETED, () => {
        console.log('======================:  ', 'TEB_HISTROYDATA_SYNCCOMPLETED')
      })

      // 白板错误回调
      // eslint-disable-next-line
      teduBoard.on(TEduBoard.EVENT.TEB_ERROR, (code, msg) => {
        console.log('======================:  ', 'TEB_ERROR', ' code:', code, ' msg:', msg)
      })

      // 白板警告回调
      // eslint-disable-next-line
      teduBoard.on(TEduBoard.EVENT.TEB_WARNING, (code, msg) => {
        console.log('======================:  ', 'TEB_WARNING', ' code:', code, ' msg:', msg)
      })

      // 图片状态加载回调
      // teduBoard.on(TEduBoard.EVENT.TEB_IMAGE_STATUS_CHANGED, (status, data) => {
      //   console.log('======================:  ', 'TEB_IMAGE_STATUS_CHANGED', ' status:', status, ' data:', data)
      // })

      // 删除白板页回调
      // teduBoard.on(TEduBoard.EVENT.TEB_DELETEBOARD, (boardId, fid) => {
      //   console.log('======================:  ', 'TEB_DELETEBOARD', ' boardId:', boardId, ' fid:', fid)
      //   this.proBoardData()
      // })

      // 跳转白板页回调
      // teduBoard.on(TEduBoard.EVENT.TEB_GOTOBOARD, (boardId, fid) => {
      //   console.log('======================:  ', 'TEB_GOTOBOARD', ' boardId:', boardId, ' fid:', fid)
      //   this.proBoardData()
      // })

      // 增加H5动画PPT文件回调
      // teduBoard.on(TEduBoard.EVENT.TEB_ADDH5PPTFILE, (fid) => {
      //   console.log('======================:  ', 'TEB_ADDH5PPTFILE', ' fid:', fid)
      //   this.proBoardData()
      // })

      // 增加文件回调
      // teduBoard.on(TEduBoard.EVENT.TEB_ADDFILE, (fid) => {
      //   console.log('======================:  ', 'TEB_ADDFILE', ' fid:', fid)
      //   this.proBoardData()
      // })

      // 删除文件回调
      // teduBoard.on(TEduBoard.EVENT.TEB_DELETEFILE, (fid) => {
      //   console.log('======================:  ', 'TEB_DELETEFILE', ' fid:', fid)
      //   this.proBoardData()
      // })

      // 文件上传状态
      // teduBoard.on(TEduBoard.EVENT.TEB_FILEUPLOADSTATUS, (status, data) => {
      //   console.log('======================:  ', 'TEB_FILEUPLOADSTATUS', status, data)
      //   if (status === 1) {
      //     this.showTip('上传成功')
      //   } else {
      //     this.showTip('上传失败')
      //   }
      //   document.getElementById('file_input').value = ''
      // })

      // 切换文件回调
      // teduBoard.on(TEduBoard.EVENT.TEB_SWITCHFILE, (fid) => {
      //   console.log('======================:  ', 'TEB_SWITCHFILE', ' fid:', fid)
      //   this.proBoardData()
      // })

      // 上传背景图片的回调
      // teduBoard.on(TEduBoard.EVENT.TEB_SETBACKGROUNDIMAGE, (fileName, fileUrl, userData) => {
      //   console.log('======================:  ', 'TEB_SETBACKGROUNDIMAGE', '  fileName:', fileName, '  fileUrl:', fileUrl, ' userData:', userData)
      // })

      // 文件上传进度
      // teduBoard.on(TEduBoard.EVENT.TEB_FILEUPLOADPROGRESS, (data) => {
      //   console.log('======================:  ', 'TEB_FILEUPLOADPROGRESS:: ', data)
      //   this.showTip('上传进度:' + parseInt(data.percent * 100) + '%')
      // })

      // H5背景加载状态
      // teduBoard.on(TEduBoard.EVENT.TEB_H5BACKGROUND_STATUS_CHANGED, (status, data) => {
      //   console.log('======================:  ', 'TEB_H5BACKGROUND_STATUS_CHANGED:: status:', status, '  data:', data)
      // })
    },

    // TRTC事件
    initTRTCEvent () {
      // 这个事件应该只在直播方触发（重要）- 学生端没有触发
      this.TRTC.on('onLocalStreamAdd', (data) => { // 本地视频流新增/更新（开自己的直播流）
        if (data && data.stream) {
          var localVideoEl = document.getElementById('local_video') // 看看页面有没有local_video
          if (!localVideoEl) { // 没有local_video，就创建一个
            localVideoEl = document.createElement('video')
            localVideoEl.id = 'local_video'
            // localVideoEl.class = 'col-md-1' // 这是bootStrap.css的样式（不用）
            document.querySelector('#video_wrap').insertBefore(localVideoEl, null) // 插入控件(保证在最前端)
          }
          localVideoEl.muted = true // 设置标签video的属性
          localVideoEl.autoplay = true
          localVideoEl.playsinline = true
          localVideoEl.srcObject = data.stream
          // this.isPushing = 1 // 正在推流 修改推流状态（这页面没有写）
          this.$notify.success({
            title: '成功',
            message: 'WebRTC接收到本地流'
          })
        }
      })
      // 这个事件应该只在观看方触发（重要）
      this.TRTC.on('onRemoteStreamUpdate', (data) => { // 远端视频流新增/更新（有其它人开直播流）
        // var userVideoEl = document.getElementById(data.videoId) // videoId是直播流的ID
        // if (!userVideoEl) { // 没有就创建一个
        //   userVideoEl = document.createElement('video')
        //   userVideoEl.id = data.videoId
        //   // userVideoEl.class = 'col-md-1' // 这是bootStrap.css的样式（不用）
        //   document.querySelector('#video_wrap').appendChild(userVideoEl)
        // }
        var userVideoEl = document.getElementById('remote_video')
        userVideoEl.autoplay = true // 设置标签video的属性
        userVideoEl.playsinline = true
        userVideoEl.srcObject = data.stream
        this.$notify.success({
          title: '成功',
          message: 'WebRTC接收到远端流'
        })
      })

      this.TRTC.on('onRemoteStreamRemove', (data) => { // 远端视频流断开（讲师关播）
        var userVideoEl = document.getElementById(data.videoId)
        if (userVideoEl) {
          userVideoEl.remove() // 移除video控件
        }
        this.$notify.info({
          title: '提示',
          message: 'WebRTC 远端流断开'
        })
      })

      this.TRTC.on('onWebSocketClose', (data) => { // websocket 断开（连接意外断开）
        this.$notify.error({
          title: '错误',
          message: 'WebRTC WebSocket 断开'
        })
      })

      this.TRTC.on('onRelayTimeout', (data) => { // 视频流 server 超时断开（连接意外断开）
        this.$notify.error({
          title: '错误',
          message: 'WebRTC 超时'
        })
      })

      this.TRTC.on('onStreamNotify', (data) => { // TRTC文档都没有写的事件
        console.log('==================== onStreamNotify==', data)
      })
    },

    /**
     * 退出课堂
     */
    quitClassroom () { // 自定义的退出课堂方法
      this.tic.quitClassroom(res => { // TICSDK的退出课堂
        if (res.code) {
          this.$notify.error({
            title: '错误',
            message: '退出课堂失败'
          })
          console.error(res)
        } else {
          // this.step = 'first' // 返回第一状态（没用）
          this.$notify.success({
            title: '成功',
            message: '退出课堂成功'
          })
        }
      })
    },

    /**
     * 销毁课堂
     */
    destroyClassroom () { // 自定义的销毁课堂方法
      this.tic.destroyClassroom(this.roomID, res => { // TICSDK的销毁课堂 // （应该只用课堂创建人才可以销毁）
        if (res.code) {
          this.$notify.error({
            title: '错误',
            message: '销毁课堂失败'
          })
          // this.showErrorTip('销毁课堂失败')
          console.error(res)
        } else {
          this.step = 'first'
          this.$notify.success({
            title: '成功',
            message: '销毁课堂成功'
          })
          // this.showTip('销毁课堂成功')
        }
      })
    },

    /**
     * 增加IM消息监听回调
     */
    addTICMessageListener () { // 被自定义login调用
      this.tic.addTICMessageListener({ // 重要的IM监听

        // /**
        //  * 收到C2C文本消息
        //  * @param fromUserId 发送此消息的用户id
        //  * @param text 收到消息的内容
        //  * @param textLen 收到消息的长度
        //  */
        // onTICRecvTextMessage: (fromUserId, text, textLen) => { // 不使用
        //   this.msgs.push({ // msgs是页面data中的一个字符冲数组与消息控件数据绑定
        //     send: fromUserId + '：',
        //     content: text
        //   })
        // },
        //
        // /**
        //  * 收到C2C自定义消息
        //  * @param fromUserId 发送此消息的用户id
        //  * @param data 收到消息的内容
        //  * @param dataLen 收到消息的长度
        //  */
        // onTICRecvCustomMessage: (fromUserId, data, textLen) => { // 不使用
        //   this.msgs.push({
        //     send: fromUserId + '：',
        //     content: data
        //   })
        // },

        /**
         * 收到群文本消息
         * @param fromUserId 发送此消息的用户id
         * @param text 收到消息的内容
         * @param textLen 收到消息的长度
         */
        onTICRecvGroupTextMessage: (fromUserId, text, textLen) => { // 暂时只使用这个
          // this.msgs.push({
          //   send: fromUserId + '：',
          //   content: text
          // })
          this.msgs.push({
            send: '',
            content: text
          })
        }

        // /**
        //  * 收到群自定义消息
        //  * @param fromUserId 发送此消息的用户id
        //  * @param data 收到消息的内容
        //  * @param dataLen 收到消息的长度
        //  */
        // onTICRecvGroupCustomMessage: (fromUserId, data, textLen) => { // 暂时不使用
        //   this.msgs.push({
        //     send: fromUserId + '：',
        //     content: data
        //   })
        // },
        //
        // /**
        //  * 所有消息
        //  * @param msg // 消息体
        //  * @note 所有收到的消息都会在此回调进行通知，包括前面已经封装的文本和自定义消息（白板信令消息除外）
        //  */
        // onTICRecvMessage (msg) { // 响应全部消息事件的监听方法（不使用）
        //
        // }
      })
    },

    // 事件监听回调
    addTICEventListener () { // 被自定义login调用
      this.tic.addTICEventListener({ // TIC事件监听
        onTICMemberJoin: (members) => { // 在消息列表中提示有人加入课堂
          this.msgs.push({
            send: '群消息提示：',
            content: members.join(',') + '进入课堂'
          })
        },

        onTICMemberQuit: (members) => { // 在消息列表中提示有人退出课堂
          this.msgs.push({
            send: '群消息提示：',
            content: members.join(',') + '退出课堂'
          })
        },

        onTICClassroomDestroy: () => { // 提示销毁课堂
          // if (!this.isTeacher) { // 对学生处理
          //   this.quitClassroom()
          //   this.$notify.info({
          //     title: '提示',
          //     message: '老师解散了课堂'
          //   })
          //   // this.showTip(`老师解散了课堂`)
          // }
          this.quitClassroom()
          this.$notify.info({
            title: '提示',
            message: '老师解散了课堂'
          })
        }
      })
    },

    // IM状态监听回调
    addTICStatusListener () { // 被自定义login调用
      this.tic.addTICStatusListener({ // TIC状态监听
        onTICForceOffline: () => {
          alert(`其他地方登录，被T了`)
          // this.step = 'first';
        }
      })
    },

    /**
     * 发送普通文本消息
     */
    sendMsg () { // 发送消息（重要）
      if (!this.imMsg.common.data) {
        this.$notify.info({
          title: '提示',
          message: `不能发送空消息`
        })
        // this.showErrorTip(`不能发送空消息`)
      }

      // C2C 文本
      if (this.imMsg.common.toUser) { // 有特指对象的话，没有的 TIC的sendTextMessage
        this.tic.sendTextMessage(this.imMsg.common.toUser, this.imMsg.common.data, function (res) {
          console.log('===sendTextMessage:', res)
        })
      } else { // 群组 文本
        // this.tic.sendGroupTextMessage(this.imMsg.common.data, function (res) { // TIC的sendGroupTextMessage
        //   console.log('===sendTextMessage:', res)
        // })
        let _this = this
        this.tic.sendGroupTextMessage(this.$store.state.name + ': ' + this.imMsg.common.data, function (res) {
          console.log('===sendTextMessage:', res)
          _this.saveChatRecordPost() // 把消息Post到后台
        })
      }
    },

    // /**
    //  * 发送自定义消息
    //  */
    // sendCustomGroupMsg () { // 自定义消息，我没写在html中，没被使用
    //   //
    //   if (this.imMsg.common.toUser) { // C2C 自定义 // TIC的sendCustomMessage
    //     this.tic.sendCustomMessage(this.imMsg.common.toUser, this.imMsg.custom.data, function (res) {
    //       console.log('===sendCustomGroupMsg:', res)
    //     })
    //   } else { // 群组 自定义
    //     this.tic.sendGroupCustomMessage(this.imMsg.custom.data, function (res) { // TIC的sendGroupCustomMessage
    //       console.log('===sendCustomGroupMsg:', res)
    //     })
    //   }
    // },

    /**
     * 设置涂鸦类型
     * @param {*} type
     */
    setType (type) { // 使用到了，画笔和橡皮的切换
      this.teduBoard.setToolType(type)
    },

    /**
     * 清空当前页涂鸦(保留背景色/图片)
     */
    clearDraws () { // 使用到了，清空
      this.teduBoard.clear()
    },

    /**
     * 回退
     */
    revert () { // 使用了 回退
      this.teduBoard.undo()
    },

    /**
     * 恢复
     */
    process () { // 使用了 恢复
      this.teduBoard.redo()
    },

    // /**
    //  * 获取userSig
    //  */
    // findUserSig () { // 重要，使用到了
    //   var userSig = null
    //   for (var i = 0, len = this.users.length; i < len; i++) {
    //     if (this.account === this.users[i].userId) {
    //       userSig = this.users[i].userToken // 找到Token
    //       break
    //     }
    //   }
    //   return userSig // 返回Token
    // },
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
    addCourseHistoryPost () {
      let notify = this.$notify
      let store = this.$store
      let _this = this
      this.$axios.post('/historyCourse/addHistoryCourse', {
        'courseId': store.state.currentCourse
      })
        .then(function (res) {
          console.log(res)
          // Post成功，添加课程记录成功
          if (res.data.successful) {
            return true
          } else {
            // Post成功，但添加课程记录失败，返回相应的错误信息
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
    saveChatRecordPost () {
      let notify = this.$notify
      let store = this.$store
      let _this = this
      this.$axios.post('/chatRecord/saveChatRecord', {
        'content': _this.imMsg.common.data,
        'courseId': store.state.currentCourse
      })
        .then(function (res) {
          console.log(res)
          // Post成功，添加消息记录成功
          if (res.data.successful) {
            return true
          } else {
            // Post成功，但添加消息记录失败，返回相应的错误信息
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
    addCollectionCoursePost () {
      let notify = this.$notify
      let store = this.$store
      let _this = this
      this.$axios.post('/collectionCourse/addCollectionCourse', {
        'courseId': store.state.currentCourse
      })
        .then(function (res) {
          console.log(res)
          // Post成功，收藏课程成功
          if (res.data.successful) {
            notify.success(
              {
                message: '收藏课程成功'
              })
            _this.collected = true
            return true
          } else {
            // Post成功，但收藏课程失败，返回相应的错误信息
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
    cancelCollectionCoursePost () {
      let notify = this.$notify
      let store = this.$store
      let _this = this
      this.$axios.post('/collectionCourse/cancelCollectionCourse', {
        'courseId': store.state.currentCourse
      })
        .then(function (res) {
          console.log(res)
          // Post成功，取消收藏成功
          if (res.data.successful) {
            notify.success(
              {
                message: '取消收藏成功'
              })
            _this.collected = false
            return true
          } else {
            // Post成功，但取消收藏失败，返回相应的错误信息
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
  // 进入页面后，自动执行（TIC的初始化）
  mounted () {
    this.addCourseHistoryPost()
    this.start()
  },
  // 在页面销毁前会调用这个方法（生命周期）
  beforeDestroy () { // 保证在离开页面前会调用销毁课堂的方法
    this.logout()
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
.el-avatar{
  margin-top: 8px;
  margin-left: 8px;
  float: right;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
#paint_box{
  /*position: relative;*/
  /*width: 650px;*/
  /*height: 500px;*/
  /*border: 1px solid #888;*/
  /*overflow: hidden;*/
  position: relative;
  width: 812px;
  height: 458px;
  border: 1px solid #888;
  overflow: hidden;
}
.im-log-box {
  display: inline-block;
  width: 60%;
  height: 300px;
  border: 1px solid #999;
  margin: 5px;
  overflow-y: auto;
}
.im-log-box li {
  margin: 2px 5px;
  font-size: 14px;
  color: #666;
}
#video_wrap {
  position: relative;
  width: 100%;
  padding: 56.25% 0px 0px;
  background-color: black;
}
video {
  /*max-width: 640px;*/
  /*max-height: 480px;*/
}
#video_wrap video {
  position: absolute;
  left: 0px;
  top: 0px;
  width: 100%;
  height: 100%;
  min-width: 160px;
  min-height: 90px;
}
.message_list{
  position: absolute;
  top: 0px;
  right: 0px;
  width: 340px;
  height: 100px;
  margin-right: 50px;
}
</style>
