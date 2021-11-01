package com.xu.vueadmin.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Description 验证码异常
 * Date 2021/10/31 20:28
 * Version 1.0.1
 *
 * @author Wen
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg) {
        super(msg);
    }
}
