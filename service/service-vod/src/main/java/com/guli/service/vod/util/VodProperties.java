package com.guli.service.vod.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.vod") // 自动读取配置文件 配置读取的前缀
public class VodProperties {
    private String keyId;
    private String keySecret;
    private String templateGroupId;
    private String workflowId;
}
