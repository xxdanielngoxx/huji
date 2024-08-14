CREATE
    TABLE
        owner(
            id UUID NOT NULL,
            full_name VARCHAR(255) NOT NULL,
            phone_number VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            CONSTRAINT pk_owner PRIMARY KEY(id)
        );

ALTER TABLE
    owner ADD CONSTRAINT uc_owner_email UNIQUE(email);

ALTER TABLE
    owner ADD CONSTRAINT uc_owner_phone_number UNIQUE(phone_number);