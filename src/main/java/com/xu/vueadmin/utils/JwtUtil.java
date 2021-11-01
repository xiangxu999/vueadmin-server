package com.xu.vueadmin.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Description JWT工具类
 * Date 2021/11/1 10:22
 * Version 1.0.1
 *
 * @author Wen
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    /**
     * 过期时间
     */
    private long expire;

    /**
     * 签名密钥
     */
    private String secret;

    /**
     * 匿名
     */
    private String header;


    /**
     * 生成jwt
     * @param username
     * @return
     */
    public String generateToken(String username) {

        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                // 7天过期
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 解析jwt
     * @param jwt
     * @return
     */
    public Claims getClaimByToken(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * jwt是否过期
     * @param claims
     * @return
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }



}
