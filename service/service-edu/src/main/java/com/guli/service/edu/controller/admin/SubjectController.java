package com.guli.service.edu.controller.admin;


import com.guli.service.base.exception.GuliException;
import com.guli.service.base.result.R;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.edu.entity.Subject;
import com.guli.service.edu.entity.vo.SubjectVo;
import com.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Api(tags = "课程类别管理")
@RestController
//@CrossOrigin // 跨域
@RequestMapping("/admin/edu/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation("Excel批量导入课程类别")
    @PostMapping("import")
    public R batchImport(@ApiParam(value = "Excel文件",required = true)
                         @RequestParam("file")MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("文件批量导入成功");
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }


    // 返回给前端一个分类列表
    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("nested-list")
    public R nestedList(){
        List<SubjectVo> subjectVoList = subjectService.nestedList();
        return R.ok().data("items", subjectVoList);
    }

}
