package com.yealink.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yealink.pojo.Account;
import com.yealink.pojo.Dept;
import com.yealink.pojo.Msg;
import com.yealink.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
import java.util.List;

/**
 *
 * @date 2018/3/8 17:56
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    private AccountService accountService;
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "admin/{sId}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getAccountBySid(@PathVariable Integer sId){
        Account account= accountService.getAccountBySid(sId);
        if(account != null){
            return Msg.success().add("account",account);
        }else return Msg.fail("null");
    }

    @RequestMapping("admin/getAccountPageList")
    @ResponseBody
    public Msg getAccountPageList(@RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,@ModelAttribute Dept dept){
        PageHelper.startPage(pageNo,10);
        List<Account> accounts=accountService.getAccountPageList(dept);
        PageInfo<Account> pageInfo=new PageInfo<Account>(accounts,3);
        return Msg.success().add("pageInfo",pageInfo);
    }

    @RequestMapping(value="admin",method = RequestMethod.POST)
    @ResponseBody
    public String saveAccount(@RequestParam String rePassword,@ModelAttribute Account account){
        //后端校验
        if(account.getUsername() == null|| account.getUsername().trim().equals("")){
            return "usernameNull";
        }
        if(account.getPassword() == null|| account.getPassword().trim().equals("")){
            return "passwordNull";
        }
        if(account.getsId() == null|| account.getsId() == 0){
            return "sidNull";
        }
        if(accountService.checkAccount(account.getUsername())){
            return "usernameRepeat";
        }
        if(!account.getPassword().equals(rePassword)){
            return "passwordWrong";
        }
        if(accountService.getAccountBySid(account.getsId()) != null){
            return "sIdWrong";
        }
        //校验通过，保存
        if(accountService.saveAccount(account)){
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping(value = "",method = RequestMethod.PUT)
    @ResponseBody
    public String updateAccount(@RequestParam String rePassword,@ModelAttribute Account account){
        if(account.getPassword() == null|| account.getPassword().trim().equals("")){
            return "passwordNull";
        }
        if(!account.getPassword().equals(rePassword)){
            return "passwordWrong";
        }
        //校验通过，保存
        if(accountService.updateAccount(account)){
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping(value="admin/{username}",method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteByUsername(@PathVariable String username){
        if(accountService.deleteByUsername(username)){
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping(value = "admin",method = RequestMethod.DELETE)
    @ResponseBody
    public String batchDeleteAccount(@RequestParam(value = "usernames[]") String usernames[]){
        if(accountService.batchDeleteAccount(usernames)){
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping("checkPassword")
    @ResponseBody
    public String checkPassword(@ModelAttribute Account account){
        if(accountService.checkPassword(account)){
            return "success";
        }else {
            return "error";
        }
    }
}
