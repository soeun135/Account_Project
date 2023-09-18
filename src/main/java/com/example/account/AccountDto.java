package com.example.account;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class AccountDto {
    private String accountNumber;
    private String nickname;
    private LocalDateTime registeredAt;

}