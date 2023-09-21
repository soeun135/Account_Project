package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.domain.Transaction;
import com.example.account.dto.TransactionDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.repository.TransactionRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.account.type.TransactionResultType.*;
import static com.example.account.type.TransactionType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountUserRepository accountUserRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void useBalance_success() {
        //given
        AccountUser user = AccountUser.builder()
                .id(24L)
                .name("soni").build();
        Account account = Account.builder()
                .accountUser(user)
                .balance(10000L)
                .accountStatus(AccountStatus.IN_USE)
                .accountNumber("1000000035")
                .build();

        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));

        given(transactionRepository.save(any()))
                .willReturn(Transaction.builder()
                        .transactionType(USE)
                        .account(account)
                        .transactionResultType(S)
                        .balanceSnapshot(9000L)
                        .amount(10000L)
                        .build());
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);

        //when
        TransactionDto transactionDto = transactionService.useBalance(1L, "100000002", 4000L);

        //then
        verify(transactionRepository,times(1)).save(captor.capture());
        assertEquals(4000L, captor.getValue().getAmount());
        assertEquals(6000L, captor.getValue().getBalanceSnapshot());
        assertEquals(S, transactionDto.getTransactionResultType());
        assertEquals(USE, transactionDto.getTransactionType());
        assertEquals(9000L, transactionDto.getBalanceSnapshot());
        assertEquals(10000L, transactionDto.getAmount());
    }
    @Test
    @DisplayName("해당 유저 없음 - 잔액 사용 실패")
    void useBalance_UserNotFound() {
        //given
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.useBalance(1L, "4444444444",1003L));

        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
    @Test
    @DisplayName("해당 계좌 없음 - 잔액 사용 실패")
    void useBalance_AccountNotFound() {
        //given
        AccountUser user = AccountUser.builder()
                .id(234L)
                .name("soni").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(any()))
                .willReturn(Optional.empty());
        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.useBalance(1L, "1111", 1003L));

        //then
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("계좌 소유주 다름 - 잔액 사용 실패")
    void useBalance_userUnMatch() {
        //given
        AccountUser soni = AccountUser.builder()
                .id(234L)
                .name("soni").build();
        AccountUser bunny = AccountUser.builder()
                .id(15L)
                .name("bunny").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(soni));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(Account.builder()
                        .accountUser(bunny)
                        .balance(0L)
                        .accountNumber("1000000035")
                        .build()));

        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.useBalance(1L, "1111", 1003L));

        //then
        assertEquals(ErrorCode.USER_ACCOUNT_UN_MATCH, exception.getErrorCode());
    }

    @Test
    @DisplayName("계좌 이미 해지 되어있을 때 - 잔액 사용 실패")
    void useBalance_alreadyUnregistered() {
        //given
        AccountUser soni = AccountUser.builder()
                .id(234L)
                .name("soni").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(soni));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(Account.builder()
                        .accountUser(soni)
                        .balance(0L)
                        .accountStatus(AccountStatus.UNREGISTERED)
                        .accountNumber("1000000035")
                        .build()));

        //when
        AccountException exception = assertThrows(AccountException.class,
                () ->transactionService.useBalance(1L, "1111", 1003L));

        //then
        assertEquals(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED, exception.getErrorCode());
    }

    @Test
    @DisplayName("거래금액이 잔액보다 큰 경우 - 잔액 사용 실패")
    void useBalance_balanceIsSmallerThanAmount() {
        //given
        AccountUser soni = AccountUser.builder()
                .id(234L)
                .name("soni").build();
        Account account = Account.builder()
                .accountUser(soni)
                .balance(10L)
                .accountStatus(AccountStatus.IN_USE)
                .accountNumber("1000000035")
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(soni));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));

        //when
        AccountException exception = assertThrows(AccountException.class,
                () ->transactionService.useBalance(1L, "1111", 10000L));

        //then
        verify(transactionRepository, times(0)).save(any());
        assertEquals(ErrorCode.BALANCE_IS_SMALLER_THAN_AMOUNT, exception.getErrorCode());
    }

    @Test
    @DisplayName("실패 트랜잭션 저장 성공")
    void saveFailedUseTransaction() {
        //given
        AccountUser user = AccountUser.builder()
                .id(24L)
                .name("soni").build();
        Account account = Account.builder()
                .accountUser(user)
                .balance(10000L)
                .accountStatus(AccountStatus.IN_USE)
                .accountNumber("1000000035")
                .build();
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));
        given(transactionRepository.save(any()))
                .willReturn(Transaction.builder()
                        .transactionType(USE)
                        .account(account)
                        .transactionResultType(S)
                        .balanceSnapshot(9000L)
                        .amount(10000L)
                        .build());
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);

        //when
        transactionService.saveFailedUseTransaction("100000002", 4000L);

        //then
        verify(transactionRepository,times(1)).save(captor.capture());
        assertEquals(4000L, captor.getValue().getAmount());
        assertEquals(10000L, captor.getValue().getBalanceSnapshot());
        assertEquals(F, captor.getValue().getTransactionResultType());
    }
}