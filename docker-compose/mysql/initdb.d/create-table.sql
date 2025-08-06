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
) engine = InnoDB;

-- 이체 한도 관리 테이블
CREATE TABLE transfer_limit
(
    id                       bigint PRIMARY KEY COMMENT "id",
    daily_dt                 varchar(8) NOT NULL COMMENT "일자(8자리)",
    daily_limit              bigint     NOT NULL COMMENT "일일 최대 한도",
    single_transaction_limit bigint     NOT NULL COMMENT "1회 이체시 최대 한도",
    daily_count              bigint     NOT NULL COMMENT "일일 이체 횟수",
    created_at               DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "생성 일자",
    updated_at               DATETIME   NULL     DEFAULT CURRENT_TIMESTAMP COMMENT "수정 일자",
    user_id                  bigint     NULL,

    -- 추후 account 외래키 추가
    INDEX (user_id),
    INDEX (daily_dt),
    INDEX (daily_dt, user_id),
    UNIQUE (daily_dt, user_id)
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
    transfer_id   BIGINT COMMENT "이체 내역 ID",                                       -- transfer.id (nullable)
    created_at    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "생성일자",

    INDEX (account_id, created_at),
    INDEX (transfer_id)
) engine = InnoDB;

-- 사용자 테이블
CREATE TABLE users
(
    id             BIGINT       NOT NULL PRIMARY KEY,
    password       VARCHAR(100) NOT NULL,
    name           VARCHAR(50)  NOT NULL,
    phone_number   VARCHAR(20)  NOT NULL,
    email          VARCHAR(255) NOT NULL,
    ci             VARCHAR(100),
    birth          CHAR(8)      NOT NULL,
    is_delete      TINYINT(1)   NOT NULL,
    email_verified TINYINT(1)   NOT NULL DEFAULT 0,
    create_at      TIMESTAMP    NOT NULL,
    update_at      TIMESTAMP,
    delete_at      TIMESTAMP,
    UNIQUE KEY `users_phone_number_uk` (phone_number)
) engine = InnoDB;

-- 사용자 메일인증 발송기록 테이블
CREATE TABLE email
(
    id          BIGINT       NOT NULL PRIMARY KEY,
    code        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    is_verified TINYINT(1)   NOT NULL DEFAULT 0,
    user_id     BIGINT       NOT NULL,
    send_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
) engine = InnoDB;

-- 계좌 테이블
CREATE TABLE account
(
    id                 BIGINT      NOT NULL PRIMARY KEY,
    account_number     VARCHAR(13) NOT NULL,
    user_id            BIGINT      NOT NULL,
    password           VARCHAR(64) NOT NULL,
    status             VARCHAR(20) NOT NULL,
    is_primary         BOOLEAN     NOT NULL,
    is_limited_account BOOLEAN     NOT NULL,
    created_at         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    balance            BIGINT      NOT NULL,

    UNIQUE KEY `account_account_number_uk` (account_number)
) engine = InnoDB;

-- jwt 토큰 정보
create table jwt_token
(
    created_at datetime(6)  not null,
    device_id  bigint,
    expired_at datetime(6)  not null,
    token_id   bigint       not null auto_increment,
    updated_at datetime(6),
    user_id    bigint       not null,
    token_hash varchar(500) not null,
    status     varchar(30)  not null, -- ('ACTIVE','PENDING_LOGOUT','REVOKED')
    token_type varchar(30),           -- ('ACCESS','REFRESH')
    primary key (token_id)
) engine = InnoDB;

-- 디바이스 정보
create table device_info
(
    is_mobile          BOOLEAN DEFAULT false not null,
    created_at         datetime(6)           not null,
    device_id          bigint                not null auto_increment,
    last_login_at      datetime(6)           not null,
    updated_at         datetime(6),
    user_id            bigint                not null,
    language           varchar(10),
    screen_resolution  varchar(20),
    ip_address         varchar(50),
    timezone           varchar(50),
    device_name        varchar(100),
    os_info            varchar(100),
    browser_info       varchar(200),
    user_agent         varchar(500),
    device_fingerprint varchar(255)          not null,
    primary key (device_id)
) engine = InnoDB;