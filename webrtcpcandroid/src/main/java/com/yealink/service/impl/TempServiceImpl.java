package com.yealink.service.impl;

import com.yealink.dao.TempDao;
import com.yealink.pojo.Temp;
import com.yealink.service.TempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @date 2018/3/19 10:51
 */
@Service
public class TempServiceImpl implements TempService{
    private TempDao tempDao;

    @Autowired
    public void setTempDao(TempDao tempDao) {
        this.tempDao = tempDao;
    }

    public Integer saveTempList(List<Temp> invited){
        return tempDao.saveTempList(invited);
    }

    public boolean userCanJoin(Temp temp){
        return tempDao.getTemp(temp) != null;
    }
}
