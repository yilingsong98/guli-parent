package com.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.service.edu.entity.Chapter;
import com.guli.service.edu.entity.Video;
import com.guli.service.edu.mapper.ChapterMapper;
import com.guli.service.edu.mapper.VideoMapper;
import com.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public boolean removeChapterById(String id) {

        // 删除课时:根据章节id删除课时
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id",id);
        videoMapper.delete(videoQueryWrapper);

        // 删除章节
        return this.removeById(id);

    }
}
