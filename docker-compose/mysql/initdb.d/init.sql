-- users
-- tester1 -> password: password1
-- tester2 -> password: password2
INSERT INTO `sendy`.users
(id, password, name, phone_number, email, ci, birth, is_delete, email_verified, create_at, update_at, delete_at)
VALUES (38352658567418872,
        '0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e',
        'tester1',
        '01012341234',
        'test1@test.com',
        'ci1',
        '19990123',
        0,
        1,
        now(),
        now(),
        null),
       (38352658567418873,
        '6cf615d5bcaac778352a8f1f3360d23f02f34ec182e259897fd6ce485d7870d4',
        'tester2',
        '0104567890',
        'test2@test.com',
        'ci2',
        '19991231',
        0,
        1,
        now(),
        now(),
        null);


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
        0);