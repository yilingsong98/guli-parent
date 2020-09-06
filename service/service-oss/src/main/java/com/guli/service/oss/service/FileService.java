package com.guli.service.oss.service;

import java.io.InputStream;

public interface FileService {

     String upload(InputStream inputStream,String originalFileName,String module);

    void removerFile(String url);
}
