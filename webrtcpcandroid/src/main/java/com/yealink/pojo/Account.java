package com.yealink.pojo;

/**
 *
 * @date 2018/3/8 17:03
 */
public class Account {
    private String username;
    private String password;
    private Integer sId;
    private Staff staff;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sId=" + sId +
                ", staff=" + staff +
                '}';
    }
}
