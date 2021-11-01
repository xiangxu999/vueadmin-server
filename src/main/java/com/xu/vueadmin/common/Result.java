package com.xu.vueadmin.common;

import com.xu.vueadmin.enums.ActionEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * Description 公用返回对象
 * Date 2021/10/30 9:40
 * Version 1.0.1
 *
 * @author Wen
 */
@Data
public class Result implements Serializable {

    private int code;

    private String msg;

    private Object data;

    public static Result success (Object data) {
        return new Result(ActionEnum.SUCCESS_ACTION.getCode(), ActionEnum.SUCCESS_ACTION.getDesc(), data);
    }

    public static Result fail (String message) {
        return new Result(ActionEnum.FAIL_ACTION.getCode(), message);
    }


    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
