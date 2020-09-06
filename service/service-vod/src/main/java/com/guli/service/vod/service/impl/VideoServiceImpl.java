package com.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.guli.service.base.exception.GuliException;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.vod.service.VideoService;
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
}
