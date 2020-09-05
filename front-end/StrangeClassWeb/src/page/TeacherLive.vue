<!--老师直播页面-->
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
      <el-main style="min-width: 1280px">
        <el-row :gutter="20">
          <el-col :span="14">
            <div id="video_wrap">
              <video id="local_video"></video>
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
          <el-col :span="24">
            <el-button-group>
              <el-button @click="startRTC" type="success" size="small">摄像头推流</el-button>
              <el-button @click="stopPush" type="danger" size="small" v-if="isPushing === 1">停止推流</el-button>
            </el-button-group>
            <el-button-group>
              <template v-if="isPushing === 1">
                <el-button @click="toggleCamera" type="primary" size="small">{{enableCamera ? '关闭': '打开'}}摄像头/屏幕分享</el-button>
                <el-button @click="toggleMic" type="primary" size="small">{{enableMic ? '关闭': '打开'}}麦克风</el-button>
              </template>
            </el-button-group>
            <div v-if="isPushing === 1 && isPushCamera">
              <el-button @click="getCameraDevices" type="success" size="small">枚举摄像头</el-button>
              <el-select v-model="cameraIndex" @change="switchCamera">
                <el-option
                  v-for="(camera, index) in devices.camera" :key="index"
                  :label="camera.label" :value="index">
                </el-option>
              </el-select>
              <el-button @click="getMicDevices" type="info" size="small">枚举麦克风</el-button>
              <el-select v-model="micIndex" @change="switchMic">
                <el-option
                  v-for="(mic, index) in devices.mic" :key="index"
                  :label="mic.label" :value="index">
                </el-option>
              </el-select>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-button type="primary" size="small"
                       @click="setType(1)">画笔</el-button>
            <el-button type="primary" size="small"
                       @click="setType(2)">橡皮擦</el-button>
            <el-button type="primary" size="small" @click="clearDraws">清空当前页涂鸦(保留背景)</el-button>
            <el-button type="primary" size="small" @click="revert">撤销</el-button>
            <el-button type="primary" size="small" @click="process">恢复</el-button>
          </el-col>
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
  name: 'Login5',
  data () {
    return {
      // 侧栏使用
      isCollapse: true,

      // 用户信息
      image: this.$store.state.image,
      name: this.$store.state.name,
      sdkAppId: this.$store.state.appId, // TEST_ACCOUNT.sdkappid, // this.$store.state.appId,
      account: this.$store.state.sdkId, // TEST_ACCOUNT.users[4]['userId'], // localStorage.getItem('IIC_USERID') ||
      sdkToken: this.$store.state.sdkToken, // 对应sdkId的Token
      isTeacher: 1,
      userSig: '',

      // 房间信息
      roomID: '11599' + this.$store.state.id + '95' + this.$store.state.currentCourse, // 12345432,
      // roomInfo: '',

      // 音视频及设备
      isPushing: 1, // 是否正在推流（按钮使用）
      isPushCamera: 1, // 是否推摄像头流
      enableCamera: true,
      enableMic: true,
      cameraIndex: 0,
      micIndex: 0,
      devices: {
        camera: ['摄像头1', '摄像头2', '摄像头3', '摄像头4'],
        mic: ['录音1', '录音2', '录音3', '录音4']
      },

      // 消息
      imMsg: {
        common: {},
        custom: {}
      },
      msgs: [], // 消息列表

      destroyHasSwitch: false
      // // 当前用户状态
      // STATUS_UNINIT: 0,
      // STATUS_UNLOGIN: 1,
      // STATUS_LOGINED: 2,
      // STATUS_INCLASS: 3,
      // status: 0
    }
  },
  methods: {
    // init--login--createClassroom--joinClassroom,以及IM、Statue、RTC事件监听
    start () {
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
      this.isPushing = 0 // 是否正在推流
      this.isPushCamera = 0 // 是否推摄像头流

      this.devices = { // 清空设备记录
        camera: [], // 摄像头
        mic: [] // 录音设备
      }
      this.cameraIndex = 0 // 选择的摄像头
      this.micIndex = 0 // 选择的录音设备
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

      this.enableCamera = true
      this.enableMic = true
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
        userSig: this.sdkToken // this.findUserSig(this.account) // 生成签名
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
          this.createClassroom() // 自定义的createClassroom
          // if (this.isTeacher) {
          //   // 老师就创建课堂
          //   this.createClassroom() // 自定义的createClassroom
          // } else { // 如果是学生
          //   // 有了课堂后就直接加入
          //   this.joinClassroom() // 自定义的joinClassroom
          // }
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

    // 创建房间
    createClassroom () {
      this.tic.createClassroom(this.roomID, (res) => { // 使用SDK中的createClassroom
        if (res.code) {
          this.$notify.error({
            title: '错误',
            message: '创建课堂失败'
          })
          console.error(res)
          // this.step = 'first'
        } else {
          this.$notify.success({
            title: '成功',
            message: '创建课堂成功'
          })
          // 如果是老师
          // if (this.isTeacher) {
          //   this.joinClassroom()
          // } else {
          //   this.$notify.error({
          //     title: '错误',
          //     message: '你不是老师'
          //   })
          // }
          this.joinClassroom()
        }
      })
    },

    // 进入房间
    joinClassroom () {
      console.log('Enter Class')
      this.tic.joinClassroom(this.roomID, {}, {
        id: 'paint_box', // 指定html页面中的白板标签
        boardContentFitMode: 0 // 白板的model
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
          window.teduBoard = this.teduBoard = this.tic.getBoardInstance() // 获取白板实例以便设置事件
          this.initBoardEvent()

          window.TRTC = this.TRTC = this.tic.getWebRTCInstance() // 获取TRTC实例以便设置事件
          this.initTRTCEvent()

          // 如果是主动推流
          if (this.pushModel === 1) {
            this.startRTC()
          }
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
      // 这个事件应该只在直播方触发（重要）
      this.TRTC.on('onLocalStreamAdd', (data) => { // 本地视频流新增/更新（开自己的直播流）
        if (data && data.stream) {
          var localVideoEl = document.getElementById('local_video') // 看看页面有没有local_video
          if (!localVideoEl) { // 没有local_video，就创建一个
            localVideoEl = document.createElement('video')
            localVideoEl.id = 'local_video'
            // localVideoEl.class = 'col-md-1' // 这是bootStrap.css的样式（不用）
            document.querySelector('#video_wrap').insertBefore(localVideoEl, null) // 插入控件
          }
          localVideoEl.muted = true // 设置标签video的属性
          localVideoEl.autoplay = true
          localVideoEl.playsinline = true
          localVideoEl.srcObject = data.stream
          this.isPushing = 1 // 正在推流 修改推流状态
          this.$notify.success({
            title: '成功',
            message: 'WebRTC接收到本地流'
          })
        }
      })
      // 这个事件应该只在观看方触发（重要）
      this.TRTC.on('onRemoteStreamUpdate', (data) => { // 远端视频流新增/更新（有其它人开直播流）
        var userVideoEl = document.getElementById(data.videoId) // videoId是直播流的ID
        if (!userVideoEl) { // 没有就创建一个
          userVideoEl = document.createElement('video')
          userVideoEl.id = data.videoId
          // userVideoEl.class = 'col-md-1' // 这是bootStrap.css的样式（不用）
          document.querySelector('#video_wrap').appendChild(userVideoEl)
        }
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
          // this.step = 'first'
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

        /**
         * 收到C2C文本消息
         * @param fromUserId 发送此消息的用户id
         * @param text 收到消息的内容
         * @param textLen 收到消息的长度
         */
        onTICRecvTextMessage: (fromUserId, text, textLen) => { // 不使用
          this.msgs.push({ // msgs是页面data中的一个字符冲数组与消息控件数据绑定
            send: fromUserId + '：',
            content: text
          })
        },

        /**
         * 收到C2C自定义消息
         * @param fromUserId 发送此消息的用户id
         * @param data 收到消息的内容
         * @param dataLen 收到消息的长度
         */
        onTICRecvCustomMessage: (fromUserId, data, textLen) => { // 不使用
          this.msgs.push({
            send: fromUserId + '：',
            content: data
          })
        },

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
        },

        /**
         * 收到群自定义消息
         * @param fromUserId 发送此消息的用户id
         * @param data 收到消息的内容
         * @param dataLen 收到消息的长度
         */
        onTICRecvGroupCustomMessage: (fromUserId, data, textLen) => { // 暂时不使用
          this.msgs.push({
            send: fromUserId + '：',
            content: data
          })
        },

        /**
         * 所有消息
         * @param msg // 消息体
         * @note 所有收到的消息都会在此回调进行通知，包括前面已经封装的文本和自定义消息（白板信令消息除外）
         */
        onTICRecvMessage (msg) { // 响应全部消息事件的监听方法（不使用）

        }
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
          if (!this.isTeacher) { // 对学生处理
            this.quitClassroom()
            this.$notify.info({
              title: '提示',
              message: '老师解散了课堂'
            })
          }
        }
      })
    },

    // IM状态监听回调
    addTICStatusListener () { // 被自定义login调用
      this.tic.addTICStatusListener({ // TIC状态监听
        onTICForceOffline: () => {
          alert(`其他地方登录，被T了`)
        }
      })
    },

    // 自定义启动推流(推摄像头) 重要
    startRTC () {
      // 获取webrtc实例
      var WebRTC = this.TRTC // TRTC实例，在自定义joinClassRoom中赋值给this.TRTC
      WebRTC.getLocalStream({ // 获取本地音频/视频流
        audio: true,
        video: true,
        attributes: {
          width: 848, // 848
          height: 477 // 477
        }
      }, (data) => {
        this.isPushCamera = true // 设置摄像机推流的标志
        if (WebRTC.global.localStream && WebRTC.global.localStream.active) { // 判断本地音频/视频流是否已经开着
          WebRTC.updateStream({ // 更新视频流
            role: 'screen', // 非必须，更新流如果需要更新画面角色设置，需要带上此参数
            stream: data.stream // 预留字段,传空对象
          }, () => {
            // 成功
          }, (error) => {
            this.$notify.error({
              title: '错误',
              message: '更新流失败' + error
            })
          })
        } else {
          WebRTC.startRTC({ // 调用RTC的startRTC创建音频/视频流(主动发起推流/拉流)
            stream: data.stream,
            role: 'user'
          }, (data) => {
            // 成功
          }, (error) => {
            this.$notify.error({
              title: '错误',
              message: '推流失败' + error
            })
          })
        }
      }, (error) => {
        this.$notify.error({
          title: '错误',
          message: '获取本地流失败' + JSON.stringify(error)
        })
      })
    },

    stopPush () { // 自定义停止推流
      var WebRTC = this.tic.getWebRTCInstance() // this.TRTC？？，与startRTC中不一致
      WebRTC.stopRTC({}, () => { // 使用RTC的stopRTC（停止推流）
        this.isPushing = 0 // 修改推流的状体标志
        document.querySelector('#local_video').srcObject = null // 停止对控件的流传输
      })
    },

    /**
     * 摄像头开关
     */
    toggleCamera () { // 使用到了
      this.enableCamera = !this.enableCamera
      var WebRTC = this.tic.getWebRTCInstance() // 还是调用了RTC的openVideo()和closeVideo()
      this.enableCamera ? WebRTC.openVideo() : WebRTC.closeVideo()
    },

    /**
     * 麦克风开关
     */
    toggleMic () { // 使用到了
      this.enableMic = !this.enableMic
      var WebRTC = this.tic.getWebRTCInstance() // 还是调用了RTC的openAudio()和closeAudio()
      this.enableMic ? WebRTC.openAudio() : WebRTC.closeAudio()
    },

    /**
     * 枚举摄像头
     */
    getCameraDevices () { // 使用到了
      var WebRTC = this.tic.getWebRTCInstance()
      WebRTC.getVideoDevices(devices => { // 还是调用了RTC的getVideoDevices()
        this.devices.camera = devices
      })
    },

    /**
     * 切换摄像头
     */
    switchCamera () {
      if (this.cameraIndex < 0) {
        return
      }
      var WebRTC = this.tic.getWebRTCInstance() // 还是调用了RTC的chooseVideoDevice()
      WebRTC.chooseVideoDevice(this.devices.camera[this.cameraIndex]) // cameraIndex
    },

    /**
     * 枚举麦克风
     */
    getMicDevices () {
      var WebRTC = this.tic.getWebRTCInstance()
      WebRTC.getAudioDevices(devices => { // 还是调用了RTC的getAudioDevices()
        this.devices.mic = devices
      })
    },

    /**
     * 切换麦克风
     */
    switchMic () {
      if (this.micIndex < 0) {
        return
      }
      var WebRTC = this.tic.getWebRTCInstance() // 还是调用了RTC的chooseAudioDevice()
      WebRTC.chooseAudioDevice(this.devices.mic[this.micIndex])
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

    // 下面是白板的设置方法

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
     * 清空当前页涂鸦 + 背景色/图片
     */
    clear () {
      this.teduBoard.clear(true)
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

    /**
     * 白板事件回调处理
     * @param {*} data
     */
    proBoardData (data) { // 白板的状态改变，但我只使用一个白板
      this.currentFile = this.teduBoard.getCurrentFile()
      this.boardFileGroup = this.teduBoard.getFileInfoList()
      var currentBoard = this.teduBoard.getCurrentBoard()

      var boards = this.teduBoard.getFileBoardList(this.currentFile)

      this.boardData.page = {
        current: boards.indexOf(currentBoard) + 1,
        total: boards.length
      }
    },

    handleCommand (command) {
      if (command === 'a') {
        // this.quitClassroom() // 退出也要退出直播() // 销毁的时候会调用，这里就不需要
        this.switchLiveStatuePost()
        this.destroyHasSwitch = true
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
          // Post成功，退出成功
          if (res.data.successful) {
            // 清除vuex和sessionStorage中的数据
            store.dispatch('Logout')
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
    switchLiveStatuePost () {
      let notify = this.$notify
      let store = this.$store
      let _this = this
      // console.log(this.roomID)
      // console.log(store.state.currentCourse)
      this.$axios.post('/course/openCourseLive', {
        'classId': parseInt(_this.roomID),
        'id': store.state.currentCourse // 课程ID不是用户ID
      })
        .then(function (res) {
          if (res.data.successful) {
            notify.success(
              {
                message: '直播状态切换成功'
              })
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
    this.start()
    this.switchLiveStatuePost()
  },
  // 在页面销毁前会调用这个方法（生命周期）
  beforeDestroy () { // 保证在离开页面前会调用销毁课堂的方法
    // this.quitClassroom() // 退出
    this.logout() // (退出课堂--登出)
    this.destroyClassroom() // 销毁
    if (!this.destroyHasSwitch) {
      this.switchLiveStatuePost() // 切换直播状态
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
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
.el-avatar{
  margin-top: 8px;
  margin-left: 8px;
  float: right;
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
