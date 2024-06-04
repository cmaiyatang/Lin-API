package com.cyl.linapiinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class })
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class LinApiInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinApiInterfaceApplication.class, args);
    }

}
