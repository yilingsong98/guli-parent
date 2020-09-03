package com.guli.service.edu.service.impl;

import com.guli.service.edu.entity.Course;
import com.guli.service.edu.entity.CourseDescription;
import com.guli.service.edu.entity.form.CourseInfoForm;
import com.guli.service.edu.mapper.CourseDescriptionMapper;
import com.guli.service.edu.mapper.CourseMapper;
import com.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
