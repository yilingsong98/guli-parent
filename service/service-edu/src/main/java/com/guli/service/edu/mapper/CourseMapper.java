package com.guli.service.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.service.edu.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.service.edu.entity.form.CourseInfoForm;
import com.guli.service.edu.entity.vo.CourseVo;
import org.apache.ibatis.annotations.Param;


import java.util.List;

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

    List<CourseVo> selectPageByCourseQuery(
            Page<CourseVo> pageParam,       // page对象 可以自动被mapper识别
            @Param(Constants.WRAPPER) QueryWrapper<CourseVo> queryWrapper); // @param 注解 对 queryWrapper
    // 进行标注 实际上WPAPPER = "ew"  xml文件识别到一个参数名为ew 会自动将此部分内容组装到where语句中
}
