package com.yealink.service;

import com.yealink.pojo.Post;
import com.yealink.pojo.Staff;

import java.util.List;

/**
 *
 * @date 2018/3/13 16:55
 */
public interface StaffService {
    public List<Staff> getStaffPageList(Post post);
    public boolean saveStaff(Staff staff);
    public boolean updateStaff(Staff staff);
    public boolean deleteById(Integer id);
    public boolean batchDeleteStaff(int ids[]);
    public List<Staff> getStaffByPid(Integer id);
    public Staff getStaffBySid(Integer id);
}
