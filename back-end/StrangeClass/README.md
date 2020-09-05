# 奇怪课堂

#### 介绍
这里是StrangeTeam团队的<奇怪课堂-在线直播课堂>的服务器端

#### 项目技术框架
Spring Boot + MySQL

#### 项目目录介绍
/sql                                              //数据库数据文件
/src                                              //项目代码
/src/main/java/com/example/strangeclass           //项目核心代码
/src/main/java/com/example/strangeclass/config    //Web服务端拦截器等设置
/src/main/java/com/example/strangeclass/controller//控制层
/src/main/java/com/example/strangeclass/dao       //持久层
/src/main/java/com/example/strangeclass/dto       //dto层
/src/main/java/com/example/strangeclass/entity    //实体类
/src/main/java/com/example/strangeclass/service   //业务层
/src/main/java/com/example/strangeclass/util      //工具包
/src/main/java/com/example/strangeclass/vo        //各种View Object
/src/main/resource    资源配置文件

#### 启动步骤

1. 克隆项目
2. 下载pom.xml中依赖
3. 设置JDk
4. 将/src/main/resources/application.properties中的数据库名称和用户密码设置为本机数据库
5. 运行DemoApplication.java启动类