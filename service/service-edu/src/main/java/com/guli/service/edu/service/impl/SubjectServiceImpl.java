package com.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.guli.service.edu.entity.Subject;
import com.guli.service.edu.entity.excel.ExcelSubjectData;
import com.guli.service.edu.entity.excel.ExcelSubjectDataListener;
import com.guli.service.edu.entity.vo.SubjectVo;
import com.guli.service.edu.mapper.SubjectMapper;
import com.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author donkey
 * @since 2020-08-25
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchImport(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelSubjectData.class,
                new ExcelSubjectDataListener(baseMapper)).excelType(ExcelTypeEnum.XLS).sheet().doRead();

    }

    @Override
    public List<SubjectVo> nestedList() {
        return baseMapper.selectNestedListByParentId("0");
    }
}
