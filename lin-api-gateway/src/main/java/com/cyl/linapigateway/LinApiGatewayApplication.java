package com.cyl.linapigateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableDubbo
public class LinApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("path_route", r -> r.path("/bilibili")
                        .uri("http://www.bilibili.com"))
                .route("host_route", r -> r.path("/dsgdg")
                        .uri("http://yupi.icu"))
                .build();
    }


}
