# Lin-API

#### 介绍
**基于Spring Boot + Dubbo + Gateway的API接口开放调用平台**

管理员可以接入并发布接口，通过SDK封装签名调用接口

通过Spring Cloud Gateway **网关校验签名，**使用**Dubbo Nacos**注册服务，帮助网关查询用户，接口是否存在**，实现接口调用次数加一****

#### 软件架构
软件架构说明

将项目分为五个模块
   ** 1.lin-api-backend**:接口增删改查，上线，下线接口
   2.lin-common：抽象出公共实体类和接口
   3.lin-api-gateway：校验签名，接口调用次数统计
   4.lin-client-sdk：帮助客户端发送请求，生成签名，通过once随机数，tempstemp时间戳防止重放，并使用response装饰器打印日志，调用成功将接口调用次数加一
   5.lin-api-interface：提供接口服务
    
    

