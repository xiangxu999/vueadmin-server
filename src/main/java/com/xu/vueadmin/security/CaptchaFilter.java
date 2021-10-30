package com.xu.vueadmin.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description 自定义验证码校验
 * Date 2021/10/30 16:15
 * Version 1.0.1
 *
 * @author Wen
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();

        if ("/login".equals(url) && "post".equals(request.getMethod())) {

            // 校验验证码


            // 如果不正确，交给LoginFailureHandler处理
        }
    }
}
