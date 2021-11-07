package com.xu.vueadmin.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xu.vueadmin.Vo.SysMenuVo;
import com.xu.vueadmin.common.Result;
import com.xu.vueadmin.pojo.SysMenu;
import com.xu.vueadmin.pojo.SysRoleMenu;
import com.xu.vueadmin.pojo.SysUser;
import com.xu.vueadmin.service.impl.SysMenuServiceImpl;
import com.xu.vueadmin.service.impl.SysRoleMenuServiceImpl;
import com.xu.vueadmin.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private SysRoleMenuServiceImpl sysRoleMenuService;

    /**
     * 获得侧边栏
     * @param principal principal
     * @return Result
     */
    @RequestMapping(value = "/nav", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('system:menu:list')")
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
    @PreAuthorize("hasAuthority('system:menu:list')")
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
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result info(@PathVariable(value = "id") Long id) {
        SysMenu result = sysMenuService.getById(id);
        if (result != null) {
            return Result.success(result);
        } else {
            return Result.fail();
        }
    }


    /**
     * 修改一条菜单信息
     * @param sysMenu 菜单实体
     * @return Result
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('system:menu:update')")
    public Result update(@RequestBody SysMenu sysMenu) {
        boolean result = sysMenuService.updateById(sysMenu);
        if (result) {
            // 清除缓存
            sysUserService.clearUserAuthorityInfoByMenuId(sysMenu.getId());
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
    @PreAuthorize("hasAuthority('system:menu:save')")
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
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public Result delete(@PathVariable(value = "id") Long id) {
        int count = sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id",id));
        if (count > 0) {
            return Result.fail("请先删除子菜单");
        }
        boolean result = sysMenuService.removeById(id);
        if (result) {
            // 清除缓存
            sysUserService.clearUserAuthorityInfoByMenuId(id);
            // 同步删除中间关联表
            sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id",id));
            return Result.success();
        } else {
            return Result.fail();
        }
    }




}
