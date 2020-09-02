package com.guli.service.edu.controller.admin;


import com.guli.service.base.result.R;
import com.guli.service.edu.entity.form.CourseInfoForm;
import com.guli.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
@Api(tags = "课程管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation("保存课程基本信息")
    @PostMapping("save-course-info")
    public R saveCourseInfo(
            @ApiParam(value = "课程基本信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm){


        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId",courseId).message("保存成功");
    }


    @ApiOperation("根据课程id显示课程信息")
    @GetMapping("course-info/{id}")
    public R getById(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String id){
        CourseInfoForm courseInfoForm = courseService.getCourseInfoFormById(id);
        if (courseInfoForm != null) {
            return R.ok().data("item",courseInfoForm);
        } else {
            return R.error().message("数据不存在");
        }

    }

}

