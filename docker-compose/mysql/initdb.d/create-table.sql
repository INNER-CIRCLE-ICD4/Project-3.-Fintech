-- 송금 이력 테이블
CREATE TABLE transfer
(
    id                 char(13) PRIMARY KEY COMMENT "id",
    sender_account_id  char(13)    NOT NULL COMMENT "송금 계좌 ID",
    receive_account_id char(13)    NOT NULL COMMENT "출금 계좌 ID",
    amount             BIGINT      NOT NULL COMMENT "송금 금액",
    status             varchar(30) NOT NULL COMMENT "이체 상태(PENDING: 대기, SUCCESS: 성공, FAILED: 실패, CANCELLED: 취소)",
    scheduled_at       DATETIME COMMENT "예약 송금 일자",
    requested_at       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "송금 요청 일자",
    completed_at       DATETIME COMMENT "완료 일자",
    reason             VARCHAR(255) COMMENT "사유",

    -- 추후 account 외래키 추가
    INDEX (sender_account_id),
    INDEX (receive_account_id)
);

-- 입출금 이력 테이블
CREATE TABLE transaction_history
(
    id            varchar(13) PRIMARY KEY COMMENT "id",
    account_id    char(13)    NOT NULL COMMENT "계좌 ID",
    tx_type       varchar(30) NOT NULL COMMENT "거래 타입(DEPOSIT: 입금, WITHDRAW: 출금)", -- ('DEPOSIT', 'WITHDRAW')
    amount        bigint      NOT NULL COMMENT "금액",
    balance_after bigint      NOT NULL COMMENT "거래 후 금액",
    description   varchar(255) COMMENT "설명",
    transfer_id   char(13) COMMENT "이체 내역 ID",                                     -- transfer.id (nullable)
    created_at    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "생성일자",

    INDEX (account_id, created_at),
    INDEX (transfer_id)
);
