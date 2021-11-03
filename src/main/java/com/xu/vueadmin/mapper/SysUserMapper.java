package com.xu.vueadmin.mapper;

import com.xu.vueadmin.pojo.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 旭日
 * @since 2021-10-30
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据菜单id获得对应的用户
     * @param menuId 菜单id
     * @return 用户
     */
    @Select("SELECT DISTINCT sys_user.* FROM sys_user LEFT JOIN sys_user_role ON sys_user.id = sys_user_role.user_id LEFT JOIN sys_role_menu ON sys_user_role.role_id = sys_role_menu.role_id \n" +
            "WHERE sys_role_menu.menu_id = #{menuId};")
    List<SysUser> listByMenuId(Long menuId);

    /**
     * 根据用户id获得所拥有的菜单
     * @param userId 用户id
     * @return 菜单
     */
    @Select("SELECT DISTINCT sys_role_menu.menu_id FROM sys_user_role LEFT JOIN sys_role_menu ON sys_user_role.role_id = sys_role_menu.role_id WHERE sys_user_role.user_id = #{userId}")
    List<Long> getNavMenuIds(Long userId);
}
