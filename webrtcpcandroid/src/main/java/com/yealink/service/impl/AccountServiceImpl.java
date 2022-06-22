package com.yealink.service.impl;

import com.yealink.dao.AccountDao;
import com.yealink.pojo.Account;
import com.yealink.pojo.Dept;
import com.yealink.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @date 2018/3/9 8:36
 */
@Service
public class AccountServiceImpl implements AccountService{

    private AccountDao accountDao;

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * 该方法用于检查账号是否存在
     * @param username
     * @return
     */
    public boolean checkAccount(String username){
        return accountDao.getAccount(username) != null;
    }

    /**
     * 该方法用于检查密码是否正确
     * @param account
     * @return
     */
    public boolean checkPassword(Account account){
        Account result=accountDao.getAccount(account.getUsername());
        return result.getPassword().trim().equals(account.getPassword().trim());
    }

    public Account getAccount(String username){
        return  accountDao.getAccount(username);
    }

    public Account getAccountBySid(Integer sId){
        return accountDao.getAccountBySid(sId);
    }

    public List<Account> getAccountPageList(Dept dept){
        return accountDao.getAccountPageList(dept);
    }
    public boolean saveAccount(Account account){
        return accountDao.saveAccount(account) != 0;
    }

    public boolean updateAccount(Account account){
        return accountDao.updateAccount(account) !=0;
    }

    public boolean deleteByUsername(String username){
        return accountDao.deleteByUsername(username) != 0;
    }

    public boolean batchDeleteAccount(String usernames[]){
        return accountDao.batchDeleteAccount(usernames) != 0;
    }
}
