## boot3-init

快速生成springboot3简单后端项目，满足基本web开发

<p align=center>
    <a href="http://gitee.com/liang-tian-yu">Spring Boot 3 脚手架</a>
</p>
<p align="center">
<a target="_blank" href="http://gitee.com/liang-tian-yu">
    <img src="https://img.shields.io/badge/JDK-17+-green" ></img>
    <img src="https://img.shields.io/badge/springboot-3.4.5-green" ></img>
    <img src="https://img.shields.io/badge/mysql-5.7-blue" ></img>
    <img src="https://img.shields.io/badge/MybatisPlus-3.5.6-green" ></img>
    <img src="https://img.shields.io/badge/Knife4j-4.4.0-brightgreen" ></img>
</a></p>




内容如下：

- AOP(鉴权、请求)
- 封装基本请求
- 数据库MySQL+MybatisPlus
- 全局异常处理
- 全局请求日志
- 跨域处理
- 工具类
- 接入Swagger + Knife4j 接口文档
- 多环境部署脚本run.sh



[TOC]

## 快速上手

> PS：可以直接查找TODO，根据提示使用
>
> 正式业务开发可以删除`IndexController`和`Book`表相关服务

TODO

- [ ] application配置文件根据TODO修改
- [ ] 根据sql文件生成数据库



- 项目启动后访问接口文档

| 说明    | 地址                                        |
| ------- | ------------------------------------------- |
| knife4j | http://127.0.0.1:8100/doc.html              |
| swagger | http://127.0.0.1:8100/swagger-ui/index.html |
| openapi | http://127.0.0.1:8100/v3/api-docs           |



### 使用说明

默认表结构：user+book

数据库+后端 `全部是驼峰命名！！！` 

主键默认都是自增

鉴权设计主要是userRole来判断(未考虑大型项目复杂化)

生成新表可参考book表设计完成(根据项目需要，可不实现DTO和VO)

打成Jar包体积约45M





### 分支说明

|        | 说明              |
| ------ | ----------------- |
| master | 正式、稳定        |
| dev    | 正在添加新功能... |
|        |                   |



## 业务功能

### 异常处理

| 类名                   | 描述           |
| ---------------------- | -------------- |
| GlobalExceptionHandler | 全局异常处理器 |
| ErrorCode              | 全局异常码     |
| BusinessException      | 业务异常       |
| ErrorCodePageHandler   | 错误码页面处理 |
| ThrowUtils             | 异常工具类     |



### 权限注解

在请求方法上添加，实现AOP响应拦截

```
@AuthCheck(mustRole = "admin")
```



切面实现具体看`AuthInterceptor`



### 日志处理

LogInterceptor   实现全局请求拦截



## 架构设计

合理分层：符合最佳实践开发设计

```
main
├── java
│   └── com
│       └── lty
│           ├── annotation    //注解
│           ├── aop             //切面
│           ├── common      //基础
│           ├── config       //配置
│           ├── constant      //常量
│           ├── controller      //请求层
│           ├── exception       //异常
│           ├── mapper       
│           ├── model         //实体类
│           ├── service         //业务层
│           ├── util 	 	 //工具
│           └── Start.java     //开始入口
└── resources
    ├── mapper
    ├── static
    ├── application-dev.yml      //开发环境
    ├── application-prod.yml   //上线环境
    ├── application.yml
    └── banner.txt			//项目信息
```



## 用户设计

### 用户表设计

```
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                           null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userRole     varchar(256) default 'user'            not null comment '用户角色',
    userName     varchar(256)                           null comment '用户昵称',
    avatarUrl    varchar(1024)                          null comment '用户头像',
    gender       tinyint      default 1                 null comment '性别',
    phone        varchar(128)                           null comment '电话',
    email        varchar(512)                           null comment '邮箱',
    userStatus   int          default 0                 not null comment '状态',
    createTime   datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint      default 0                 not null comment '是否删除'
)
    comment '用户';
```



### 注册设计

1. 用户在前端输入账户和密码、以及校验码（TODO）
2. 校验用户的账户、密码、校验密码，是否符合要求
   1. 非空
   2. 账户长度 **不小于** 4 位
   3. 密码就 **不小于** 6位
   4. 账户不能重复
   5. 账户不包含特殊字符？
   6. 密码和校验密码相同？
3. 对密码进行加密（密码千万不要直接以明文存储到数据库中）
4. 向数据库插入用户数据



### 用户登录

1. 校验用户账号和密码是否合理
2. 校验密码是否正确
3. 用户是否存在
4. 记录用户的登录态(session)

```
request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
```

4. 返回用户信息(controller脱敏)



### 获取当前用户

1. 判断用户是否已经登录
2. 从数据库查询(可加缓存)
3. 返回用户



### 是否管理员

根据HttpServletRequest判断返回布尔值



### 注销用户

1. 判断用户是否登录
2. 移除登录态



### 用户修改

个人或管理员才可修改



## 增删改查

> 项目以book表作为CRUD增删改查实现参考
>
> 注意：请求最好实现序列化接口：public class PageRequest implements Serializable



建表语句

```
create table book
(
    id         bigint auto_increment comment 'id'
        primary key,
    bookName   varchar(256)                       null comment '书名称',
    author     varchar(256)                       null comment '作者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '书';
```



### 开发流程

1.编写表结构

2.插件(mybatis-X)生成对应分层文件

3.文件拷贝(注意加TableLogic及修改文件路径)

4.添加验证规则

5.请求、查询、修改的各请求标识

6.替换变量(注意大小写)





## 一键部署

具体参考run.sh脚本实现部署



**服务器**

1. 拉取代码，并对run.sh设置变量(jarName和logFile)
2. chmod +x run.sh  (给脚本执行权利)
3. 执行./run.sh  (执行脚本,项目部署)



application生产配置(注意为了性能，可关闭 MyBatis Plus 的日志；为了安全，要给 Knife4j 接口文档设置用户名和密码)

```
mybatis-plus:
  configuration:
    # 生产环境关闭日志
    log-impl: ''

# 接口文档配置
knife4j:
  # 开启增强配置
  enable: true
  # 开启Swagger的Basic认证功能,默认是false
  basic:
    enable: true
    # Basic认证用户名
    username: lty
    # Basic认证密码
    password: 123456
 
 # 日志
logging:
  # 输出级别
  level:
    root: info
    com.lty.mapper: DEBUG  # 替换为实际的mapper包路径
  file:
    # 指定路径
    path: logs
  logback:
    rollingpolicy:
      # 最大保存天数
      max-history: 7
      # 每个文件最大大小
      max-file-size: 5MB
```

