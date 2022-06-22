package com.yealink.controller;

import com.yealink.pojo.Account;
import com.yealink.pojo.Staff;
import com.yealink.service.AccountService;
import com.yealink.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 *
 * @date 2018/3/9 14:53
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private AccountService accountService;
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    private StaffService staffService;

    @Autowired
    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }

    @RequestMapping("")
    @ResponseBody
    public String login(Account account, HttpSession session){
        //第一层判断账号是否存在
        if(accountService.checkAccount(account.getUsername())){
            //第二层判断密码是否正确
            if(accountService.checkPassword(account)){
                //第三层判断用户权限
                if(account.getUsername().trim().equals("admin")){
                    session.setAttribute("username","admin");
                    return "admin";
                }else{
                    Account result=accountService.getAccount(account.getUsername());
                    session.setAttribute("username",result.getUsername());
                    //查询出职员具体信息并保存
                    Staff s=staffService.getStaffBySid(result.getsId());
                    session.setAttribute("staffId",result.getsId());
                    session.setAttribute("staffName",s.getName());
                    session.setAttribute("staffSex",s.getSex());
                    session.setAttribute("staffAge",s.getAge());
                    session.setAttribute("staffPhone",s.getPhone());
                    session.setAttribute("staffPid",s.getpId());
                    session.setAttribute("staffPostName",s.getPost().getName());
                    session.setAttribute("staffDid",s.getPost().getdId());
                    session.setAttribute("staffDeptName",s.getPost().getDept().getName());
                    return "normal";
                }
            }else {
                return "passwordWrong";
            }
        }else {
            return "usernameWrong";
        }
    }

    /**
     * 登出功能
     * @param session
     * @return
     */
    @RequestMapping("/unlogin")
    public String unlogin(HttpSession session){
        if(session.getAttribute("username") != null)
            session.removeAttribute("username");
        if(session.getAttribute("s_id") != null){
            session.removeAttribute("s_id");
        }
        return "redirect:/";
    }
}
