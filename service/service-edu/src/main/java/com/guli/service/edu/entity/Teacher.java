package com.guli.service.edu.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guli.service.base.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 讲师
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)        // 串连 实体类set的返回值变为实体类对象本身（不再是返回void）
@TableName("edu_teacher")
@ApiModel(value="Teacher对象", description="讲师")
public class Teacher extends BaseEntity{

    private static final long serialVersionUID=1L;

    //@ApiModelProperty(value = "讲师姓名",example = "helen") example : 举例
    @ApiModelProperty(value = "讲师姓名")
    private String name;

    @ApiModelProperty(value = "讲师简介")
    private String intro;

    @ApiModelProperty(value = "讲师资历,一句话说明讲师")
    private String career;

    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer level;

    @ApiModelProperty(value = "讲师头像")
    private String avatar;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "入驻时间",example = "1970-01-01")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date joinDate;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField("is_deleted")  // 指定 属性daleted 映射到 表中的 is_deleted 字段
    @TableLogic
    private Boolean deleted;


}
