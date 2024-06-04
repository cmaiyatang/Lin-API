package com.cyl.linapigateway;


import com.cyl.common.entity.InterfaceInfo;
import com.cyl.common.entity.User;
import com.cyl.common.service.nacos.NacosInterfaceInfoService;
import com.cyl.common.service.nacos.NacosUserInterfaceInfoService;
import com.cyl.common.service.nacos.NacosUserService;
import com.cyl.linapigateway.utils.IpUtil;
import com.cyl.linapigateway.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private NacosUserService nacosUserService;

    @DubboReference
    private NacosInterfaceInfoService nacosInterfaceInfoService;

    @DubboReference
    private NacosUserInterfaceInfoService nacosUserInterfaceInfoService;


    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8102";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("custom global filter：", exchange.getRequest().getPath());

        // 获取请求信息
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //打印日志
        // region
        log.info("请求路径" + request.getPath());
        log.info("请求方法" + request.getMethod());
        log.info("来源地址" + request.getRemoteAddress());
        log.info("本地地址" + request.getLocalAddress());
//        String remoteIP = Objects.requireNonNull(IpUtil.getIP(request));
        String remoteIP = request.getRemoteAddress().getHostString();
        // endregion

        //拿到请求头
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String once = headers.getFirst("once");
        String timestamp = headers.getFirst("timestamp");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");

        //请求白名单
        if (!IP_WHITE_LIST.contains(remoteIP)) {
            log.info("请求来源地址" + remoteIP);
            Mono<Void> voidMono = handleNoAuth(response);
            return voidMono;
        }

//        限制请求

        //1. 校验once
        if (Long.parseLong(once) > 10000L) {
            return handleNoAuth(response);
        }
        //2. 校验timestamp  不能超过2分钟
        final long TWO_MINUTES = 60 * 2L;
        if (System.currentTimeMillis() / 1000 - Long.parseLong(timestamp) > TWO_MINUTES) {
            Mono<Void> voidMono = handleNoAuth(response);
            return voidMono;
        }

        //用户是否存在
        User invokeUser = nacosUserService.getInvokeUser(accessKey);
        if (invokeUser == null) {
            //调用方不存在
            throw new RuntimeException("调用方不存在");
        }
        //3. 校验sign签名
        if (!Objects.equals(sign, SignUtils.genSign(body, invokeUser.getSecretKey()))) {
            Mono<Void> voidMono = handleNoAuth(response);
            return voidMono;
        }

        //4. 校验接口是否存在
        String url = INTERFACE_HOST + request.getPath().toString();
        String method = request.getMethod().toString();
        InterfaceInfo interfaceInfo = nacosInterfaceInfoService.getInterface(url, method);
        if (interfaceInfo == null) {
            Mono<Void> voidMono = handleNoAuth(response);
            return voidMono;
        }

        return handleResponse(exchange, chain, invokeUser.getId(), interfaceInfo.getId());

    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long userId, Long interfaceInfoId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            nacosUserInterfaceInfoService.invokeNumPlusOne(userId, interfaceInfoId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }


    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}