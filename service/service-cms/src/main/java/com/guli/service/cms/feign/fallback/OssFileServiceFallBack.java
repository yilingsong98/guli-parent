package com.guli.service.cms.feign.fallback;

import com.guli.service.base.result.R;
import com.guli.service.cms.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {


    @Override
    public R test() {
        return null;
    }

    @Override
    public R removeFile(String url) {
        log.warn("远程调用失败，服务熔断");
        return R.error();
    }
}