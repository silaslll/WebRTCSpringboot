package com.yealink.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用的返回类
 *代码来源于下面这个作者，在本项目中复用，并根据需要进行一定程度的修改
 * @author 杨超
 * @date 2017/12/11 10:32
 */
public class Msg {
    // 状态码   100-成功    200-失败
    private int code;
    // 提示信息
    private String msg;

    // 返回给浏览器的数据
    private Map<String, Object> extend = new HashMap<String, Object>();

    public static Msg success() {
        Msg result = new Msg();
        result.setCode(100);
        result.setMsg("success");
        return result;
    }

    public static Msg fail(String message) {
        Msg result = new Msg();
        result.setCode(200);
        //填入错误发生的具体原因
        result.setMsg(message);
        return result;
    }

    public Msg add(String key, Object value) {
        this.getExtend().put(key, value);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }


}
