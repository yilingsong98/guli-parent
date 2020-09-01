package com.guli.service.edu.mapper;

import com.guli.service.edu.entity.Subject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.service.edu.entity.vo.SubjectVo;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
public interface SubjectMapper extends BaseMapper<Subject> {


    List<SubjectVo> selectNestedListByParentId(String parentId);
}
