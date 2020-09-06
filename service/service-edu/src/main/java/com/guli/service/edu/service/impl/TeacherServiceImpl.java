package com.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.guli.service.base.result.R;
import com.guli.service.edu.entity.Teacher;
import com.guli.service.edu.entity.query.TeacherQuery;
import com.guli.service.edu.fegin.OssFileService;
import com.guli.service.edu.mapper.TeacherMapper;
import com.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private OssFileService ossFileService;

    @Override
    public IPage<Teacher> selectPage(Long page, Long limit, TeacherQuery teacherQuery) {
        
        // 组装分页对象
        Page<Teacher> pageParam = new Page<Teacher>(page,limit);

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String joinDateBegin = teacherQuery.getJoinDateBegin();
        String joinDateEnd = teacherQuery.getJoinDateEnd();

        // 组装查询条件
        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);         // 注意：like 会使索引失效 %name% 和 %name
        }
        if (!StringUtils.isEmpty(joinDateBegin)){
            queryWrapper.ge("join_date",joinDateBegin);
        }
        if (!StringUtils.isEmpty(joinDateEnd)){
            queryWrapper.le("join_date",joinDateEnd);
        }
        if (level != null){
            queryWrapper.eq("level",level);
        }

        // 执行查询返回结果
        // selectPage(参数一：查询的参数,参数二：查询的组合条件)
        return baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public int deleteBatchIds(List<String> list) {
        return baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<Map<String, Object>> selectNameListByKey(String key) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name");
        queryWrapper.likeRight("name", key);

        List<Map<String, Object>> list = baseMapper.selectMaps(queryWrapper);//返回值是Map列表
        return list;
    }

    @Override
    public boolean removerAvatarById(String id) {
        Teacher teacher = baseMapper.selectById(id);
        if( teacher != null){
            String avatar = teacher.getAvatar();
            if (!StringUtils.isEmpty(avatar)){
                // 删除图片 调用client方法
                R r = ossFileService.removeFile(avatar);
                return r.getSuccess();
            }
        }

        return false;
    }

}
