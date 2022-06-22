package com.yealink.pojo;

/**
 *
 * @date 2018/3/16 15:34
 */
public class Temp {
    private Integer vcsId;
    private String username;

    public Temp(){
        super();
    }

    public Temp(Integer vcsId, String username) {
        this.vcsId = vcsId;
        this.username = username;
    }

    public Integer getVcsId() {
        return vcsId;
    }

    public void setVcsId(Integer vcsId) {
        this.vcsId = vcsId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Temp{" +
                "vcsId=" + vcsId +
                ", username='" + username + '\'' +
                '}';
    }
}
