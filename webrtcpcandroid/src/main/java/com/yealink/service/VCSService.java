package com.yealink.service;


import com.yealink.pojo.Temp;
import com.yealink.pojo.VCS;

import java.util.List;

/**
 *
 * @date 2018/3/16 16:07
 */

public interface VCSService {
    public List<VCS> getPageList(VCS vcs);
    public Integer selectMaxId();
    public Integer saveVCS(VCS vcs);
    public String getCreater(Integer id);
    public boolean closeVCS(Integer id);
    public List<Temp> getTemps(Integer id);
    public boolean checkVcsState(Integer id);
    public Integer deleteVcs(Integer id);
    public Integer deleteBatch(int ids[]);
}
