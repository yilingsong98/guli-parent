package com.guli.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guli.service.edu.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.service.edu.entity.query.TeacherQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
public interface TeacherService extends IService<Teacher> {

    /**
     * 根据条件 分页查询
     * @param page          // 查询第几页
     * @param limit         // 每页分几个
     * @param teacherQuery  // 查询的条件 TeacherQuery ： 自己定义的查询条件类
     * @return
     */
    IPage<Teacher> selectPage(Long page, Long limit, TeacherQuery teacherQuery);

    int deleteBatchIds(List<String> list);

    List<Map<String, Object>> selectNameListByKey(String key);

    boolean removerAvatarById(String id);

}
