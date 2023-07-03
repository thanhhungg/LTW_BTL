package com.example.libraryJSP.service;

import com.example.libraryJSP.entities.Account;
import com.example.libraryJSP.entities.Book;
import com.example.libraryJSP.repository.accountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountDao {
    @Autowired
    private accountRepository accountdao;

    public Account getAccount(String username){
        Account account = null;
        try{
        account = accountdao.findByUsername(username);
        }catch (Exception e){
            e.printStackTrace();
        }
        return account;
    }
    public Account insertAccount(Account account){
        return accountdao.save(account);
    }

    public Account saveAccount(Account account){
        return accountdao.save(account);
    }

    //Add

    public Optional<Account> findById(int isadmin) {

        return accountdao.findById(isadmin);
    }
    public List<Account> getListAccount(){
        return accountdao.findAll();
    }
    public Account getAccount(int id){
        return accountdao.findById(id).get();
    }
    public void deleteAcoount(int id){
        Account account = getAccount(id);
        accountdao.delete(account);
    }
    
}
