package com.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.guli.service.oss.service.FileService;
import com.guli.service.oss.utils.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String originalFileName, String module) {
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(
            ossProperties.getEndpoint(),
            ossProperties.getKeyId(),
            ossProperties.getKeySecret());

        String bucketName = ossProperties.getBucketName();
        // 判断是否存在bucketName
        if (!ossClient.doesBucketExist(bucketName)){
            ossClient.createBucket(bucketName);
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }

        // 上传文件流
        // 随机文件名： avatar/2020/08/31/随机文件名.扩展名
        String folder = new DateTime().toString("yyyy-MM-dd");
        String fileName = UUID.randomUUID().toString();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String key = module + "/" + folder + "/" + fileName + fileExtension;
        //文件上传至阿里云
        ossClient.putObject(bucketName, key, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        //返回url地址
        return "https://" + bucketName + "." + ossProperties.getEndpoint() + "/" + key;
    }

    @Override
    public void removerFile(String url) {
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(
                ossProperties.getEndpoint(),
                ossProperties.getKeyId(),
                ossProperties.getKeySecret());

        String host = "https://" +  ossProperties.getBucketName() + "." + ossProperties.getEndpoint() + "/";
        // url = host + objectName
        String objectName = url.substring(host.length());

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(ossProperties.getBucketName(), objectName);

        // 关闭OSSClient。
        ossClient.shutdown();

    }
}
