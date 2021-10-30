package com.xu.vueadmin.enums;

import lombok.Getter;

/**
 * Description 操作枚举
 * Date 2021/10/30 9:48
 * Version 1.0.1
 *
 * @author Wen
 */
@Getter
public enum ActionEnum {

    /**
     * 成果操作
     */
    SUCCESS_ACTION(200, "操作成功"),

    /**
     * 失败操作
     */
    FAIL_ACTION(400, "操作失败");

    /**
     * 操作编码
     */
    Integer code;

    /**
     * 操作描述
     */
    String desc;

    ActionEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
