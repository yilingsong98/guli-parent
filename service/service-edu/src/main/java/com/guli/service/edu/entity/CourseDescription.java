package com.guli.service.edu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guli.service.base.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程简介
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_course_description")
@ApiModel(value="CourseDescription对象", description="课程简介")
public class CourseDescription extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id" ,type = IdType.NONE) // 主键策略（IdType.NONE）没有策略
    private String id;  // 没有策略 所以 我们主动去setid 根据Course.setId

    @ApiModelProperty(value = "课程简介")
    private String description;


}
