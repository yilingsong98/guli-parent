package com.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@ApiModel("课程章节对象")
@Data
public class ChapterVo {

    @ApiModelProperty(value = "章节ID")
    private String id;
    @ApiModelProperty(value = "章节标题")
    private String title;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "章节下的课时列表")
    // 章节下面 包含多个 视频（小节）
    private List<VideoVo> children = new ArrayList<>();

}
