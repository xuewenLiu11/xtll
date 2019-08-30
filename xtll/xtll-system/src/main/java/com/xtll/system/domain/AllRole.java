package com.xtll.system.domain;

import com.xtll.common.core.domain.BaseEntity;

public class AllRole extends BaseEntity {
    private Integer id;

    private String role;

    private String perm;

    private String[] power;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm == null ? null : perm.trim();
    }

    public String[] getPower() {
        return power;
    }

    public void setPower(String[] power) {
        this.power = power;
    }
}