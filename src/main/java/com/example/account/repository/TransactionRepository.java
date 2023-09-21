package com.example.account.repository;

import com.example.account.domain.Transaction;
import com.example.account.dto.TransactionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
