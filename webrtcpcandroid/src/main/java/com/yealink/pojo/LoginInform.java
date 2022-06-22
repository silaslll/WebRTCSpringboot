package com.yealink.pojo;

import java.io.Serializable;

/**
 * 该类用于登录时向 登录服务器 保存相关记录信息
 *
 * @date 2018/3/16 11:05
 */
public class LoginInform implements Serializable {
    private String username;
    private String staffName;
    private String staffPostName;
    private Integer staffDid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffPostName() {
        return staffPostName;
    }

    public void setStaffPostName(String staffPostName) {
        this.staffPostName = staffPostName;
    }

    public Integer getStaffDid() {
        return staffDid;
    }

    public void setStaffDid(Integer staffDid) {
        this.staffDid = staffDid;
    }

    @Override
    public String toString() {
        return "LoginInform{" +
                "username='" + username + '\'' +
                ", staffName='" + staffName + '\'' +
                ", staffPostName='" + staffPostName + '\'' +
                ", staffDid=" + staffDid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginInform)) return false;

        LoginInform that = (LoginInform) o;

        if (getUsername() != null ? !getUsername().equals(that.getUsername()) : that.getUsername() != null)
            return false;
        if (getStaffName() != null ? !getStaffName().equals(that.getStaffName()) : that.getStaffName() != null)
            return false;
        if (getStaffPostName() != null ? !getStaffPostName().equals(that.getStaffPostName()) : that.getStaffPostName() != null)
            return false;
        return getStaffDid() != null ? getStaffDid().equals(that.getStaffDid()) : that.getStaffDid() == null;
    }

    @Override
    public int hashCode() {
        int result = getUsername() != null ? getUsername().hashCode() : 0;
        result = 31 * result + (getStaffName() != null ? getStaffName().hashCode() : 0);
        result = 31 * result + (getStaffPostName() != null ? getStaffPostName().hashCode() : 0);
        result = 31 * result + (getStaffDid() != null ? getStaffDid().hashCode() : 0);
        return result;
    }
}
