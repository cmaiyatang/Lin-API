# Lin-API

[toc]
项目架构图

![image](https://github.com/cmaiyatang/Lin-API/assets/127107267/53006b44-f879-495b-bdab-97f527b7ca68)



#### 介绍
**基于Spring Boot + Dubbo + Gateway的API接口开放调用平台**

管理员可以接入并发布接口，通过SDK封装签名调用接口

通过Spring Cloud Gateway **网关校验签名，**使用**Dubbo Nacos**注册服务，帮助网关查询用户，接口是否存在，实现**接口调用次数加一**

#### 软件架构
软件架构说明

将项目分为五个模块
   **1.lin-api-backend**:接口增删改查，上线，下线接口
   
   **2.lin-common**：抽象出公共实体类和接口
   
  **3.lin-api-gateway**：校验签名，接口调用次数统计
  
   **4.lin-client-sdk**：帮助客户端发送请求，生成签名，通过once随机数，tempstemp时间戳防止重放，并使用response装饰器打印日志，调用成功将接口调用次数加一
   
   **5.lin-api-interface**：提供接口服务


**项目笔记**
## api签名认证

## 掘金

https://juejin.cn/post/6857363801533710343

## csdn

https://blog.csdn.net/incredible1024/article/details/132652050

在请求头中添加 accesskey 和 secretkey

后端验证这两个参数，确认身份

accesskey

secretkey

accesskey和secretkey是在进行身份验证时使用的一对密钥。通常在云服务、存储服务、CDN等各种服务的API调用中需要使用accesskey和secretkey来进行身份验证，以确保只有授权的用户才能够访问和使用这些服务\

## Spring Cloud gateway

https://docs.spring.io/spring-cloud-gateway/docs/4.0.9/reference/html/#gateway-starter

**实现：编程式，配置式**

### 路由

网关的基本组成,它由ID,目标URL,断言和过滤器组成.如果断言为true,将匹配路由

```java
											---  编程式 ---
@Bean
public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
            .route("path_route", r -> r.path("/yupiicu")
                    .uri("http://yupi.icu"))
            .route("host_route", r -> r.path("/dsgdg")
                    .uri("http://yupi.icu"))
            .build();
}
```


### 断言 

**只有断言成功的请求才会匹配路由**

### 作用

​	1.可以判断请求路径是否包含某一部分

​	2.可以判断请求时间是否在某一时间之前，之后，在某段时间之内

```java
											---   配置式   ---
spring:
  cloud:
    gateway:
      routes:
        - id: path_route
          uri: http://localhost:8100
          predicates:
            - Path=/api/**
            - Before=2017-01-20T17:42:47.789-07:00[America/Denver]
```

如果请求路径中有/api ，则将页面重定向到http://localhost:8100/api/**

如http://localhost:8848/api/name   =>  http://localhost:8100/api/name


### 过滤器

https://juejin.cn/post/6844903741867425800

**可以对请求或响应进行处理 **       **！！！！增删改查！！！！**

![1.png](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2018/12/17/167bc70d89b7299e~tplv-t2oaga2asx-jj-mark:3024:0:0:0:q75.png)

每个服务被调用的时候都会做一些**相同的操作**，非常麻烦

我们可以将这些**相同的操作全部提取出来**，将他们放到一个**单独的模块去开发和管理**

我们利用filter来实现

![2.png](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2018/12/17/167bc70d8a85463a~tplv-t2oaga2asx-jj-mark:3024:0:0:0:q75.png)

#### 类型和作用

filter过滤器可以用来 

​	**鉴权 ，**

​	**限流，**

​	**日志输出**

**1**.**在“pre”类型的过滤器**可以做参数校验、**权限校验**、**流量监控**、日志输出、协议转换，

**2.**在“post”类型的过滤器中可以做**响应内容、响应头的修改**，日志的输出，流量监控等。

​	**添加请求头**

- AddRequestHeader=younglin,cyl  --请求头
- 调用的接口可以使用request.getHeader("younglin")来得到请求头的参数

```java
spring:
  cloud:
    gateway:
      routes:
        - id: path_route
          uri: http://localhost:8100
          predicates:
            - Path=/api/**
          filters:
            - AddRequestHeader=younglin,cyl
            - AddRequestParameter=name, cyl
```

​	**添加请求参数**

- AddRequestParameter=name, cyl   请求参数 name -> cyl

**3.熔断（降级）**

当请求失败是，执行一些操作 ,例如跳转到其他页面等

### 染色

\- AddResponseHeader=X-Response-Red, Blue

为请求添加某种**标识**   因为官方文档中有red，blue等，所以叫染色

### 负载均衡

将url改成服务地址

## Dubbo微服务

https://cn.dubbo.apache.org/zh-cn/overview/quickstart/java/spring-boot/#5-%E5%90%AF%E5%8A%A8%E6%9C%8D%E5%8A%A1%E6%B6%88%E8%B4%B9%E8%80%85

提供者 provider

注册中心

消费者consumer

提供者在注册中心注册服务

消费者访问注册中心拿到已注册的服务

消费者调用提供者提供的服务

## 4.申请签名（注册）

用户注册成功后自动分配，accessKey ，secretKey

## 5.调用接口

注意将请求参数转换为**User****对象**

使用谷歌的**Json**工具**Gson**

```java
Gson gson = new Gson(); 
com.cyl.ylapiclientsdk.model.User user = gson.fromJson(userRequestParams, com.cyl.ylapiclientsdk.model.User.class);

```

## 6.统计接口调用次数

**业务逻辑**

网关spring cloud gateway

1.用户请求网关

2.黑名单，请求日志

3.对用户进行鉴权 ak，sk是否合法

4.判断接口是否存在，

5.将请求转发到目标接口地址

6.打印响应日志

7.调用接口成功后，将接口调用次数➕1

8.调用失败，降级（熔断）或者返回一个规范的错误码



总体业务逻辑

调用younglinclient方法，该方法将请求发送到网关，网关执行一系列操作


需求

1.用户调用接口成功后次数+1 修改数据库次数 + 1



数据库表设计



​	用户 =》接口（多对多）

用户接口关系表

user_interface_info

```sql
-- 用户接口信息表
create table if not exists yapi.`user_interface_info`
(
    `id`              bigint                             not null auto_increment comment '主键' primary key,
    `userId`          bigint                             not null comment '用户id',
    `interfaceInfoId` bigint                             not null comment '接口id',
    `totalNum`        int      default 0                 not null comment '总调用次数',
    `leftNum`         int      default 0                 not null comment '剩余调用次数',
    `status`          int      default 0                 not null comment '0-正常，1-禁用',
    `create_time`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_deleted`      tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户接口信息表';

```

```
create table if not exists yapi.interface_info
(
    id               bigint auto_increment comment '主键'
        primary key,
    name             varchar(256)                       not null comment '接口名称',
    description      varchar(256)                       null comment '接口描述',
    url              varchar(512)                       not null comment '接口地址',
    sdkPath          varchar(64)                        not null comment '接口对应的SDK',
    parameterExample varchar(128)                       null comment '接口请求示例',
    requestHeader    text                               null comment '请求头',
    requestParams    varchar(512)                       null comment '请求参数',
    responseHeader   text                               null comment '响应头',
    sdkInvokeMethod  varchar(32)                        null comment 'sdk里调用接口的方法',
    status           int      default 0                 not null comment '接口状态（0-关闭，1-开启）',
    method           varchar(256)                       not null comment '请求类型',
    userId           bigint                             null comment '创建人',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted       tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
)
    comment '接口信息表';
``` 

