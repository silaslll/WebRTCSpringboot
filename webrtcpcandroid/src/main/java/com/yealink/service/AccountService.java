package com.yealink.service;

import com.yealink.pojo.Account;
import com.yealink.pojo.Dept;

import java.util.List;

/**
 *
 * @date 2018/3/8 17:55
 */
public interface AccountService {
    public boolean checkAccount(String username);
    public boolean checkPassword(Account account);
    public Account getAccount(String username);
    public Account getAccountBySid(Integer sId);
    public List<Account> getAccountPageList(Dept dept);
    public boolean saveAccount(Account account);
    public boolean updateAccount(Account account);
    public boolean deleteByUsername(String username);
    public boolean batchDeleteAccount(String usernames[]);
}
