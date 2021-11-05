package com.xu.vueadmin.service.impl;

import cn.hutool.json.JSONUtil;
import com.xu.vueadmin.Vo.SysMenuVo;
import com.xu.vueadmin.mapper.SysUserMapper;
import com.xu.vueadmin.pojo.SysMenu;
import com.xu.vueadmin.mapper.SysMenuMapper;
import com.xu.vueadmin.pojo.SysUser;
import com.xu.vueadmin.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 旭日
 * @since 2021-10-30
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysMenuVo> getCurrentUserNav() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getByUsername(username);

        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        List<SysMenu> sysMenus = this.listByIds(menuIds);

        // 把菜单集合转换为树形结构
        List<SysMenu> menuTree = buildTreeMenu(sysMenus);
        // 实体转Vo
        return convert(menuTree);
    }

    public List<SysMenuVo> convert(List<SysMenu> menuTree) {
        List<SysMenuVo> menuVoList = new ArrayList<>();

        menuTree.forEach(m -> {
            SysMenuVo sysMenuVo = new SysMenuVo();

            sysMenuVo.setId(m.getId());
            sysMenuVo.setName(m.getName());
            sysMenuVo.setTitle(m.getName());
            sysMenuVo.setComponent(m.getComponent());
            sysMenuVo.setPath(m.getPath());
            sysMenuVo.setIcon(m.getIcon());

            if (m.getChildren().size() > 0) {
                sysMenuVo.setChildren(convert(m.getChildren()));
            }

            menuVoList.add(sysMenuVo);
        });

        return menuVoList;
    }


    public List<SysMenu> buildTreeMenu(List<SysMenu> menus) {

        List<SysMenu> finalMenus = new ArrayList<>();

        // 先各自寻找各自的孩子
        for (SysMenu menu : menus) {
            for (SysMenu e: menus) {
                if (e.getParentId().equals(menu.getId())) {
                    menu.getChildren().add(e);
                }
            }
            // 提取父节点
            if (menu.getParentId() == 0L) {
                finalMenus.add(menu);
            }
        }

        System.out.println(JSONUtil.toJsonStr(finalMenus));
        return finalMenus;
    }

}
