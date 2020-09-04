package com.guli.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guli.service.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.service.edu.entity.form.CourseInfoForm;
import com.guli.service.edu.entity.query.CourseQuery;
import com.guli.service.edu.entity.vo.CourseVo;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
public interface CourseService extends IService<Course> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoFormById(String id);

    boolean updateCourseInfoById(CourseInfoForm courseInfoForm);

    IPage<CourseVo> selectPage(Long page, Long limit, CourseQuery courseQuery);

    boolean removeCourseById(String id);

    /* 通过id获取该课程状态 */
    String getCourseStatusById(String id);
}
