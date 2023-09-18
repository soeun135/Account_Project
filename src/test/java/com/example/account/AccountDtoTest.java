package com.example.account;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AccountDtoTest {
    @Test
    void accountDto() {
        //given
        AccountDto accountDto = new AccountDto(
                "accountNumber",
                "summer",
                LocalDateTime.now()
        );

        //when

        //then
        System.out.println(accountDto.getAccountNumber());
        System.out.println(accountDto.toString());
    }
}