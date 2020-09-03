package com.guli.service.edu.service;

import com.guli.service.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.service.edu.entity.form.CourseInfoForm;

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
}
