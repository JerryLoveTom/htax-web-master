
**项目说明** 

 

**具有如下特点** 
- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，通过token进行数据交互，前端再也不用关注后端技术
- 灵活的权限控制，可控制到页面或按钮，满足绝大部分的权限需求
- 页面交互使用Vue2.x，极大的提高了开发效率
- 完善的代码生成机制，可在线生成entity、xml、dao、service、vue、sql代码，减少70%以上的开发任务
- 引入quartz定时任务，可动态完成任务的添加、修改、删除、暂停、恢复及日志查看等功能
- 引入API模板，根据token作为登录令牌，极大的方便了APP接口开发
- 引入Hibernate Validator校验框架，轻松实现后端校验
- 引入云存储服务，已支持：七牛云、阿里云、腾讯云、本地存储等
- 引入swagger文档支持，方便编写API接口文档
 

**项目结构** 
```
renren-fast
├─db  项目SQL语句
│
├─common 公共模块
│  ├─aspect 系统日志
│  ├─exception 异常处理
│  ├─validator 后台校验
│  └─xss XSS过滤
│ 
├─config 配置信息
│ 
├─modules 功能模块
│  ├─app  API接口模块(APP调用)
│  ├─eams 自定义业务模块
│  ├─job  定时任务模块
│  ├─oss  文件服务模块
│  └─sys  权限模块
│ 
├─HtaxWebApplication 项目启动类
│  
├──resources 
│  ├─mapper SQL对应的XML文件
│  └─static 静态资源

```
 


**技术选型：** 
- 核心框架：Spring Boot 2.6.6

- 安全框架：Apache Shiro 1.9 

- 持久层框架：MyBatis-plus 3.3.1

- 定时器：Quartz 2.3

- 数据库连接池：Druid 1.1.13

- 日志管理：SLF4J 1.7、Log4j

- 页面交互：Vue2.x

- EXCEL解析： easyexcel 2.1.1

- 全文检索：elasticsearch 7.15.2

- 接口文档：swagger2.7

   


 **后端部署**
- idea、eclipse需安装lombok插件，不然会提示找不到entity的get set方法

- 创建数据库htax_armedpolice ，数据库编码为UTF-8

- 执行db/htax_armedpolice.sql 文件，初始化数据，暂时是mysql，之后通过DM迁移工具进行迁移即可。

- 修改数据中sys_menu表的SQL监控地址，改为服务器所在IP地址即可

- 修改application-dev.yml，更新MySQL账号和密码

- Eclipse、IDEA运行HtaxWebApplication.java，则可启动项目

- Swagger文档路径：http://localhost:8803/htax-fast/swagger/index.html

- Swagger注解路径：http://localhost:8803/htax-fast/swagger-ui.html

  

业务流程图
 
<img width="1722" alt="image" src="https://user-images.githubusercontent.com/20388507/182788802-cb68bcb9-de00-4b49-a939-07c408051a05.png">


