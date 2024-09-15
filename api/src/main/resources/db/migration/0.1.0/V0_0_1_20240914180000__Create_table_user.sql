CREATE
    TABLE
        "user"(
            id UUID NOT NULL,
            username VARCHAR(255),
            password VARCHAR(255),
            phone_number VARCHAR(255),
            ROLE VARCHAR(255) NOT NULL,
            CONSTRAINT pk_user PRIMARY KEY(id)
        );

ALTER TABLE
    "user" ADD CONSTRAINT uc_user_phone_number UNIQUE(phone_number);

ALTER TABLE
    "user" ADD CONSTRAINT uc_user_username UNIQUE(username);