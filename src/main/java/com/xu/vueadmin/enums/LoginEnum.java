package com.xu.vueadmin.enums;

import lombok.Getter;

/**
 * Description 登录的状态枚举类
 * Date 2021/11/1 20:40
 * Version 1.0.1
 *
 * @author Wen
 */
@Getter
public enum LoginEnum {

    /**
     * 验证码错误
     */
    ERROR_CAPTCHA(400, "验证码错误"),

    /**
     * 用户名不存在
     */
    ERROR_LOGIN(400, "账户名或密码错误");

    /**
     * 操作编码
     */
    Integer code;

    /**
     * 操作描述
     */
    String desc;

    LoginEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
