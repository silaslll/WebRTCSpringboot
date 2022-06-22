package com.yealink.pojo;

/**
 *
 * @date 2018/3/8 15:37
 */
public class Dept {
    private Integer id;
    private String name;

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
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dept:{ id = " + this.id + " , name = "+this.name+" }";
    }
}
