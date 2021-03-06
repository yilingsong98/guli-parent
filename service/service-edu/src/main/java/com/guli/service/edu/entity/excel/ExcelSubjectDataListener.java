package com.guli.service.edu.entity.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.service.edu.entity.Subject;
import com.guli.service.edu.mapper.SubjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    //   @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        System.out.println("subjectMapper = " + subjectMapper);
        System.out.println(excelSubjectData);

        String levelOneTitle = excelSubjectData.getLevelOneTitle();
        // 判断一级类别是否存在
        Subject subjectLevelOneDB = this.getSubjectLevelOneByTitle(levelOneTitle);

        // 一级类别的id
        String parentId = null;

        if (subjectLevelOneDB == null){
            // 获取一级类别 并插入一级类别
            Subject subjectOneLevel = new Subject();
            subjectOneLevel.setParentId("0");
            subjectOneLevel.setTitle(levelOneTitle);
            subjectOneLevel.setSort(0);
            subjectMapper.insert(subjectOneLevel);


            parentId = subjectOneLevel.getId();
        }else{
            parentId = subjectLevelOneDB.getId();
        }


        // 判断二级类别是否存在
        String levelTwoTitle = excelSubjectData.getLevelTwoTitle();
        Subject subjectTwoLevelDB = this.getSubjectLevelTwoByTitle(levelTwoTitle,parentId);

        if (subjectTwoLevelDB == null) {
            // 二级类别
            Subject subjectTwoLevel = new Subject();
            subjectTwoLevel.setParentId(parentId);
            subjectTwoLevel.setTitle(levelTwoTitle);
            subjectTwoLevel.setSort(0);
            subjectMapper.insert(subjectTwoLevel);
        }


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    // 判断一级类别 是否存在
    private Subject getSubjectLevelOneByTitle(String title){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        // 一级类别的 父id 为0
        queryWrapper.eq("parent_id","0");
        return subjectMapper.selectOne(queryWrapper);
    }

    // 判断二级类别 是否存在
    private Subject getSubjectLevelTwoByTitle(String title,String parentId){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        // 一级类别的 父id 为0
        queryWrapper.eq("parent_id",parentId); // 在相同的一级类别下
        return subjectMapper.selectOne(queryWrapper);
    }

    // 如果传入 parentId = 0 则这是个一级类别
    private Subject getSubjectTitle(String title,String parentId){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        // 一级类别的 父id 为0
        queryWrapper.eq("parent_id",parentId); // 在相同的一级类别下
        return subjectMapper.selectOne(queryWrapper);
    }

}
