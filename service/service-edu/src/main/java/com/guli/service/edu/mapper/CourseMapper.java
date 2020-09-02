package com.guli.service.edu.mapper;

import com.guli.service.edu.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.service.edu.entity.form.CourseInfoForm;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
public interface CourseMapper extends BaseMapper<Course> {

    CourseInfoForm selectCourseInfoFormById(String id);
}
