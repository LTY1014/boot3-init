## boot3-init

基于 Spring Boot 3 的简单后端脚手架，提供用户体系、权限校验、统一响应、全局异常、请求日志和 Book CRUD 示例。

<p align=center>
    <a href="http://gitee.com/liang-tian-yu">Spring Boot 3 脚手架</a>
</p>
<p align="center">
<a target="_blank" href="http://gitee.com/liang-tian-yu">
    <img src="https://img.shields.io/badge/JDK-17+-green" ></img>
    <img src="https://img.shields.io/badge/springboot-3.5.9-green" ></img>
    <img src="https://img.shields.io/badge/mysql-5.7-blue" ></img>
    <img src="https://img.shields.io/badge/MybatisPlus-3.5.9-green" ></img>
    <img src="https://img.shields.io/badge/Knife4j-4.4.0-brightgreen" ></img>
</a></p>




主要内容：

- Spring Boot 3.5.9 + JDK 17
- MySQL + MyBatis-Plus
- 基于 Session 的登录态和基于 AOP 的角色鉴权
- 用户注册、登录、注销、查询、修改、删除及密码管理
- Book CRUD 与分页示例
- 统一响应、全局异常处理
- 全局请求日志（敏感参数脱敏、超长参数截断）
- 全局跨域和静态资源配置
- Swagger / Knife4j 接口文档



[TOC]

## 快速上手

> 正式业务开发可以删除 `Book` 表相关服务（示例 CRUD）

### 1. 准备环境
- JDK：17+
- MySQL：5.7+

### 2. 初始化数据库
1. 创建数据库 `demo`（字符集建议使用 `utf8mb4`）。
2. 在 `demo` 数据库中执行 `sql/create.sql`，创建 `user`、`book` 表并写入示例数据。

> `application.yml` 默认连接 `localhost:3306/demo`。如使用其他库名或连接信息，请同步修改数据源配置。

### 3. 修改配置（必须项）
> 以下是“配置项清单表”，也可以找TODO提示
| 配置项 | 文件 | 说明 | 示例 |
| --- | --- | --- | --- |
| spring.application.name | application.yml | 项目名称   | boot3-init |
| spring.datasource.url | application.yml | 数据库连接 | jdbc:mysql://localhost:3306/demo |
| spring.datasource.username | application.yml | 用户名 | root |
| spring.datasource.password | application.yml | 密码 | 123456（请按本机环境修改） |
| server.port | application.yml | 端口 | 8100 |



### 4. 启动与验证

```bash
mvn spring-boot:run
```

- 启动应用后访问接口文档：

| 说明    | 地址                                        |
| ------- | ------------------------------------------- |
| knife4j | http://127.0.0.1:8100/doc.html              |
| swagger | http://127.0.0.1:8100/swagger-ui/index.html |
| openapi | http://127.0.0.1:8100/v3/api-docs           |



> 生产环境注意事项
>
> - 生产环境建议默认关闭 Swagger/Knife4j，或仅内网访问 + Basic 认证
> - 敏感配置（DB 密码、文档密码）不要提交到 git，建议通过环境变量/外部配置注入

### 使用说明

默认表结构：`user` + `book`。

数据库字段与 Java 属性均使用驼峰命名，MyBatis-Plus 已设置 `map-underscore-to-camel-case: false`。

两张表的主键均为自增主键，`isDelete` 字段启用 MyBatis-Plus 逻辑删除（`0` 未删除，`1` 已删除）。

登录态保存在 HttpSession 中，Session 默认有效期为 1 天；鉴权主要根据 `userRole` 判断，内置 `user`、`admin` 两种角色。

新增业务可参考 `business` 包下的 Book 示例；正式开发时可以删除整个 Book 示例模块及对应 SQL。

统一响应格式为 `BaseResponse<T>`，业务异常由 `GlobalExceptionHandler` 转换为统一响应。

## 业务功能

### 异常处理

| 类名 | 描述 |
| --- | --- |
| `GlobalExceptionHandler` | 统一处理 `BusinessException` 和其他运行时异常 |
| `ErrorCode` | 全局业务错误码 |
| `BusinessException` | 携带业务错误码的自定义异常 |
| `ThrowUtils` | 按条件抛出业务异常的工具类 |



### 权限注解

在 Controller 方法上添加 `@AuthCheck`，由 `AuthInterceptor` 通过 AOP 校验当前 Session 用户的角色：

```java
@AuthCheck(mustRole = "admin")
```

注解支持：

- `mustRole`：当前用户必须拥有指定角色。
- `anyRole`：当前用户的角色命中指定角色列表之一即可。
- 使用该注解的方法必须先登录；未登录返回 `40100`，角色不匹配返回 `40101`。



### 日志处理

`LogInterceptor` 拦截 `com.lty..controller` 下的请求方法，记录请求 ID、路径、IP、参数和耗时。密码等敏感字段会被替换为 `******`，参数日志超过 2000 个字符时会截断。

### 用户接口

基础路径：`/user`。除表格中特别注明外，接口未使用 `@AuthCheck`；“需登录”由 Controller 或 Service 中的登录态检查实现。

