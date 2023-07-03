package com.example.libraryJSP.repository;

import com.example.libraryJSP.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface accountRepository extends JpaRepository<Account, Integer> {
    @Query("Select p from Account p where p.username = :name ")
    Account findByUsername(@Param("name") String username);
//    boolean existsAccountByUsername(String username);
}
