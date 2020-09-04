package com.guli.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guli.service.base.result.R;
import com.guli.service.edu.entity.form.CourseInfoForm;
import com.guli.service.edu.entity.query.CourseQuery;
import com.guli.service.edu.entity.vo.CourseVo;
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

    @ApiOperation("更新课程基本信息")
    @PutMapping("update-course-info")
    public R updateCourseInfoById(
            @ApiParam(value = "课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm){

        courseService.updateCourseInfoById(courseInfoForm);
        return R.ok().message("修改成功");

    }

    @ApiOperation("分页课程列表")
    @GetMapping("list/{page}/{limit}")
    public R list(@ApiParam(value = "当前页码",required = true) @PathVariable Long page,
                  @ApiParam(value = "每页记录数",required = true) @PathVariable Long limit,
                  @ApiParam(value = "课程列表查询对象",required = false)CourseQuery courseQuery){

        IPage<CourseVo> courseModel = courseService.selectPage(page,limit,courseQuery);
        return R.ok().data("pageModel",courseModel);
    }



    @ApiOperation("根据课程id删除课程")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String id){

        // 如果课程已发布 不让删除
        String res = courseService.getCourseStatusById(id);

        if ("null".equals(res)) {

        }
        if (!"Draft".equals(res)){
            return R.error().message("课程已发布，删除失败");
        }


        boolean result = courseService.removeCourseById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }


}

