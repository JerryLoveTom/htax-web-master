
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

  

# 武警项目后端已完成功能记录 

## 1.首页

- 我方单位树
- 敌方单位树
- 战场单位树
- 人员总数、装备总数的编制树和现有数的统计
- 统计图：
  - 通信人员：编制统计（饼状图）、年龄统计（漏斗图）、学历统计（横向柱状图）
  - 装备情况：**暂缓，表结构暂无设计**
  - 天气信息：全部数据以JSON格式数据进行存储，1地区1年只存1调数据，包含平均气温、降雨量、风速情况
    - 已完成：降雨量及数据格式的封装
    - **未完成**：平均气温、风速情况，由于echarts图暂无法确定呈现形式（主要纠结于是否显示多年对比图）。

## 2.其它

- 部队简况
  - 已完成：主官查询、部队基本信息查询
  - **未完成**：担负任务，由于客户暂未提供任务来源及未来如何使用任务，暂缓开发
- 人员简况
  - 已完成：带分页、带条件的列表查询；人员详情；更多详情；通过excel模板批量导入人员信息；
- 装备简况
  - **未完成**：暂缓开发，表结构暂无设计
- 环境
  - 已完成：根据地区新建、修改、查询与之相关联的地理环境信息、气象水文信息
  - **未完成：**电磁环境，由于客户暂未明确电磁环境具体内容，暂缓开发
- 全文检索
  - 仅完成环境搭建，暂未进行具体使用
- 文件上传
  - 已实现文件上传功能及文件删除功能
    - 文件上传后查看功能，已与前端沟通，所有的静态资源由前端进行管理，前端指定nginx目录，后端进行存储即可。

