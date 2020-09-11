package com.guli.service.edu.fegin;


import com.guli.service.base.result.R;
import com.guli.service.edu.fegin.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;


// 服务熔断 即：此 远程调用的 本地调用接口的 实现
// 指定 远程服务器 和 本地熔断器
@Service
@FeignClient(value = "service-oss", fallback = OssFileServiceFallBack.class)
public interface OssFileService {


    @GetMapping("/admin/oss/file/test-oss")
    public R test();

    // 远程调用的接口
    @DeleteMapping("/admin/oss/file/remove")
    public R removeFile(@RequestBody String url);
}
