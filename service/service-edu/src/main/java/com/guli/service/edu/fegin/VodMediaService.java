package com.guli.service.edu.fegin;

import com.guli.service.base.result.R;
import com.guli.service.edu.fegin.fallback.VodMediaServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@FeignClient(value = "service-vod", fallback = VodMediaServiceFallBack.class)
public interface VodMediaService {

    @DeleteMapping("/admin/vod/media/remove/{videoSourceId}")
    R removeVideo(@PathVariable("videoSourceId") String videoSourceId);


    @DeleteMapping("/admin/vod/media/remove")
    R removeVideoByIdList(@RequestBody List<String> videoIdList);
}