package com.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.service.edu.entity.Chapter;
import com.guli.service.edu.entity.Video;
import com.guli.service.edu.entity.vo.ChapterVo;
import com.guli.service.edu.entity.vo.VideoVo;
import com.guli.service.edu.mapper.ChapterMapper;
import com.guli.service.edu.mapper.VideoMapper;
import com.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<ChapterVo> nestedListById(String courseId) {

        // 根据courseId获取chapter列表
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",courseId);
            // 排序 sort相同 根据id排
        chapterQueryWrapper.orderByAsc("sort","id");
        List<Chapter> chapterList = baseMapper.selectList(chapterQueryWrapper);


        // 根据courseId获取video列表
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",courseId);
        // 排序 sort相同 根据id排
        videoQueryWrapper.orderByAsc("sort","id");
        List<Video> videoList = videoMapper.selectList(videoQueryWrapper);

        // 将chapterList组装成ChapterVo列表
        ArrayList<ChapterVo> chapterVoArrayList = new ArrayList<ChapterVo>();
        // 遍历chapterList
        for (Chapter chapter : chapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            chapterVoArrayList.add(chapterVo);

            // 将videoList组装成VideoVo列表
            ArrayList<VideoVo> videoVoArrayList = new ArrayList<>();
            for (Video video : videoList) {
                if (chapter.getId().equals(video.getChapterId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);
                    videoVoArrayList.add(videoVo);
                }
            }
            // 将videoList放入chapterVo的children属性中
            chapterVo.setChildren(videoVoArrayList);
        }



        return  chapterVoArrayList;

//         遍历chapter列表{
//            根据chapterId 构建 video列表
//         }

    }
}
