package com.guli.service.edu.service;

import com.guli.service.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.service.edu.entity.vo.SubjectVo;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
public interface SubjectService extends IService<Subject> {

    void batchImport(InputStream inputStream);

    List<SubjectVo> nestedList();

}
