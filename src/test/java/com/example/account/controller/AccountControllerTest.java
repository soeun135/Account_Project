package com.example.account.controller;

import com.example.account.domain.Account;
import com.example.account.domain.AccountStatus;
import com.example.account.service.AccountService;
import com.example.account.service.RedisTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class) //Test하려고 하는 컨트롤러 명시
class AccountControllerTest {
    @MockBean //Mock은 Mock인데 Bean으로 등록해주는 Mock //자동으로 Bean등록돼서 AccountController에 주입됨.
    private AccountService accountService;

    @MockBean
    private RedisTestService redisTestService;

    //injection을 해줘야하지만 맨 위에 @WebMvcTest()괄호 안에 컨트롤러 넣어줘서 안 해도됨.

    @Autowired
    private MockMvc mockMvc;

    @Test
    void successGetAccount() throws Exception {
        //given
        given(accountService.getAccount(anyLong()))
                .willReturn(Account.builder()
                        .accountNumber("1234")
                        .accountStatus(AccountStatus.IN_USE)
                        .build());
        //when
        //then
        mockMvc.perform(get("/account/23123411124"))
                .andDo(print()) //응답값을 화면에 표시해줌
                .andExpect(jsonPath("$.accountNumber").value("1234"))
                .andExpect(jsonPath("$.accountStatus").value("IN_USE"))
                .andExpect(status().isOk());
     }
}