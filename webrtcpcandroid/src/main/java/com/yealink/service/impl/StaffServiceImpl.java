package com.yealink.service.impl;

import com.yealink.dao.StaffDao;
import com.yealink.pojo.Post;
import com.yealink.pojo.Staff;
import com.yealink.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @date 2018/3/13 16:55
 */
@Service
public class StaffServiceImpl implements StaffService{
    private StaffDao staffDao;
    @Autowired
    public void setStaffDao(StaffDao staffDao) {
        this.staffDao = staffDao;
    }

    public List<Staff> getStaffPageList(Post post){
        return staffDao.selectAll(post);
    }

    public List<Staff> getStaffByPid(Integer id) {
        return staffDao.selectStaffByPid(id);
    }

    public boolean saveStaff(Staff staff){
        return staffDao.insert(staff) != 0 ;
    }

    public boolean updateStaff(Staff staff){
        return staffDao.updateStaff(staff) != 0;
    }

    public boolean deleteById(Integer id){
        return staffDao.deleteById(id) != 0;
    }

    public boolean batchDeleteStaff(int ids[]){
        return staffDao.batchDeleteStaff(ids) != 0;
    }

    public Staff getStaffBySid(Integer id){
        return staffDao.selectBySid(id);
    }

}
