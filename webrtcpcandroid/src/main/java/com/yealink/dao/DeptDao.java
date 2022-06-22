package com.yealink.dao;

import com.yealink.pojo.Dept;

import java.util.List;

/**
 *
 * @date 2018/3/8 15:53
 */
public interface DeptDao {
    public Integer insert(Dept dept);
    public Integer delete(Integer id);
    public List<Dept> getPageList();
    public Integer update(Dept dept);
    public Dept selectByName(String deptname);
    public Integer deleteBatch(int[] ids);
    public Dept selectById(Integer id);
}
