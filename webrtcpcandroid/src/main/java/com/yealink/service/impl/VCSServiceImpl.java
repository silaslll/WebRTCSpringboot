package com.yealink.service.impl;

import com.yealink.dao.VCSDao;
import com.yealink.pojo.Temp;
import com.yealink.pojo.VCS;
import com.yealink.service.VCSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @date 2018/3/16 16:07
 */
@Service
public class VCSServiceImpl implements VCSService{
    private VCSDao vcsDao;

    @Autowired
    public void setVcsDao(VCSDao vcsDao) {
        this.vcsDao = vcsDao;
    }

    public List<VCS> getPageList(VCS vcs){
        return vcsDao.selectPageList(vcs);
    }

    public Integer selectMaxId(){
        return vcsDao.selectMaxId();
    }

    public Integer saveVCS(VCS vcs){
        return vcsDao.saveVCS(vcs);
    }

    public String getCreater(Integer id){
        return vcsDao.getCreater(id);
    }

    public boolean closeVCS(Integer id){
        return vcsDao.closeVCS(id) == 1;
    }

    public List<Temp> getTemps(Integer id){
        try {
            return vcsDao.getTemps(id).getInvited();
        }catch (NullPointerException e){
            return new ArrayList<Temp>();
        }
    }

    public boolean checkVcsState(Integer id){
        return vcsDao.getVcsState(id) == 0;
    }

    public Integer deleteVcs(Integer id){
        return vcsDao.deleteVCS(id);
    }

    public Integer deleteBatch(int ids[]){
        return vcsDao.deleteBatch(ids);
    }
}
