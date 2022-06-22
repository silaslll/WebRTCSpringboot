package com.yealink.service.impl;

import com.yealink.dao.DeptDao;
import com.yealink.pojo.Dept;
import com.yealink.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @date 2018/3/12 11:45
 */
@Service
public class DeptServiceImpl implements DeptService {
    DeptDao deptDao;

    @Autowired
    public void setDeptDao(DeptDao deptDao) {
        this.deptDao = deptDao;
    }

    public List<Dept> getPageList(){
        return deptDao.getPageList();
    }

    public boolean checkDept(String deptname){
        return deptDao.selectByName(deptname) == null;
    }

    public Integer saveDept(Dept dept){
        return deptDao.insert(dept);
    }

    public Integer delete(Integer id){
        return deptDao.delete(id);
    }

    public Integer update(Dept dept){
        return  deptDao.update(dept);
    }
    public Integer deleteBatch(int [] ids){
        return deptDao.deleteBatch(ids);
    }
}
