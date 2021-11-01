package com.xu.vueadmin.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.xu.vueadmin.common.Result;
import com.xu.vueadmin.consts.CommonConst;
import com.xu.vueadmin.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Description 权限控制层
 * Date 2021/10/30 11:40
 * Version 1.0.1
 *
 * @author Wen
 */
@RestController
public class AuthController {

    @Autowired
    private Producer producer;
    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public Result captcha() throws IOException {

        // 唯一标识
        String key = UUID.randomUUID().toString();
        // 图片的code
        String code = producer.createText();

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        // 前缀
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encode(byteArrayOutputStream.toByteArray());

        // 验证码code放入redis
        redisUtil.hset(CommonConst.CAPTCHA_KEY, key, code, 120);

        return Result.success(MapUtil.builder()
                .put("token", key)
                .put("captchaImg", base64Img).build());

    }
}
