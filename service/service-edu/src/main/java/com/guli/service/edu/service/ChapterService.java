package com.guli.service.edu.service;

import com.guli.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
public interface ChapterService extends IService<Chapter> {

    boolean removeChapterById(String id);
}
