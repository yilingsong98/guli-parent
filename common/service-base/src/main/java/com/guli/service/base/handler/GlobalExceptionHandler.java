package com.guli.service.base.handler;

import com.guli.service.base.result.R;
import com.guli.service.base.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody // 以json形式给前端
    @ExceptionHandler(Exception.class) // 此注解 可以捕获到 所有controller的异常
    public R error(Exception e){
//        e.printStackTrace();
        log.error(ExceptionUtils.getStackTrace(e));
        return R.error();
    }

    @ResponseBody // 以json形式给前端
    @ExceptionHandler(BadSqlGrammarException.class) // 此注解 可以捕获到 所有controller的异常
    public R error(BadSqlGrammarException e){
        //e.printStackTrace();
        log.error(ExceptionUtils.getStackTrace(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException e){
//        e.printStackTrace();
        log.error(ExceptionUtils.getStackTrace(e));
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }

    // 自定义异常处理

}