| 方法 | 路径 | 说明 | 访问要求 |
| --- | --- | --- | --- |
| POST | `/user/register` | 用户注册；账号至少 4 位，密码至少 6 位；密码为空时使用默认密码 | 公开 |
| POST | `/user/login` | 用户登录并在 Session 中保存用户 ID | 公开 |
| POST | `/user/logout` | 注销并移除登录态 | 需登录 |
| GET | `/user/current` | 获取当前登录用户的脱敏信息 | 需登录 |
| POST | `/user/update` | 修改用户资料；非管理员不能修改角色 | 本人或管理员 |
| GET | `/user/get?id={id}` | 按 ID 获取用户 | 管理员 |
| GET | `/user/list` | 查询用户列表，支持账号、昵称模糊查询及排序 | 管理员 |
| GET | `/user/list/page` | 分页查询用户，单页最多 50 条 | 公开 |
| POST | `/user/delete` | 逻辑删除用户 | 管理员 |
| POST | `/user/updatePassword` | 校验旧密码后修改当前用户密码 | 需登录 |
| POST | `/user/resetPassword/{userId}` | 将指定用户密码重置为 `123456` | 管理员 |
| POST | `/user/add` | 新增用户，初始密码为 `123456` | 管理员 |



### Book 示例接口

基础路径：`/book`，当前示例接口均未配置登录或角色限制。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/book/add` | 新增图书 |
| POST | `/book/update` | 修改图书 |
| POST | `/book/id` | 按 ID 查询图书，请求体使用 `IdRequest` |
| POST | `/book/list` | 查询图书列表；查询条件暂未实现，目前返回全部未删除记录 |
| POST | `/book/list/page` | 分页查询图书，单页最多 200 条 |
| POST | `/book/delete` | 根据 `IdRequest.ids` 批量逻辑删除图书 |

### 通用分页参数

| 字段 | 默认值 | 说明 |
| --- | --- | --- |
| `current` | 1 | 当前页码 |
| `pageSize` | 10 | 每页数量 |
| `sortField` | 空 | 排序字段 |
| `sortOrder` | `ascend` | `ascend` 为升序，其他非空值按降序处理 |

### 工具类

| 类名 | 说明 |
| --- | --- |
| `PasswordUtil` | 固定盐 MD5 摘要、密码校验、强度检查、随机密码与随机盐生成 |
| `IpInfoUtil` | 从代理请求头或远程地址中获取客户端 IP |
| `ServletUtil` | 获取当前请求、响应和 Session |



## 架构设计

项目采用 Controller、Service、Mapper、Model 分层，并将 Book 演示业务集中在 `business` 包中。

```text
main
├── java
│   └── com
│       └── lty
│           ├── annotation       // 权限注解
│           ├── aop              // 权限与请求日志切面
│           ├── business         // Book 示例业务（controller/mapper/model）
│           ├── common           // 统一响应和通用请求对象
│           ├── config           // Jackson、跨域、Knife4j、MyBatis-Plus 配置
│           ├── constant         // 通用常量和用户常量
│           ├── controller       // 用户接口层
│           ├── exception        // 业务异常和全局异常处理
│           ├── mapper           // 用户 Mapper
│           ├── model            // 用户 DTO、实体、枚举和 VO
│           ├── service          // 用户服务层
│           ├── util             // IP、密码和 Servlet 工具
│           └── Start.java       // 应用启动入口
└── resources
  ├── mapper                   // MyBatis XML
  ├── static                   // 静态资源和 404 页面
  ├── application.yml          // 唯一的应用配置文件
  └── banner.txt               // 启动 Banner
```



## 用户设计

### 用户表设计

完整建表语句见 `sql/create.sql`。`userAccount` 非空且有唯一索引；`userRole` 默认为 `user`；`userStatus` 的 `0` 表示正常；`isDelete` 用于逻辑删除。



### 注册设计

1. 校验请求体不为空，账号长度不少于 4 位。
2. 密码不为空时要求不少于 6 位；密码为空时使用默认密码 `123456`。
3. 注册用户的角色固定为 `user`。
4. 使用固定盐值 MD5 摘要后保存密码。
5. 由数据库唯一索引保证账号不重复。



### 用户登录

1. 校验用户账号和密码是否合理
2. 校验密码是否正确
3. 用户是否存在
4. 在 Session 中保存用户 ID：

```java
request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user.getId());
```

5. 返回不包含密码和逻辑删除字段的 `UserVO`。



### 获取当前用户

1. 从 Session 中读取用户 ID。
2. 根据 ID 查询数据库，确认用户存在且状态正常。
3. 返回脱敏后的用户信息。



### 是否管理员

根据当前 Session 用户的 `userRole` 是否为 `admin` 返回布尔值。



### 注销用户

1. 判断 Session 中是否存在登录态。
2. 移除登录态。



### 用户修改

仅本人或管理员可以修改用户资料；非管理员提交的角色字段会被忽略。





## 一键部署

`run.sh` 适用于 Linux/Unix 环境，会依次拉取代码、执行 Maven 打包、强制停止旧进程，并以 `nohup` 启动新 Jar。



**服务器**

1. 首次手动拉取代码，并按需修改 `run.sh` 中的 `jarName` 和 `logFile`。
2. 执行 `chmod +x run.sh` 授予执行权限。
3. 执行 `./run.sh` 部署。脚本默认构建 `target/boot3-init.jar`，以 `-Xms512m -Xmx512m` 启动，并将日志写入 `target/1.out`。



当前项目只有一个 `application.yml`，未内置 dev/prod Profile。生产部署时建议使用外部配置覆盖敏感项，并关闭 MyBatis-Plus SQL 标准输出。Knife4j Basic 认证配置当前处于注释状态，如需启用可参考：

```yaml
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
    username: admin
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

