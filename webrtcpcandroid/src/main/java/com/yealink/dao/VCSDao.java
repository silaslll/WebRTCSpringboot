package com.yealink.dao;

import com.yealink.pojo.VCS;

import java.util.List;

/**
 *
 * @date 2018/3/16 15:33
 */
public interface VCSDao {
    public List<VCS> selectPageList(VCS vcs);
    public Integer selectMaxId();
    public Integer saveVCS(VCS vcs);
    public String getCreater(Integer id);
    public Integer closeVCS(Integer id);
    public VCS getTemps(Integer id);
    public Integer getVcsState(Integer id);
    public Integer deleteVCS(Integer id);
    public Integer deleteBatch(int ids[]);
}
