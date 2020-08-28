package com.guli.service.edu.controller.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.service.base.result.R;
import com.guli.service.edu.entity.Teacher;
import com.guli.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Api(tags = "讲师管理") // @Api 写在类头上
@RestController
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("获取所有讲师列表") // @ApiOperation 写在方法上
    @GetMapping("list")
    public List<Teacher> list(){
        return teacherService.list();
    }

    @ApiOperation(value = "根据ID删除讲师" , notes = "逻辑删除")
    @DeleteMapping("remove/{id}")
    public boolean removeById(@ApiParam("讲师ID") @PathVariable String id){
        // @ApiParam 写在参数上
        return teacherService.removeById(id);
    }



    @ApiOperation("分页查询讲师列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(
            @ApiParam(value = "当前页码",required = true) @PathVariable Long page,
            @ApiParam(value = "每页记录条数",required = true) @PathVariable Long limit){


        Page<Teacher> pageParam = new Page<>(page, limit);
        teacherService.page(pageParam);
        return R.ok().data("pageModel",pageParam);
    }



}