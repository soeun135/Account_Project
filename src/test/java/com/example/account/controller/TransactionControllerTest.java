package com.example.account.controller;

import com.example.account.dto.CancelBalance;
import com.example.account.dto.TransactionDto;
import com.example.account.dto.UseBalance;
import com.example.account.service.TransactionService;
import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static com.example.account.type.TransactionResultType.*;
import static com.example.account.type.TransactionType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectmapper;

    @Test
    void useBalance_success() throws Exception {
        //given
        given(transactionService.useBalance(anyLong(), anyString(), anyLong()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1234567890")
                        .transactionType(USE)
                        .transactionResultType(S)
                        .amount(100L)
                        .transactionId("IIDD")
                        .transactedAt(LocalDateTime.now())
                        .build()
                );
        //when
        //then
        mockMvc.perform(post("/transaction/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectmapper.writeValueAsString(
                                new UseBalance.Request(1324L, "1111111111", 1000L)
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.transactionResultType").value("S"))
                .andExpect(jsonPath("$.transactionId").value("IIDD"))
                .andExpect(jsonPath("$.amount").value(100L));

    }

    @Test
    void cancelBalance_success() throws Exception {
        //given
        given(transactionService.cancelBalance(anyString(), anyString(), anyLong()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1234567890")
                        .transactionType(CANCEL)
                        .transactionResultType(S)
                        .amount(10330L)
                        .transactionId("IIDD")
                        .transactedAt(LocalDateTime.now())
                        .build()
                );
        //when
        //then
        mockMvc.perform(post("/transaction/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectmapper.writeValueAsString(
                                new CancelBalance.Request("IIDDsds", "1111111111", 1000L)
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.transactionResultType").value("S"))
                .andExpect(jsonPath("$.transactionId").value("IIDD"))
                .andExpect(jsonPath("$.amount").value(10330L));
    }
}