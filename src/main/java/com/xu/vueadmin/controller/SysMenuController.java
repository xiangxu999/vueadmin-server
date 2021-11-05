package com.xu.vueadmin.controller;


import cn.hutool.core.map.MapUtil;
import com.xu.vueadmin.Vo.SysMenuVo;
import com.xu.vueadmin.common.Result;
import com.xu.vueadmin.pojo.SysMenu;
import com.xu.vueadmin.pojo.SysUser;
import com.xu.vueadmin.service.impl.SysMenuServiceImpl;
import com.xu.vueadmin.service.impl.SysUserServiceImpl;
import com.xu.vueadmin.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获得侧边栏
     * @param principal principal
     * @return Result
     */
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

    /**
     * 获得导航栏
     * @return Result
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list() {
        List<SysMenu> menus = sysMenuService.list();
        List<SysMenu> treeMenus = sysMenuService.buildTreeMenu(menus);
        return Result.success(treeMenus);
    }

    /**
     * 获得单条的菜单信息
     * @return Reuslt
     */
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public Result info(@PathVariable(value = "id") Long id) {
        SysMenu menu = sysMenuService.getById(id);
        return Result.success(menu);
    }

    /**
     * 修改一条菜单信息
     * @return Reuslt
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody SysMenu sysMenu) {
        boolean result = sysMenuService.updateById(sysMenu);
        if (result) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /**
     * 添加一条菜单信息
     * @param sysMenu 菜单实体
     * @return Reuslt
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody SysMenu sysMenu) {
        boolean result = sysMenuService.save(sysMenu);
        if (result) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /**
     * 根据id删除对应的菜单
     * @param id 菜单的id
     * @return Result
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public Result delete(@PathVariable(value = "id") Long id) {
        boolean result = sysMenuService.removeById(id);
        if (result) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }




}
