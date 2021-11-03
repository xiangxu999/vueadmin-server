package com.xu.vueadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xu.vueadmin.pojo.SysMenu;
import com.xu.vueadmin.pojo.SysRole;
import com.xu.vueadmin.pojo.SysUser;
import com.xu.vueadmin.mapper.SysUserMapper;
import com.xu.vueadmin.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.vueadmin.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 旭日
 * @since 2021-10-30
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysRoleServiceImpl sysRoleService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysMenuServiceImpl sysMenuService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        SysUser sysUser = userMapper.selectById(userId);

        // ROLE_admin system:user:list
        String authority = "";

        if (redisUtil.hasKey("GrantedAuthority" + sysUser.getUsername())) {
            authority = (String)redisUtil.get("GrantedAuthority" + sysUser.getUsername());
        } else {
            // 获取角色
            List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id = " + userId));
            if (roles.size() > 0) {
                String roleCodes = roles.stream().map(code -> "ROLE_" + code.getCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }
            // 获取菜单操作编码
            List<Long> menuIds = userMapper.getNavMenuIds(userId);
            if (menuIds.size() > 0) {
                List<SysMenu> sysMenus = sysMenuService.listByIds(menuIds);
                String menuPerms = sysMenus.stream().map(SysMenu::getPerms).collect(Collectors.joining(","));
                authority = authority.concat(menuPerms);
            }
            redisUtil.set("GrantedAuthority" + sysUser.getUsername(), authority, 60 * 60);
        }
        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("GrantedAuthority" + username);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
    //    SELECT * FROM `sys_user` WHERE sys_user.id in (SELECT user_id FROM sys_user_role WHERE role_id = 3)
        List<SysUser> list = this.list(new QueryWrapper<SysUser>().inSql("id", "SELECT user_id FROM sys_user_role  WHERE role_id = " + roleId));
        list.forEach(e -> clearUserAuthorityInfo(e.getUsername()));
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<SysUser> sysUsers =  userMapper.listByMenuId(menuId);
        sysUsers.forEach(e -> clearUserAuthorityInfo(e.getUsername()));
    }
}
