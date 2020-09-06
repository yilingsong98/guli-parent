package com.guli.service.vod.controller.admin;


import com.guli.service.base.exception.GuliException;
import com.guli.service.base.result.R;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.vod.service.VideoService;
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

@Api(tags = "阿里云视频管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/vod/media")
@Slf4j
public class MediaController {

    @Autowired
    private VideoService videoService;


    @ApiOperation("视频上传")
    @PostMapping("upload")
    public R uploadVideo(
            @ApiParam(value = "视频文件" ,required = true)
            @RequestParam("file") MultipartFile file){

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String videoId = videoService.uploadVideo(inputStream,originalFilename);
            return R.ok().message("视频上传成功").data("videoId",videoId);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR );
        }
    }


}
