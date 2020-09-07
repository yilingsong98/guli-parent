package com.guli.service.edu.controller.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guli.service.base.result.R;
import com.guli.service.edu.entity.Teacher;
import com.guli.service.edu.entity.query.TeacherQuery;
import com.guli.service.edu.fegin.OssFileService;
import com.guli.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */


@CrossOrigin // 允许跨域请求策略
@Slf4j
@Api(tags = "讲师管理") // @Api 写在类头上
@RestController
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController {

    @Autowired
    private TeacherService teacherService;


    @ApiOperation("获取所有讲师列表") // @ApiOperation 写在方法上
    @GetMapping("list")
    public R list(){
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list);
    }


}

