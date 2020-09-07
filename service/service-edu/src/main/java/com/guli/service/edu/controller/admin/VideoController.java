package com.guli.service.edu.controller.admin;


import com.guli.service.base.result.R;
import com.guli.service.edu.entity.Video;
import com.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
@CrossOrigin
@Api(tags = "课时管理")
@RestController
@RequestMapping("/admin/edu/video")
@Slf4j
public class VideoController {
    @Autowired
    private VideoService videoService;

    // 增
    @ApiOperation("新增课时")
    @PostMapping("save")
    public R save(
            @ApiParam(value="课时对象", required = true)
            @RequestBody Video video){
        boolean result = videoService.save(video);
        if (result) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }

    // 删
    @ApiOperation("删除课程")
    @DeleteMapping("remove/{id}")
    public R remove(@ApiParam(value = "课时对象", required = true)
                    @PathVariable String id){
        boolean result = videoService.removeById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    // 改
    @ApiOperation("修改课程")
    @PutMapping("update")
    public R update(@ApiParam(value = "课时对象", required = true)
                    @RequestBody Video video){
        boolean result = videoService.updateById(video);
        if (result) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

    // 查
    @ApiOperation("查询课程")
    @GetMapping("get/{id}")
    public R get(@ApiParam(value = "课时对象",required = true)
                 @PathVariable String id){
        Video video = videoService.getById(id);
        if (video != null){
            return R.ok().data("item",video);
        }else{
            return R.error().message("未找到课时");
        }
    }


    // 删课程视频
    @ApiOperation("删除课程视频")
    @DeleteMapping("removeById/{id}")
    public R removeById(@ApiParam(value = "课时对象", required = true)
                    @PathVariable String id){
        boolean result = videoService.removeMediaVideoById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

}

