package com.xu.vueadmin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.vueadmin.common.Result;
import com.xu.vueadmin.consts.CommonConst;
import com.xu.vueadmin.pojo.SysRole;
import com.xu.vueadmin.pojo.SysUser;
import com.xu.vueadmin.pojo.SysUserRole;
import com.xu.vueadmin.service.impl.SysRoleServiceImpl;
import com.xu.vueadmin.service.impl.SysUserRoleServiceImpl;
import com.xu.vueadmin.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 旭日
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController {

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private SysUserRoleServiceImpl sysUserRoleService;

    @Autowired
    private SysRoleServiceImpl sysRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 获得用户列表
     * @return Result
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result list(@RequestParam(value = "current", defaultValue = "1") Integer current,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       @RequestParam(value = "username", defaultValue = "") String username) {
        Page<SysUser> page = new Page<>(current, size);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq(StringUtils.isNotBlank(username),"username", username);
        Page<SysUser> result = sysUserService.page(page, queryWrapper);
        result.getRecords().forEach(sysUser -> {
            List<SysUserRole> sysUserRoles = sysUserRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", sysUser.getId()));
            if (sysUserRoles.size() > 0) {
                List<Long> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                List<SysRole> roles = sysRoleService.listByIds(roleIds);
                sysUser.setRoles(roles);
            }
        });
        if (result.getRecords().size() > 0) {
            return Result.success(result);
        } else {
            return Result.fail();
        }
    }

    /**
     * 获得单条的用户信息
     * @return Reuslt
     */
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result info(@PathVariable(value = "id") Long id) {
        SysUser result = sysUserService.getById(id);
        List<SysUserRole> sysUserRoles = sysUserRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", id));
        if (sysUserRoles.size() > 0) {
            List<Long> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            List<SysRole> roles = sysRoleService.listByIds(roleIds);
            result.setRoles(roles);
        }
        return Result.success(result);
    }

    /**
     * 修改一条用户信息
     * @param sysUser 用户实体
     * @return Reuslt
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('system:user:update')")
    public Result update(@RequestBody SysUser sysUser) {
        boolean result = sysUserService.updateById(sysUser);
        if (result) {
            sysUserService.clearUserAuthorityInfo(sysUser.getUsername());
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /**
     * 新增一条用户信息
     * @param sysUser 用户实体
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('system:user:save')")
    public Result save(@RequestBody SysUser sysUser) {
        // 设置用户的默认密码
        String encode = passwordEncoder.encode(CommonConst.USER_PASSWORD);
        sysUser.setPassword(encode);
        // 设置默认头像
        sysUser.setAvatar("https://cdn.lixingyong.com/2021/01/15/QQ20210115152209.jpg");
        boolean result = sysUserService.save(sysUser);
        if (result) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /**
     * 根据ids删除对应的用户
     * @param ids 用户的id
     * @return Result
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('system:user:delete')")
    public Result deleteByIds(@RequestBody Long[] ids) {
        sysUserService.removeByIds(Arrays.asList(ids));
        boolean result = sysUserService.removeByIds(Arrays.asList(ids));
        if (result) {
            List<SysUser> sysUsers = sysUserService.listByIds(Arrays.asList(ids));
            sysUsers.forEach(sysUser -> {
                sysUserService.clearUserAuthorityInfo(sysUser.getUsername());
            });
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('system:user:save')")
    public Result roleChange(@PathVariable(value = "id") Long id, @RequestBody Long[] roleIds) {
       List<SysUserRole> sysUserRoles = new ArrayList<>();

       Arrays.stream(roleIds).forEach(roleId -> {
           SysUserRole sysUserRole = new SysUserRole();
           sysUserRole.setUserId(id);
           sysUserRole.setRoleId(roleId);
           sysUserRoles.add(sysUserRole);
       });

        // 删除之前的记录
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", id));

        // 添加记录
        sysUserRoleService.saveBatch(sysUserRoles);

        // 删除缓存
        SysUser sysUser = sysUserService.getById(id);
        sysUserService.clearUserAuthorityInfo(sysUser.getUsername());

        return Result.success();
    }

    @RequestMapping(value = "/repass", method = RequestMethod.POST)
    public Result repass(@RequestBody Long id) {
        SysUser sysUser = sysUserService.getById(id);
        String encode = passwordEncoder.encode(CommonConst.USER_PASSWORD);
        sysUser.setPassword(encode);
        sysUserService.updateById(sysUser);
        return Result.success();
    }




}
