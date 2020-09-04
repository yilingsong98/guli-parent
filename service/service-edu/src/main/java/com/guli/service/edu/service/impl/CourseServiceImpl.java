package com.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.service.edu.entity.*;
import com.guli.service.edu.entity.form.CourseInfoForm;
import com.guli.service.edu.entity.query.CourseQuery;
import com.guli.service.edu.entity.vo.CourseVo;
import com.guli.service.edu.mapper.*;
import com.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.management.Query;
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
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CourseCollectMapper courseCollectMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {

        // 保存Course
        Course course = new Course();
        course.setStatus(Course.COURSE_DRAFT);
        // BeanUtils此工具类的copyProperties() 将参数一 拷贝到 参数二
        BeanUtils.copyProperties(courseInfoForm,course);
        baseMapper.insert(course);


        // 保存CourseDescription
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.insert(courseDescription);

        return course.getId();
    }

    /**
     * 方案二： 在mapper层查询CourseInfoForm信息,效率更高
     * @param id
     * @return
     */
    @Override
    public CourseInfoForm getCourseInfoFormById(String id) {
        return baseMapper.selectCourseInfoFormById(id);
    }

    @Override
    public boolean updateCourseInfoById(CourseInfoForm courseInfoForm) {
        // 更新course
        Course course = new Course();
        // 将传入的值拷贝到course
        BeanUtils.copyProperties(courseInfoForm,course);
        // 根据id更新course
        int i = baseMapper.updateById(course);
        if (i == 0){
            return false;
        }

        // 更新courseDescription
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseDescription.getDescription());
        courseDescription.setId(courseInfoForm.getId());
        int i1 = courseDescriptionMapper.updateById(courseDescription);
        // 如果 i1 == 0 意味着没有详情信息 将详情存入
        if (i1 == 0){
            courseDescriptionMapper.insert(courseDescription);
        }
        return true;
    }

    @Override
    public IPage<CourseVo> selectPage(Long page, Long limit, CourseQuery courseQuery) {
        
        // 组装查询条件
        QueryWrapper<CourseVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("publish_time");  // 组装排序条件

        // 查询条件
        String subjectId = courseQuery.getSubjectId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String teacherId = courseQuery.getTeacherId();
        String title = courseQuery.getTitle();

        if (!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("c.subject_parent_id",subjectParentId);
        }

        if (!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("c.subjectId",subjectId);
        }
        if (!StringUtils.isEmpty(teacherId)){
            queryWrapper.eq("c.teacherId",teacherId);
        }
        if (!StringUtils.isEmpty(title)){
        /* c mapper里定义 c 表  但是组装条件 在业务层 多个同名列名 需要写别名 避免歧义 */
            queryWrapper.like("c.title",title);
        }

        // Mapper查询 只能单个表
        // mybatis plus的mapper层 可以自动识别page对象
        // MP 可以帮我们自动组装 分页查询语句
        // 泛型 前面写了 后面可以不写 并且 后面不写泛型效率会高一些
        Page<CourseVo> pageParam = new Page<CourseVo>(page, limit);
        List<CourseVo> records = baseMapper.selectPageByCourseQuery(pageParam,queryWrapper); // 自动执行查询语句
        // 将条件records 放入 pageParam
        pageParam.setRecords(records);  // 将records 组装到 查询语句后面

        return pageParam;

    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCourseById(String id) {



        // 课时 video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",id);
        videoMapper.delete(videoQueryWrapper);

        // 章节 chapter
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",id);
        chapterMapper.delete(chapterQueryWrapper);

        // 评论 comment
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id",id);
        commentMapper.delete(commentQueryWrapper);

        // 收藏 collect
        QueryWrapper<CourseCollect> collectQueryWrapper = new QueryWrapper<>();
        collectQueryWrapper.eq("course_id",id);
        courseCollectMapper.delete(collectQueryWrapper);

        // 课程 course

        return this.removeById(id);
    }

    @Override
    public String getCourseStatusById(String id) {
        String result = courseCollectMapper.getCourseStatusById(id);
//        if ( "null".equals(result)){
//            return null;
//        }
//        if ("Draft".equals(result)){
//            return "Draft";
//        }
        return result;
    }


    /**
     * 方案一： 两个sql查询出CourseInfoForm的值
     * @param id
     * @return
     */
    public CourseInfoForm getCourseInfoFormById1(String id) {
        // 根据id查询course
        Course course = baseMapper.selectById(id);

        if (course == null) {
            return null;
        }

        // 根据id查询courseDescription
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);

        // 将course和courseDescription组装成courseInfoForm
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        // 使用工具类 直接将course拷贝到InfoForm
        BeanUtils.copyProperties(course,courseInfoForm);

        if (courseDescription != null){
            // InfoForm还有一个Description属性 使用
            courseInfoForm.setDescription(courseDescription.getDescription());
        }



        return courseInfoForm;
    }
}
