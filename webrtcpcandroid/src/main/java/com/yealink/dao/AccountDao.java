package com.yealink.dao;

import com.yealink.pojo.Account;
import com.yealink.pojo.Dept;

import java.util.List;

/**
 *
 * @date 2018/3/8 17:05
 */
public interface AccountDao {
    /**
     * 该方法可以通过账号获取一个账号的信息（账号名称、账号密码、账号对应的用户），若查找不到对应的用户，将返回 null。
     * @param name
     * @return
     */
    public Account getAccount(String name);

    public Account getAccountBySid(Integer sId);

    public List<Account> getAccountPageList(Dept dept);

    public Integer saveAccount(Account account);

    public Integer updateAccount(Account account);

    public Integer deleteByUsername(String username);

    public Integer batchDeleteAccount(String usernames[]);
    
}
