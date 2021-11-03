package com.xu.vueadmin.controller;


import cn.hutool.core.map.MapUtil;
import com.xu.vueadmin.Vo.SysMenuVo;
import com.xu.vueadmin.common.Result;
import com.xu.vueadmin.pojo.SysUser;
import com.xu.vueadmin.service.impl.SysMenuServiceImpl;
import com.xu.vueadmin.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 旭日
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private SysMenuServiceImpl sysMenuService;

    @RequestMapping(value = "/nav", method = RequestMethod.GET)
    public Result nav(Principal principal) {
        SysUser sysUser = sysUserService.getByUsername(principal.getName());

        // 获取权限信息
        String userAuthorityInfo = sysUserService.getUserAuthorityInfo(sysUser.getId());
        String[] authorities = StringUtils.tokenizeToStringArray(userAuthorityInfo, ",");

        // 获取对应的菜单
        List<SysMenuVo> nav = sysMenuService.getCurrentUserNav();

        return Result.success(MapUtil.builder()
                .put("authorities",authorities)
                .put("nav",nav).map());
    }
}
