package com.xu.vueadmin.security;

import cn.hutool.json.JSONUtil;
import com.xu.vueadmin.common.Result;
import com.xu.vueadmin.enums.LoginEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Description 自定义失败跳转
 * Date 2021/10/30 15:40
 * Version 1.0.1
 *
 * @author Wen
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException
    {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        String errorMsg = "";
        // 验证码部分
        if (LoginEnum.ERROR_CAPTCHA.getDesc().equals(exception.getMessage())) {
            errorMsg = LoginEnum.ERROR_CAPTCHA.getDesc();
        } else {
            // 账号或密码错误
            errorMsg = LoginEnum.ERROR_LOGIN.getDesc();
        }

        Result result = Result.fail(errorMsg);

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        outputStream.flush();
        outputStream.close();

    }
}
