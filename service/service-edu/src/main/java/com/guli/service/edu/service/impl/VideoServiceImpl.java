package com.guli.service.edu.service.impl;

import com.guli.service.base.result.R;
import com.guli.service.edu.entity.Video;
import com.guli.service.edu.fegin.VodMediaService;
import com.guli.service.edu.mapper.VideoMapper;
import com.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
