package com.guli.service.vod.service;

import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.List;

public interface VideoService {

    String uploadVideo(InputStream inputStream, String originalFilename);

    void removeVideo(String videoSourceId) throws ClientException;

    void removeVideoByIdList(List<String> videoIdList) throws ClientException;
}
