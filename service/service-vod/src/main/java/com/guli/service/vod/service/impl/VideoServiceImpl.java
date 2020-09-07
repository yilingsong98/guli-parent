package com.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.guli.service.base.exception.GuliException;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.vod.service.VideoService;
import com.guli.service.vod.util.AliyunVodSDKUtils;
import com.guli.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VodProperties vodProperties;

    @Override
    public String uploadVideo(InputStream inputStream, String originalFilename) {
        // 创建请求对象
        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        UploadStreamRequest request = new UploadStreamRequest(
                vodProperties.getKeyId(),
                vodProperties.getKeySecret(),
                title,
                originalFilename,
                inputStream);

        //模板组ID(可选)
//        request.setTemplateGroupId(vodProperties.getTemplateGroupId());
        // 工作流ID（可选）
        request.setWorkflowId(vodProperties.getWorkflowId());
        //创建uploader客户端对象
        UploadVideoImpl uploader = new UploadVideoImpl();
        //发送请求得到响应
        UploadStreamResponse response = uploader.uploadStream(request);

        //获取阿里云视频id
        String videoId = response.getVideoId();
        if(StringUtils.isEmpty(videoId)){
            log.error("阿里云视频上传失败" + response.getCode() + " - " + response.getMessage());
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }
        return videoId;
    }

    @Override
    public void removeVideo(String videoSourceId) throws ClientException {
        // 创建客户端对象
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyId(),
                vodProperties.getKeySecret());

        // 创建请求对象
        // 视频删除类
        DeleteVideoRequest request = new DeleteVideoRequest();
        // 设置请求参数：支持多个视频,逗号分隔
        // 传入视频源id
        request.setVideoIds(videoSourceId);

        client.getAcsResponse(request);
    }
}
