package com.xu.vueadmin.service;

import com.xu.vueadmin.Vo.SysMenuVo;
import com.xu.vueadmin.pojo.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 旭日
 * @since 2021-10-30
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 获取当前用户能看到的导航栏
     * @return 导航栏的集合列表
     */
    List<SysMenuVo> getCurrentUserNav();
}
