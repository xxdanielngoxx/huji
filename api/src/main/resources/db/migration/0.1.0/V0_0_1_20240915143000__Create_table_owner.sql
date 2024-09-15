CREATE
    TABLE
        "owner"(
            id UUID NOT NULL,
            full_name VARCHAR(255) NOT NULL,
            CONSTRAINT pk_owner PRIMARY KEY(id)
        );

ALTER TABLE
    "owner" ADD CONSTRAINT FK_OWNER_ON_ID FOREIGN KEY(id) REFERENCES "user"(id);