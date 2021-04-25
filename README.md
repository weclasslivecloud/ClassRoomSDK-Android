# Android LiveUI SDK接入文档


## 一、功能简介

直播云安卓LiveUISDK在LiveCoreSDK基础上提供了教室功能，包括一对一教室、小班教室、大班教室以及课程回放功能。涵盖一对一及多人音视频互动、课件教学、多功能涂鸦绘制、文字聊天、多种教学工具等功能。在此文档中，包含一套完整的LiveUI SDK接入说明。



## 二、设备要求

- **Android Minsdk 21**
- **安卓平板、安卓手机**



## 三、接入步骤

### 1.在项目根目录的build.gradle文件里添加

```
allprojects {
    repositories {
    	...
    	jcenter { url 'https://dl.bintray.com/livecloud/NewZhiboyun'}
        maven {
            url "https://jitpack.io"
        }
        ...
    }
}
```

### 2.在App⽬录下的 build.gradle ⽂件⾥**添加依赖**（最新版本更新中）​

```
dependencies {
    implementation 'com.weclassroom.liveui:liveui:0.3.9'
}
```



## 四、如何使用

### 1.初始化

在Application实现类中调用初始化initSDK方法，建议添加到  Oncreate()方法中 。

```
LiveRoomManager.init(Context context, URL.ENVIRONMENT_VARIABLE env)
```

​	**参数含义：**

​	**URL.ENVIRONMENT_VARIABLEU(确定环境参数)：**

​		URL.ENVIRONMENT_VARIABLE.ONLINE：正式环境，上线使用；
		URL.ENVIRONMENT_VARIABLE.TEST：测试环境，开发测试时使用。

### 2.创建信息并进入教室

```
  WcrClassJoinInfo joinInfo = new WcrClassJoinInfo();
                WcrClassJoinInfo.ClassInfo classInfo = WcrClassJoinInfo.ClassInfo();
                WcrClassJoinInfo.User user = new WcrClassJoinInfo.User();
                //课程预计开始时间(必填项)
                classInfo.setSchedualStartTime("2019-05-15 11:45:00");
                //课程预计结束时间(必填项)
                classInfo.setSchedualEndTime("2019-05-15 23:40:00");
                //老师头像
                classInfo.setTeacherAvatar(teacherAvatarUrl);
                //老师id(必填项)
                classInfo.setTeacherID(teacherid);
                //老师name
                classInfo.setTeacherName(teacherName);
                //课程状态(必填项)
                classInfo.setClassState(ClassStatus.CLASS_ONGOING);
                //课程id(必填项)
                classInfo.setClassID(classId);
                //课程类型(必填项)
                classInfo.setClasstype(0);
                //课程持续时长
                classInfo.setDuration(0);
                classInfo.setCourseId(courseId);
                //课程标题
                classInfo.setClassTitle(courseTitle);
                //机构id(必填项)
                classInfo.setInstitutionID(institutionID);
                //用户token(必填项)
                user.setUserToken(token);
                //用户id(必填项)，为整型，为了方便使用，以字符串方式入参，如：“202027”等
                user.setUserId(userID);
                //用户头像
                user.setAvatar(avatarUrl);
                //用户角色
                user.setUserRole("student");
                //用户昵称
                user.setUserName(mStudentName);
                //手机号(大班课特有参数)
                user.setPhoneNumber("");
                //课程等待时H5页面（一对一、小班课、小组课特有参数)                 			  
                classInfo.setWaitingDocument("");
                //设置是否显示水印，LOGO_WATERMARK_GONE或 0为隐藏，LOGO_WATERMARK_SHOW或1为显示
                classInfo.setWatermarkShow(watermarkShow)；
                //设置大班是否显示logo，LOGO_WATERMARK_GONE或0为隐藏，LOGO_WATERMARK_SHOW或1为显示
                classInfo.setLogoShow(logoShow)；
                //设置大班课Logo图片（目前只支持本地图片）
                classInfo.setRoomLogoResource(roomLogoResource)；
                joinInfo.setUser(user);
                joinInfo.setClassInfo(classInfo);
 JoinRoomHelper.joinRoom(Context, joinInfo)
```
​	**特殊参数含义：**	

​	**ClassState(课程状态)：**
```
​		
CLASS_PREPARE：课前状态(进入直播课堂)；

​CLASS_ONGOING：课中开课状态(进入直播课堂)；

CLASS_OVER：课程结束状态(进入回放页)；

CLASS_EXPIRE：课程到期状态（暂未使用）
```
​	**ClassType(课程类型):**  
```
​0=一对一模式;

1=普通大班课 口令方式;

2=普通大班课 登录的方式;

5=互动大班课 密码方式;

6=互动大班课 口令方式;

7=小班课。

9=小组课。
````
### 3.监听直播课程状态。

​	如果需要监听课程状态，请在监听页面实现 ***com.weclassroom.liveui.listener.OnGlobalNotifier*** 接口，并实现方法 ***void onGlobalNotify(TNotifyType type, ExitRoomInfo info)*** 。

​	**参数 TNotifyType type：**
```
​JOIN_ROOM_SUCCESS(进房成功)；

JOIN_ROOM_FAILD(进房失败)；

EXIT_ROOM(退出教室)；

STATE_CLASS_START(开始上课)；

STATE_CLASS_END(下课消息)；

**ExitRoomInfo info：** 返回课程的一些相关信息。
```


## 五、权限相关

* 使用到的运行时权限如下，该权限已在教室内动态申请，请提示用户同意权限，否则会导致部分功能无法正常使用。
  `Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO`



## 六、混淆相关

​	待补充



## 七、附录

### 附1

#### `AndroidX`

 **注意：** 目前直播云新架构使用的是 `Android` 提供的 `AndroidX` 依赖库，暂时不支持和之前的 `support` 库共存，如果发生编译时错误请参考[官方文档](https://developer.android.com/jetpack/androidx) [Migrating to AndroidX](https://developer.android.com/jetpack/androidx/migrate) 进行迁移。



### 附2

**刚结束的直播课程无法观看该课程的回放视频，视频流转为可观看的回放视频需要时间。**



### 附3

**常见问题请参考常见问题文档**

​	​
