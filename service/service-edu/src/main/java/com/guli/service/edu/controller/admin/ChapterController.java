package com.guli.service.edu.controller.admin;


import com.guli.service.base.result.R;
import com.guli.service.edu.entity.Chapter;
import com.guli.service.edu.entity.vo.ChapterVo;
import com.guli.service.edu.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
@Api(tags = "章节管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @ApiOperation("新增章节")
    @PostMapping("save")
    public R save(
            @ApiParam(value = "章节对象",required = true)
            @RequestBody Chapter chapter){

        boolean result = chapterService.save(chapter);
        if (result) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }

    }

    // 删除
    @ApiOperation("根据id删除章节")
    @DeleteMapping("removeById/{id}")
    public R removeById(
            @ApiParam(value = "删除id",required = true)
            @PathVariable String id){
        // 分布式项目 级联 影响增删改查性能
        // 阿里规范 分布式项目 禁止使用级联 一切外键概率都在业务层解决
        boolean result = chapterService.removeChapterById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    // 修改
    @ApiOperation("根据id修改章节")
    @PutMapping("updateById")
    public R updateById(@ApiParam(value = "修改id",required = true)
                        @RequestBody Chapter chapter){
        boolean result = chapterService.updateById(chapter);
        if (result) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    // 回显 根据id查询一条
    @ApiOperation("根据id显示章节")
    @GetMapping("getById/{id}")
    public R getById(@ApiParam(value = "根据id查询语句",required = true)
                         @PathVariable String id){
        Chapter chapter = chapterService.getById(id);
        if (chapter != null){
            return R.ok().data("item",chapter);
        } else {
            return R.error().message("数据不存在");
        }
    }


    @ApiOperation("嵌套章节列表")
    @GetMapping("nested-list/{courseId}")
    public R nestedListByCourseId(
            @ApiParam(value = "课程id", required =  true)
            @PathVariable String courseId){

        List<ChapterVo> chapterVoList = chapterService.nestedListById(courseId);
        return R.ok().data("items",chapterVoList);
    }

}

