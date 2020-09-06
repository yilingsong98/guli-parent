package com.guli.service.oss.controller.admin;


import com.guli.service.base.exception.GuliException;
import com.guli.service.base.result.R;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.oss.service.FileService;
import com.guli.service.oss.utils.OssProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Api(tags = "阿里云文件管理")
@CrossOrigin // 跨域
@RequestMapping("/admin/oss/file")
@RestController
public class FileController {

    @Autowired
    private OssProperties ossProperties;
    @Autowired
    private FileService fileService;

//    @GetMapping("test")
//    public R test(){
//        String endpoint = ossProperties.getEndpoint();
//        return R.ok().data("endpoint",endpoint);
//    }

    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(
                @ApiParam(value = "文件",required = true)
                MultipartFile file,
                @ApiParam(value = "文件上传所属的模块名称", required = true)
                String module) throws IOException {

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadUrl = fileService.upload(inputStream,originalFilename , module);

            //返回r对象
            return R.ok().message("文件上传成功").data("url", uploadUrl);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation("删除文件")
    @DeleteMapping("remove")
    public R removeFile(@ApiParam(value = "文件的url地址",required = true)
                        @RequestBody String url){

        fileService.removerFile(url);
        return R.ok().message("文件删除成功");
    }


    @GetMapping("test-oss")
    public R test(){
        System.out.println("oss test is running");
        return R.ok();
    }

}
