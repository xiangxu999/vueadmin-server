package com.xu.vueadmin.controller;

import com.xu.vueadmin.common.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description Todo
 * Date 2021/11/1 18:09
 * Version 1.0.1
 *
 * @author Wen
 */
@RestController
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Result test() {
        return Result.success("测试方法");
    }
}
