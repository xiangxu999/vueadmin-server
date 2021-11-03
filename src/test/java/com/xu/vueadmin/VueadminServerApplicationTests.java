package com.xu.vueadmin;

import com.xu.vueadmin.mapper.SysUserMapper;
import com.xu.vueadmin.pojo.SysUser;
import com.xu.vueadmin.service.impl.SysUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class VueadminServerApplicationTests {

    @Autowired
    SysUserServiceImpl sysUserService;

    @Autowired
    SysUserMapper userMapper;

    @Test
    void contextLoads() {
    }

}
