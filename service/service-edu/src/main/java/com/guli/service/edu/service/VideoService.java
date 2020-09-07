package com.guli.service.edu.service;

import com.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
public interface VideoService extends IService<Video> {

    void removeMediaVideoById(String id);

    // 删除 课程的时候 删除该节课的视频
    void removeMediaVideoByChapterId(String chapterId);

    // 删除 章节的时候 删除该章下的所有视频
    void removeMediaVideoByCourseId(String courseId);
}
