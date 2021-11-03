package com.xu.vueadmin.service;

import com.xu.vueadmin.pojo.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 旭日
 * @since 2021-10-30
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 根据用户名返回用户对象
     * @param username 用户名
     * @return 用户对象
     */
    SysUser getByUsername(String username);

    /**
     * 根据用户id获取对应的权限信息
     * @param userId 用户id
     * @return 权限信息
     */
    String getUserAuthorityInfo(Long userId);

    /**
     * 用户更改的时候清除缓存
     * @param username 用户名
     */
    void clearUserAuthorityInfo(String username);


    /**
     * 角色更改的时候清除缓存
     * @param roleId 角色id
     */
    void clearUserAuthorityInfoByRoleId(Long roleId);

    /**
     * 菜单更改的时候清除缓存
     * @param menuId
     */
    void clearUserAuthorityInfoByMenuId(Long menuId);
}
