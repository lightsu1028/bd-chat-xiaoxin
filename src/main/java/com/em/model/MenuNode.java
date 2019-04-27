package com.em.model;

import java.util.List;

/**
 * @author Baikang Lu
 * @date 2018/1/22
 */
public class MenuNode {

    private String name;
    private List<MenuNode> menuList;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<MenuNode> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuNode> menuList) {
        this.menuList = menuList;
    }
}
