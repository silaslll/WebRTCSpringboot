package com.yealink.pojo;

import java.util.List;

/**
 *
 * @date 2018/3/16 15:30
 */
public class VCS {
    private Integer vcsId;
    private String creater;
    private String vcsTitle;
    private Integer vcsState;

    private List<Temp> invited;

    public Integer getVcsId() {
        return vcsId;
    }

    public void setVcsId(Integer vcsId) {
        this.vcsId = vcsId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getVcsTitle() {
        return vcsTitle;
    }

    public void setVcsTitle(String vcsTitle) {
        this.vcsTitle = vcsTitle;
    }

    public Integer getVcsState() {
        return vcsState;
    }

    public void setVcsState(Integer vcsState) {
        this.vcsState = vcsState;
    }

    public List<Temp> getInvited() {
        return invited;
    }

    public void setInvited(List<Temp> invited) {
        this.invited = invited;
    }

    @Override
    public String toString() {
        return "VCS{" +
                "vcsId=" + vcsId +
                ", creater='" + creater + '\'' +
                ", vcsTitle='" + vcsTitle + '\'' +
                ", vcsState=" + vcsState +
                '}';
    }
}
