package com.xtll.system.domain;

import com.xtll.common.core.domain.BaseEntity;
import com.xtll.system.domain.vo.AreasPac;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class XtUser extends BaseEntity {
    private Integer id;

    private String name;

    private String loginName;

    private String password;

    private Integer roleId;

    private Long telephone;

    private String status;

    private Date createTime;

    private String perm;

    private String userProvince;

    private Date updateTime;

    private String userCardid;

    private String userCity;

    private String userDist;
    //存储地区的代号
    private String userAreas;


    //接受权限--增加的
    private String[] power;

    //角色名称--增加的
    private String roleName;

    //----增加的
    private String number;

    //---增加的
    private String[] areasNum;

    //---增加的 封装区域
    private List<AreasPac> areas;

    //---增加的  封装区域代号父级子级
    private List<String> areaNums;

    public List<String> getAreaNums() {
        return areaNums;
    }

    public void setAreaNums(List<String> areaNums) {
        this.areaNums = areaNums;
    }

    public List<AreasPac> getAreas() {
        return areas;
    }

    public void setAreas(List<AreasPac> areas) {
        this.areas = areas;
    }

    public String[] getAreasNum() {
        return areasNum;
    }

    public void setAreasNum(String[] areasNum) {
        this.areasNum = areasNum;
    }

    public String getUserAreas() {
        return userAreas;
    }

    public void setUserAreas(String userAreas) {
        this.userAreas = userAreas;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String[] getPower() {
        return power;
    }

    public void setPower(String[] power) {
        this.power = power;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Long getTelephone() {
        return telephone;
    }

    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm == null ? null : perm.trim();
    }

    public String getUserProvince() {
        return userProvince;
    }

    public void setUserProvince(String userProvince) {
        this.userProvince = userProvince == null ? null : userProvince.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserCardid() {
        return userCardid;
    }

    public void setUserCardid(String userCardid) {
        this.userCardid = userCardid == null ? null : userCardid.trim();
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity == null ? null : userCity.trim();
    }

    public String getUserDist() {
        return userDist;
    }

    public void setUserDist(String userDist) {
        this.userDist = userDist == null ? null : userDist.trim();
    }

    public boolean isAdmin()
    {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Integer id)
    {
        return id != null && 1 == id;
    }

    @Override
    public String toString() {
        return "XtUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", roleId=" + roleId +
                ", telephone=" + telephone +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", perm='" + perm + '\'' +
                ", userProvince='" + userProvince + '\'' +
                ", updateTime=" + updateTime +
                ", userCardid='" + userCardid + '\'' +
                ", userCity='" + userCity + '\'' +
                ", userDist='" + userDist + '\'' +
                ", userAreas='" + userAreas + '\'' +
                ", power=" + Arrays.toString(power) +
                ", roleName='" + roleName + '\'' +
                ", number='" + number + '\'' +
                ", areasNum=" + Arrays.toString(areasNum) +
                ", areas=" + areas +
                '}';
    }
}