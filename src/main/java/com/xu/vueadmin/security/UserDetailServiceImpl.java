package com.xu.vueadmin.security;

import com.xu.vueadmin.enums.LoginEnum;
import com.xu.vueadmin.pojo.SysUser;
import com.xu.vueadmin.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description 自定义登录逻辑
 * Date 2021/11/1 18:41
 * Version 1.0.1
 *
 * @author Wen
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    SysUserServiceImpl sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException(LoginEnum.ERROR_LOGIN.getDesc());
        }
        return new AccountUser(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(), getUserAuthority(sysUser.getId()));
    }

    /**
     * 根据用户id获得用户对应的权限(角色、菜单权限)
     * @param userId 用户id
     * @return 用户对应的权限集合
     */
    public List<GrantedAuthority> getUserAuthority(Long userId) {
        String authority = sysUserService.getUserAuthorityInfo(userId);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
