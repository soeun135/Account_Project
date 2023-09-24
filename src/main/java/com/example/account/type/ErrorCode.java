package com.example.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    USER_NOT_FOUND("사용자가 없습니다."),
    ACCOUNT_NOT_FOUND("계좌가 없습니다."),
    TRANSACTION_NOT_FOUND("거래가 없습니다!"),
    MAX_ACCOUNT_PER_USER_10("사용자 최대 계좌는 10개입니다."),
    USER_ACCOUNT_UN_MATCH("사용자와 계좌의 소유주가 다릅니다."),
    ACCOUNT_ALREADY_UNREGISTERED("이미 해지된 계좌입니다."),
    BALANCE_NOT_EMPTY("잔액이 있는 계좌 해지 불가"),
    BALANCE_IS_SMALLER_THAN_AMOUNT("거래 금액보다 잔액이 적다"),
    TRANSACTION_UN_MATCH_ACCOUNT("거래와 계좌 일치하지 않는다."),
    CANCEL_AMOUNT_UN_MATCH_USE_AMOUNT("거래 금액과 거래 취소 금액이 다릅니다."),
    TOO_OLD_TRANSACTION("1년이 지난 거래는 취소가 불가합니다.");
    private final String description;

}
