USE expert;


DROP PROCEDURE IF EXISTS generate_random_string;
DROP PROCEDURE IF EXISTS generate_users;

DELIMITER $$

CREATE PROCEDURE generate_random_string(
    IN str_length INT,
    OUT result VARCHAR(255)
)
BEGIN
    DECLARE chars VARCHAR(62) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    DECLARE i INT DEFAULT 1;
    DECLARE result_string VARCHAR(255) DEFAULT '';

    WHILE i <= str_length DO
            SET result_string = CONCAT(result_string, SUBSTRING(chars, FLOOR(1 + RAND() * 62), 1));
            SET i = i + 1;
        END WHILE;

    SET result = result_string;
END $$

CREATE PROCEDURE generate_users(IN num_users INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE email_domain VARCHAR(20);
    DECLARE random_role VARCHAR(10);
    DECLARE random_string VARCHAR(255);

    -- 닉네임 생성을 위한 단어 배열
    DECLARE adjectives TEXT DEFAULT 'Happy,Brave,Clever,Gentle,Swift,Wise,Mighty,Bright,Noble,Wild,Calm,Bold,Lucky,Kind,Pure,Free,Fair,Proud,Elite,Smart';
    DECLARE nouns TEXT DEFAULT 'Lion,Tiger,Eagle,Wolf,Bear,Fox,Hawk,Deer,Owl,Dragon,Phoenix,Knight,Warrior,Hero,King,Queen,Sage,Master,Legend,Star';
    DECLARE colors TEXT DEFAULT 'Red,Blue,Green,Gold,Silver,Black,White,Purple,Pink,Orange,Yellow,Bronze,Crystal,Ruby,Jade,Azure,Crimson,Violet,Amber,Coral';

    -- 성능 향상을 위해 자동 커밋 비활성화
    SET autocommit = 0;
    SET email_domain = '@example.com';

    START TRANSACTION;

    WHILE i <= num_users DO
            -- 랜덤하게 role 선택 (대부분 USER, 소수 ADMIN)
            IF RAND() < 0.05 THEN
                SET random_role = 'ROLE_ADMIN';
            ELSE
                SET random_role = 'ROLE_USER';
            END IF;

            -- 랜덤 닉네임 생성
            CALL generate_random_string(4, random_string);

            -- 닉네임 생성 및 해시값 계산
            SET @nickname = CONCAT(
                    SUBSTRING_INDEX(SUBSTRING_INDEX(adjectives, ',', FLOOR(1 + RAND() * 20)), ',', -1),
                    SUBSTRING_INDEX(SUBSTRING_INDEX(colors, ',', FLOOR(1 + RAND() * 20)), ',', -1),
                    SUBSTRING_INDEX(SUBSTRING_INDEX(nouns, ',', FLOOR(1 + RAND() * 20)), ',', -1),
                    random_string
                            );

            -- 닉네임의 해시값 계산
            SET @nickname_hash = ABS(CAST(CONV(SUBSTRING(MD5(@nickname), 1, 8), 16, 10) AS SIGNED));

            -- 데이터 삽입
            INSERT INTO users (
                created_at,
                modified_at,
                email,
                image_url,
                nickname,
                nickname_hash,
                password,
                user_role
            )
            VALUES (
                       DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY),
                       DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY),
                       CONCAT('user', i, email_domain),
                       CONCAT('https://example.com/images/profile', i, '.jpg'),
                       @nickname,
                       @nickname_hash,
                       SHA2(CONCAT('password', i), 256),
                       random_role
                   );

            -- 매 1000건마다 커밋
            IF i % 1000 = 0 THEN
                COMMIT;
                START TRANSACTION;
                SELECT CONCAT('Processed: ', i, ' records');
            END IF;

            SET i = i + 1;
        END WHILE;

    -- 마지막 남은 데이터 커밋
    COMMIT;

    -- 자동 커밋 다시 활성화
    SET autocommit = 1;

    SELECT CONCAT('Total ', num_users, ' users generated successfully.');
END $$

DELIMITER ;


CALL generate_users(1000000);

