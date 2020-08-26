package com.guli.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.internal.$Gson$Preconditions;
import com.guli.service.base.result.R;
import com.guli.service.edu.entity.Teacher;
import com.guli.service.edu.entity.query.TeacherQuery;
import com.guli.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RequestMapping("/admin/edu/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;


    @ApiOperation("获取所有讲师列表") // @ApiOperation 写在方法上
    @GetMapping("list")
    public R list(){
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "根据ID删除讲师" , notes = "逻辑删除")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam("讲师ID") @PathVariable String id){
        // @ApiParam 写在参数上
        boolean b = teacherService.removeById(id);
        if(b){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("删除失败");
        }
    }



    @ApiOperation("分页查询讲师列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(
            @ApiParam(value = "当前页码",required = true) @PathVariable Long page,
            @ApiParam(value = "每页记录条数",required = true) @PathVariable Long limit,
            @ApiParam(value = "讲师列表查询对象",required = false)TeacherQuery teacherQuery){

//        Page<Teacher> pageParam = new Page<>(page, limit);
//        teacherService.page(pageParam);
        IPage<Teacher> teacherModel = teacherService.selectPage(page, limit, teacherQuery);
        return R.ok().data("pageModel",teacherModel);
    }



    @ApiOperation("新增讲师")
    @PostMapping("save")
    public R save(
            @ApiParam(value = "讲师列表对象",required = true)
            @RequestBody Teacher teacher){
        boolean b = teacherService.save(teacher);
        if (b){
            return R.ok().message("添加成功");
        }else{
            return R.error().message("添加失败");
        }
    }

    @ApiOperation("更新讲师")
    @PutMapping("update")
    public R update(
            @ApiParam(value = "更新讲师",required = true)
            @RequestBody Teacher teacher){      // teacher 需要包含id
        boolean b = teacherService.updateById(teacher);
        if (b){
            return R.ok().message("更新成功");
        }else{
            return R.error().message("更新失败");
        }
    }

    @ApiOperation("根据ID查询讲师")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(value = "查询讲师",required = true)@PathVariable String id){
        Teacher teacher = teacherService.getById(id);
        if (teacher != null){
            return R.ok().data("teacher",teacher);
        }else{
            return R.error().message("数据不存在");
        }
    }
}

