package com.xu.vueadmin.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xu.vueadmin.consts.CommonConst;
import com.xu.vueadmin.exception.CaptchaException;
import com.xu.vueadmin.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();

        if ("/login".equals(url) && "POST".equals(request.getMethod())) {
            try{
                // 校验验证码
                validate(request);
            } catch (CaptchaException e) {
                // 如果不正确，交给LoginFailureHandler处理
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }

        // 校验通过，放行
        filterChain.doFilter(request, response);
    }

    /**
     * 校验验证码
     * @param request
     */
    private void validate(HttpServletRequest request) {

        String code = request.getParameter("code");
        String key = request.getParameter("token");

        // 验证码为空
        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }

        // 验证码错误
        if (!code.equals(redisUtil.hget(CommonConst.CAPTCHA_KEY, key))) {
            throw new CaptchaException("验证码错误");
        }

        // 验证码只使用一次
        redisUtil.hdel(CommonConst.CAPTCHA_KEY, key);
    }


}
