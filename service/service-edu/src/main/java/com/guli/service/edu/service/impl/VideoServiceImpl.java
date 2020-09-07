package com.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.service.edu.entity.Video;
import com.guli.service.edu.fegin.VodMediaService;
import com.guli.service.edu.mapper.VideoMapper;
import com.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {


    @Autowired
    private VodMediaService vodMediaService;

    @Override
    public void removeMediaVideoById(String id) {
        // 根据id找到视频
        Video video = baseMapper.selectById(id);
        if (video != null) {
            vodMediaService.removeVideo(video.getVideoSourceId());
        }
    }

    @Override
    public void removeMediaVideoByChapterId(String chapterId) {
        // 删除章节的同时 删除视频
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        // 根据章节 找到视频
        // 查询条件 查询为此chapterId的videoSourceId
        queryWrapper.select("video_source_id");
        queryWrapper.eq("chapter_id",chapterId);

        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);

        List<String> videoSourceIdList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            String videoSourceId = (String) map.get("video_source_id");
            // 找到所有要删除的视频id 添加到集合中
            videoSourceIdList.add(videoSourceId);
        }

        vodMediaService.removeVideoByIdList(videoSourceIdList);
    }

    @Override
    public void removeMediaVideoByCourseId(String courseId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("course_id", courseId);
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        List<String> videoSourceIdList = this.getVideoSourceIdList(maps);
        vodMediaService.removeVideoByIdList(videoSourceIdList);
    }

    /**
     * 获取阿里云视频id列表
     */
    private List<String> getVideoSourceIdList(List<Map<String, Object>> maps){
        List<String> videoSourceIdList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            String videoSourceId = (String)map.get("video_source_id");
            videoSourceIdList.add(videoSourceId);
        }
        return videoSourceIdList;
    }
}
