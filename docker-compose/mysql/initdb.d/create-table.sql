-- 송금 이력 테이블
CREATE TABLE transfer
(
    id                 BIGINT PRIMARY KEY COMMENT "id",
    sender_account_id  BIGINT      NOT NULL COMMENT "송금 계좌 ID",
    receive_account_id BIGINT      NOT NULL COMMENT "출금 계좌 ID",
    amount             BIGINT      NOT NULL COMMENT "송금 금액",
    status             VARCHAR(30) NOT NULL COMMENT "이체 상태(PENDING: 대기, SUCCESS: 성공, FAILED: 실패, CANCELLED: 취소)",
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
    id            BIGINT PRIMARY KEY COMMENT "id",
    account_id    BIGINT      NOT NULL COMMENT "계좌 ID",
    tx_type       VARCHAR(30) NOT NULL COMMENT "거래 타입(DEPOSIT: 입금, WITHDRAW: 출금)", -- ('DEPOSIT', 'WITHDRAW')
    amount        BIGINT      NOT NULL COMMENT "금액",
    balance_after BIGINT      NOT NULL COMMENT "거래 후 금액",
    description   VARCHAR(255) COMMENT "설명",
    transfer_id   CHAR(13) COMMENT "이체 내역 ID",                                     -- transfer.id (nullable)
    created_at    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "생성일자",

    INDEX (account_id, created_at),
    INDEX (transfer_id)
);

-- 사용자 테이블
CREATE TABLE users (
       user_id BIGINT NOT NULL PRIMARY KEY,
       password VARCHAR(100) NOT NULL,
       name VARCHAR(50) NOT NULL,
       phone_number VARCHAR(20) NOT NULL,
       email VARCHAR(255) NOT NULL,
       ci VARCHAR(100),
       birth CHAR(8) NOT NULL,
       is_delete BOOLEAN NOT NULL DEFAULT false,
       email_verified BOOLEAN NOT NULL DEFAULT false,
       create_at TIMESTAMP NOT NULL,
       update_at TIMESTAMP,
       delete_at TIMESTAMP
);

-- 사용자 메일인증 발송기록 테이블
CREATE TABLE email_auth (
    email_id BIGINT NOT NULL PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    is_verified BOOLEAN NOT NULL
);