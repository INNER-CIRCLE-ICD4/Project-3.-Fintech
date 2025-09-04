-- users
-- tester1 -> password: password1
-- tester2 -> password: password2
INSERT INTO `sendy`.users
(id, password, name, phone_number, email, ci, birth, is_delete, email_verified, create_at, update_at, delete_at,
 is_locked, wrong_count)
VALUES (38352658567418872,
        '0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e',
        'tester1',
        'Cx/OjtBzh4LMoQnTi88/hNlXyGeIvZmq1gIM7CVvQT0=',
        'qpXdwvkn2fj8BjGIyhwHfBfONx/LzRjuenSYwkAzcuE=',
        'ci1',
        '19990123',
        0,
        1,
        now(),
        now(),
        null,
        false,
        0),
       (38352658567418873,
        '6cf615d5bcaac778352a8f1f3360d23f02f34ec182e259897fd6ce485d7870d4',
        'tester2',
        'G4ud4CMhyt0B/SdraVjGidVtH+r++Px1uplGXYyk/c4=',
        'NyfMsclCGx1QflWO5xFyM8LaPxhvFYdN3j/6rmaaCPc=',
        'ci2',
        '19991231',
        0,
        1,
        now(),
        now(),
        null, false, 0),
       (38352658567418874,
        '6cf615d5bcaac778352a8f1f3360d23f02f34ec182e259897fd6ce485d7870d4',
        'tester3',
        '01032141111',
        '4rGsE91CofxjfWSL/xUab9V4CFj5kktcS4GZkUiXoSE=',
        'ci2',
        '19991231',
        0,
        1,
        now(),
        now(),
        null,
        false,
        0);


-- transfer_limit(tester1)

-- account(tester1, tester2)
INSERT INTO `sendy`.account
    (id, account_number, user_id, password, status, balance)
VALUES (38352658567418874,
        '3210000000001',
        38352658567418872,
        '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4',
        'ACTIVE',
        1000),
       (38352658567418875,
        '3210000000002',
        38352658567418873,
        '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4',
        'ACTIVE',
        0),
       (38352658567418876,
        '3210000000003',
        38352658567418874,
        '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4',
        'ACTIVE',
        0);

-- transfer(reserve)
insert into `sendy`.transfer
(id, send_user_id, send_account_number, receive_phone_number, receive_account_number, amount, status, scheduled_at)
values (745937025742925543, 38352658567418872, '3210000000001', '01012341234', null,
        1000, 'RESERVE',
        '2025-08-20 18:05:00'),
       (745937025742925544, 38352658567418873, '3210000000002', null, '3210000000003', 1000, 'RESERVE',
        '2025-08-20 18:05:00');