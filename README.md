# PCRBot
PCR 公主连接国服 Bigfun工会战信息查询  
本项目直接基于[mirai-core](https://github.com/mamoe/mirai)开发
目前已经适配新版五个boss一起打的新版会战
bigfun部分接口返回有错误，查刀，剩余刀结果不准确


# 功能&命令
### 以下命令所有成员都能使用
```
状态：打印当前boss信息
查刀：列出所有未出满三刀的成员
排名：查看昨日凌晨五点的工会排名
尾刀 剩余刀：列出所有目前被卡剩余刀的成员
报告 [玩家名]：搜索公会成员并列出当日的出刀记录
查分 [公会名]：搜索公会名并列出公会基本信息
查线 [排名]：精确查找指定排名的公会基本信息
```
### 以下命令需要管理员或开发者才能使用  
```
/cookie [cookie]：设置新的bigfun的cookie，只需要session-api=xxxxxxxx
/config ：从文件中重新载入配置，机器人不会重新登陆
```


# 预览
|       |  |  |
| ----------- | ----------- | ------ |
| ![1dad280ddf04890e](https://user-images.githubusercontent.com/26589867/192179111-c6669c8b-06a6-40db-80ec-c737292cac18.png)      | ![-4c53f24d32c55036](https://user-images.githubusercontent.com/26589867/192179115-a1f3e48a-b882-4f39-9eb5-7b8a90ac345e.png)       | ![20e19bafe3b35dcd](https://user-images.githubusercontent.com/26589867/192179117-29192603-2b61-4ba0-afa0-4481dcf5c6b1.png)  |
| ![-29dea4ba56d186f0](https://user-images.githubusercontent.com/26589867/192179120-80954f29-b206-4d58-ae7e-411edb265fc4.png)   | ![-62ecf1ce5aff1b28](https://user-images.githubusercontent.com/26589867/192179123-62aa3df7-e1ae-4f14-b4f2-0b583dab8759.png)        | ![-3004519ccf068432](https://user-images.githubusercontent.com/26589867/192179124-89b92387-415c-494a-be69-9e767833a626.png) |


# 配置说明
config.txt配置文件  
```
#QQ配置文件  
qq.id = qq号  
qq.pwd = qq密码  
qq.owner = 开发者qq  
qq.group = 工会战群  
qq.protocol = MACOS
cookie = bingfun网页的cookie
```

环境需求java11及以上，启动命令  
正常情况下使用  
```java -jar PCRBot-1.0.0.jar```   
如果出现乱码或者时间错误，请使用  
```java -jar -Dfile.encoding=UTF-8 -Duser.timezone=GMT+8 PCRBot-1.0.0.jar```

