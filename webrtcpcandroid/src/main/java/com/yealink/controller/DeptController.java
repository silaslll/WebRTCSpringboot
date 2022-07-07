package com.yealink.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yealink.pojo.Dept;
import com.yealink.pojo.Msg;
import com.yealink.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 *
 * @date 2018/3/12 11:48
 */
@Controller
@RequestMapping("/dept")
public class DeptController {
    private DeptService deptService;
    @Autowired
    public void setDeptService(DeptService deptService) {
        this.deptService = deptService;
    }

    @RequestMapping(value = "/{pageNo}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getDeptPageList(@PathVariable(value = "pageNo") Integer pageNo){
        PageHelper.startPage(pageNo,10);
        List<Dept> deptList=deptService.getPageList();
        PageInfo<Dept> pageInfo=new PageInfo<Dept>(deptList,3);
        return Msg.success().add("pageInfo",pageInfo);
        
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    @ResponseBody
    public Msg getDeptList(){
        List<Dept> deptList=deptService.getPageList();
        return Msg.success().add("deptList",deptList);
    }

    @RequestMapping(value="admin/{name}",method=RequestMethod.GET)
    @ResponseBody
    public Msg checkDept(@PathVariable(value = "name") String name){
        //System.out.println(name);
        if( name==null){
            return Msg.fail("请输入部门名称");
        }
        String regx = "^[\\u2E80-\\u9FFF]{2,10}$";
        if (!name.matches(regx)) {
            return Msg.fail("部门名称必须是2-10位的中文");
        }
        System.out.println(name);
        // 数据库部门名重复校验
        if (deptService.checkDept(name)) {
            return Msg.success();
        } else {
            return Msg.fail("部门名称不可用");
        }
    }

    @RequestMapping(value="admin",method = RequestMethod.POST)
    @ResponseBody
    public String saveDept(@RequestParam String name){
        Dept dept=new Dept();
        dept.setName(name);
        Integer result=deptService.saveDept(dept);
        if(result != 0) return "success";
        else{
            return "error";
        }
    }

    @RequestMapping(value="admin/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@PathVariable Integer id){
        Integer result=deptService.delete(id);
        if(result != 0) return "success";
        else{
            return "error";
        }
    }

    @RequestMapping(value="admin",method = RequestMethod.PUT)
    @ResponseBody
    public String modifyDept(@ModelAttribute Dept dept){
        Integer result=deptService.update(dept);
        if(result != 0) return "success";
        else{
            return "error";
        }
    }

    @RequestMapping(value = "admin",method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteBatch(@RequestParam(value = "ids[]") int ids[]){
        Integer result=deptService.deleteBatch(ids);
        if(result != 0) return "success";
        else{
            return "error";
        }
    }
}
