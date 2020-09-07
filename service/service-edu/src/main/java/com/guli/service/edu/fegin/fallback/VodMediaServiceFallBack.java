package com.guli.service.edu.fegin.fallback;

import com.guli.service.base.result.R;
import com.guli.service.edu.fegin.VodMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VodMediaServiceFallBack implements VodMediaService {
    @Override
    public R removeVideo(String videoSourceId) {
        log.warn("熔断保护");
        return R.error();
    }

    @Override
    public R removeVideoByIdList(List<String> videoIdList) {
        log.warn("熔断保护");
        return R.error();
    }
}