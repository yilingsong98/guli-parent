package com.guli.service.vod.controller.admin;


import com.aliyuncs.exceptions.ClientException;
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
import java.util.List;

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


    @ApiOperation("删除视频")
    @DeleteMapping("remove/{videoSourceId}")
    public R removeVideo(
            @ApiParam(value="阿里云视频id", required = true)
            @PathVariable String videoSourceId){

        try {
            videoService.removeVideo(videoSourceId);
            return R.ok().message("视频删除成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

    @ApiOperation("删除视频")
    @DeleteMapping("remove")
    public R removeVideoByIdList(
            @ApiParam(value="阿里云视频id", required = true)
            @RequestBody List<String> videoIdList) throws ClientException {

        videoService.removeVideoByIdList(videoIdList);
        return R.ok().message("删除视频成功");
    }


}
