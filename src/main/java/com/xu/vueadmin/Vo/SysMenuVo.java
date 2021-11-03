package com.xu.vueadmin.Vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description 返回给前端的菜单格式
 * Date 2021/11/3 11:14
 * Version 1.0.1
 *
 * @author Wen
 */
@Data
public class SysMenuVo implements Serializable {

    private Long id;

    private String name;

    private String title;

    private String icon;

    private String path;

    private String component;

    private List<SysMenuVo> children = new ArrayList<>();
}
