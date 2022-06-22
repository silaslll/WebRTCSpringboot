package com.yealink.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yealink.pojo.Msg;
import com.yealink.pojo.Post;
import com.yealink.pojo.Staff;
import com.yealink.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @date 2018/3/13 16:57
 */
@RequestMapping("/staff")
@Controller
public class StaffController {
    private StaffService staffService;
    @Autowired
    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }

    @RequestMapping("pageList")
    @ResponseBody
    public Msg getStaffPageList(@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,@ModelAttribute Post post){
        PageHelper.startPage(pageNo,10);
        List<Staff> staffs=staffService.getStaffPageList(post);
        PageInfo<Staff> pageInfo=new PageInfo<Staff>(staffs,3);
        return Msg.success().add("pageInfo",pageInfo);
    }

    @RequestMapping("getListByDeptId")
    @ResponseBody
    public List<Staff> getStaffList(@RequestParam Integer dId){
        Post post=new Post();
        post.setdId(dId);
        return staffService.getStaffPageList(post);
    }
    //检查是否能够删除某职位
    @RequestMapping("admin/checkDeleteStaff")
    @ResponseBody
    public String checkDeleteStaff(@RequestParam Integer id){
        List<Staff> staffs=staffService.getStaffByPid(id);
        if(staffs.isEmpty()){
            return "success";
        }else{
            return "error";
        }
    }

    @RequestMapping("admin/checkBatchDeleteStaff")
    @ResponseBody
    public String checkBatchDeleteStaff(@RequestParam(value = "ids[]") Integer ids[]){
        for(Integer id:ids) {
            List<Staff> staffs = staffService.getStaffByPid(id);
            if(!staffs.isEmpty()){
                return "error";
            }
        }
        return "success";
    }


    @RequestMapping(value="admin",method=RequestMethod.POST)
    @ResponseBody
    public String saveStaff(@ModelAttribute Staff staff){
        //先进行后端校验
        if(staff.getName() == null || staff.getName().trim().equals("")){
            return "nameNull";
        }
        if(staff.getAge() == null){
            return "ageNull";
        }
        if(staff.getPhone() == null || staff.getPhone().trim().equals("")){
            return "phoneNull";
        }
        if(staff.getpId() == null || staff.getpId() == 0){
            return "postNull";
        }
        if(staff.getAge() < 1 || staff.getAge() > 99){
            return "ageError";
        }
        String regPhone="^[1][3,4,5,7,8][0-9]{9}$";
        if(!staff.getPhone().matches(regPhone)){
            return "phoneError";
        }
        if(staffService.saveStaff(staff))
            return "success";
        else return "error";
    }

    @RequestMapping(value="",method = RequestMethod.PUT)
    @ResponseBody
    public String updateStaff(@ModelAttribute Staff staff){
        //先进行后端校验
        if(staff.getName() == null || staff.getName().trim().equals("")){
            return "nameNull";
        }
        if(staff.getAge() == null){
            return "ageNull";
        }
        if(staff.getPhone() == null || staff.getPhone().trim().equals("")){
            return "phoneNull";
        }
        if(staff.getpId() == null || staff.getpId() == 0){
            return "postNull";
        }
        if(staff.getAge() < 1 || staff.getAge() > 99){
            return "ageError";
        }
        String regPhone="^[1][3,4,5,7,8][0-9]{9}$";
        if(!staff.getPhone().matches(regPhone)){
            return "phoneError";
        }
        if(staffService.updateStaff(staff))
            return "success";
        else return "error";
    }

    @RequestMapping(value = "admin/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteById(@PathVariable Integer id){
        if(staffService.deleteById(id))
            return "success";
        else return "error";
    }

    @RequestMapping(value = "admin",method = RequestMethod.DELETE)
    @ResponseBody
    public String batchDeleteStaff(@RequestParam(value = "ids[]")int ids[]){
        if(staffService.batchDeleteStaff(ids))
            return "success";
        else return "error";
    }
}
