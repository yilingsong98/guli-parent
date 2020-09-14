package com.guli.service.edu.controller.api;

import com.guli.service.base.result.R;
import com.guli.service.edu.entity.Course;
import com.guli.service.edu.entity.query.WebCourseQuery;
import com.guli.service.edu.entity.vo.ChapterVo;
import com.guli.service.edu.entity.vo.WebCourseVo;
import com.guli.service.edu.service.ChapterService;
import com.guli.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin
@Api(tags = "课程")
@RestController
@RequestMapping("/api/edu/course")
public class ApiCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    @ApiOperation("课程列表")
    @GetMapping("list")
    public R list(
            @ApiParam(value = "查询对象", required = false)
                    WebCourseQuery webCourseQuery) {

        List<Course> courseList =  courseService.webSelectList(webCourseQuery);
        return R.ok().data("courseList",courseList);
    }

    @ApiOperation("根据id查询课程信息")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(value = "课程id" , required = true)
            @PathVariable String id){


        // 课时基本信息
        WebCourseVo webCourseVo = courseService.getWebCourseVoById(id);

        // 大纲信息
        List<ChapterVo> chapterVoList = chapterService.nestedListById(id);

        return R.ok().data("course",webCourseVo).data("chapterVoList",chapterVoList);
    }


}