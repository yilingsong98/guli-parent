package com.guli.service.base.exception;

import com.guli.service.base.result.ResultCodeEnum;
import lombok.Data;

@Data
public class GuliException extends RuntimeException {

    private Integer code; //错误码

    public GuliException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public GuliException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ",message=" + this.getMessage() +
                '}';
    }
}
