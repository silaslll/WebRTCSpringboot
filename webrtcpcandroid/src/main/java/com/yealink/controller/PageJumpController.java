package com.yealink.controller;

import com.yealink.pojo.Temp;
import com.yealink.service.TempService;
import com.yealink.service.VCSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


/**
 *
 * @date 2018/3/9 11:20
 */
@Controller
@RequestMapping("/Page")
public class PageJumpController {
    private VCSService vcsService;
    @Autowired
    public void setVcsService(VCSService vcsService) {
        this.vcsService = vcsService;
    }

    private TempService tempService;

    @Autowired
    public void setTempService(TempService tempService) {
        this.tempService = tempService;
    }

    @RequestMapping("/admin/AdminIndex")
    public String toAdminIndexPage(){
        return "admin/adminIndex";
    }

    @RequestMapping("/admin/AdminDept")
    public String toAdminDeptPage(){
        return "admin/adminDept";
    }

    @RequestMapping("/admin/AdminPost")
    public String toAdminPostPage(){
        return "admin/adminPost";
    }

    @RequestMapping("/admin/AdminAccount")
    public String toAdminAccountPage(){
        return "admin/adminAccount";
    }

    @RequestMapping("/admin/AdminStaff")
    public String toAdminDeptStaff(){
        return "admin/adminStaff";
    }

    @RequestMapping("/admin/AdminVCS")
    public String toAdminVCS(){
        return "admin/adminVCS";
    }

    @RequestMapping("/normal/NormalIndex")
    public String toNormalIndexPage(){
        return "normal/normalIndex";
    }


    @RequestMapping("/test/test")
    public String test(){
        return "test/test";
    }

    @RequestMapping("/normal/VCSIndex")
    public String toVCSIndexPgea(@RequestParam Integer vcsId, HttpSession session){
        //页面跳转时便进行校验，只有校验通过才能进入页面。
        //校验过程为根据用户名和 视频会议id去进行查询，查询到才能进入。
        if(!vcsService.checkVcsState(vcsId)){
            Temp temp=new Temp(vcsId,(String)session.getAttribute("username"));
            if(tempService.userCanJoin(temp)) {
                session.setAttribute("vcs_Id",vcsId);
                return "normal/VCSindex";
            }
            else{
                return "normal/error";
            }
        }else {
            return "normal/error";
        }
    }
}
