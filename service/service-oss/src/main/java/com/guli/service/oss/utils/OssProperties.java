package com.guli.service.oss.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss") // 自动读取配置文件 配置读取的前缀
public class OssProperties {
    private String endpoint;
    private String keyId;
    private String keySecret;
    private String bucketName;
}
