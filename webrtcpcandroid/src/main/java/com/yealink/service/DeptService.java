package com.yealink.service;

import com.yealink.pojo.Dept;

import java.util.List;

/**
 *
 * @date 2018/3/12 11:44
 */
public interface DeptService {
    public List<Dept> getPageList();
    public boolean checkDept(String deptname);
    public Integer saveDept(Dept dept);
    public Integer delete(Integer id);
    public Integer update(Dept dept);
    public Integer deleteBatch(int[] ids);
}
