package com.xtll.system.domain;

import com.xtll.common.core.domain.BaseEntity;

public class XtMenu extends BaseEntity {
    private Integer id;

    private String menuKey;

    private String menuName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey == null ? null : menuKey.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }
}