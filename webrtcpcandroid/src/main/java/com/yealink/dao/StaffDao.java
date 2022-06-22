package com.yealink.dao;

import com.yealink.pojo.Post;
import com.yealink.pojo.Staff;

import java.util.List;

/**
 *
 * @date 2018/3/13 16:46
 */
public interface StaffDao {
    public List<Staff> selectAll(Post post);
    public Integer insert(Staff staff);
    public Integer updateStaff(Staff staff);
    public Integer deleteById(Integer id);
    public Integer batchDeleteStaff(int ids[]);
    public List<Staff> selectStaffByPid(Integer id);
    public Staff selectBySid(Integer id);
}
