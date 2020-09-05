package com.example.strangeclass.service;

import com.example.strangeclass.dao.TicAccountRepository;
import com.example.strangeclass.entity.TicAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicAccountService {

    @Autowired
    private TicAccountRepository ticAccountRepository;

    /**
     * 获得可用TIC账号
     *
     * @param username
     * @return
     */
    public TicAccount getAvailableAccount(String username) {
        List<TicAccount> ticAccounts = ticAccountRepository.findAllByWhoUsedIsNull();
        if (ticAccounts.size() == 0) {
            return null;
        }
        TicAccount ticAccount = ticAccounts.get(0);
        ticAccount.setWhoUsed(username);
        return ticAccount;
    }

    /**
     * 释放TIC账号
     *
     * @param username
     */
    public void returnAccount(String username) {
        TicAccount ticAccountDB = ticAccountRepository.findByWhoUsed(username);
        ticAccountDB.setWhoUsed(null);
        ticAccountRepository.save(ticAccountDB);
    }
}
