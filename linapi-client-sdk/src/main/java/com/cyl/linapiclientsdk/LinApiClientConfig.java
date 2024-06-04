package com.cyl.linapiclientsdk;

import com.cyl.linapiclientsdk.client.LinApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * YuApi 客户端配置
 *
 * @author <a href="https://github.com/cmaiyatang">YoungLin</a>

 */
@Configuration
@ConfigurationProperties("linapi.client")
@Data
@ComponentScan
public class LinApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public LinApiClient linApiClient() {
        return new LinApiClient(accessKey, secretKey);
    }

}
