# AndroidUPnPDemo

### 当前使用 cling

>使用方法以及Cling原理介绍可见[我的简书](http://www.jianshu.com/u/511ccb5a2012)

关于 android 投屏技术系列：


一、知识概念

- [android设备投屏技术:协议&概念](http://www.jianshu.com/p/5a260182cc82)
> 这章主要讲一些基本概念， 那些 DLNA 类库都是基于这些概念来做的，了解这些概念能帮助你理清思路，同时可以提升开发效率，遇到问题也能有个解决问题的清晰思路。

二、手机与tv对接

- [android投屏技术:发现设备代码实现](http://www.jianshu.com/p/14cbeb898050) 
-  [android投屏技术:发现设备源码分析](http://www.jianshu.com/p/9e063d84ab9f)

>这部分是通过[Cling DLNA类库](https://github.com/4thline/cling)来实现发现设备的。 
内容包括：
1. 抽出发现设备所需接口
2. 发现设备步骤的实现
3. 原理的分析

三、手机与tv通信

- [android投屏技术:控制设备代码实现](http://www.jianshu.com/p/d0dcfdd0cd6e)
- [android投屏技术:控制设备源码分析](http://www.jianshu.com/p/4452182d2b48)


#### 2017/7/5 更新：已有功能

发现设备
- 发现设备
- 发现设备监听

操作功能：
- 播放
- 暂停
- 停止
- 进度拖拽
- 音量调节
- 设置静音
