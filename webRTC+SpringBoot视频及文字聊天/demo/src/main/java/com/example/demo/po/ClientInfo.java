package com.example.demo.po;

import com.example.demo.utils.DateUtils;

import java.util.Date;

/**
 * socket会话信息
 */
public class ClientInfo {

    private String roomId;

    private Object data;

    private String userName;

    private Date date;

    private String msg;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStrDate() {
        if (this.date != null) {
            return DateUtils.dateToString(this.date, DateUtils.DATEFORMAT3);
        }
        return DateUtils.dateToString(new Date(), DateUtils.DATEFORMAT3);
    }
}