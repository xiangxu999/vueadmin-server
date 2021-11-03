package com.xu.vueadmin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 旭日
 * @since 2021-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysMenu对象", description="SysMenu对象")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "父菜单ID，一级菜单为0")
    private Long parentId;

    private String name;

    @ApiModelProperty(value = "菜单URL")
    private String path;

    @ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list,user:create)")
    private String perms;

    private String component;

    @ApiModelProperty(value = "类型     0：目录   1：菜单   2：按钮")
    private Integer type;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    private LocalDateTime created;

    private LocalDateTime updated;

    private Integer status;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();


}
