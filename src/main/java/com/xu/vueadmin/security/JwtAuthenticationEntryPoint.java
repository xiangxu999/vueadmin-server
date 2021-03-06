package com.xu.vueadmin.security;

import cn.hutool.json.JSONUtil;
import com.xu.vueadmin.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Description Todo
 * Date 2021/11/1 17:05
 * Version 1.0.1
 *
 * @author Wen
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ServletOutputStream outputStream = response.getOutputStream();
        Result result = Result.fail("请先登录");

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        outputStream.flush();
        outputStream.close();
    }
}
