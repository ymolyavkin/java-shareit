CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                     name VARCHAR(255) NOT NULL,
                                     email VARCHAR(512) NOT NULL,
                                     CONSTRAINT PK_USER PRIMARY KEY (id),
                                     CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS requests (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        description VARCHAR(512) NOT NULL,
                                        requestor_id BIGINT NOT NULL,
                                        CONSTRAINT PK_REQUEST PRIMARY KEY (id),
                                        CONSTRAINT FK_REQUESTOR_ID FOREIGN KEY(requestor_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS items (
                                     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                     name VARCHAR(255) NOT NULL,
                                     description VARCHAR(512) NOT NULL,
                                     available BOOLEAN NOT NULL,
                                     owner_id BIGINT NOT NULL,
                                     request_id INTEGER,
                                     number_of_times_to_rent INTEGER DEFAULT 0,
                                     CONSTRAINT PK_ITEM PRIMARY KEY (id),
                                     CONSTRAINT FK_OWNER_ID FOREIGN KEY(owner_id) REFERENCES users(id),
                                     CONSTRAINT FK_REQUEST_ID FOREIGN KEY(request_id) REFERENCES requests(id)
);
CREATE TABLE IF NOT EXISTS bookings (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        item_id BIGINT NOT NULL,
                                        booker_id BIGINT NOT NULL,
                                        status VARCHAR(8),
                                        CONSTRAINT PK_BOOKING PRIMARY KEY (id),
                                        CONSTRAINT FK_ITEM_ID FOREIGN KEY(item_id) REFERENCES items(id),
                                        CONSTRAINT FK_BOOKER_ID FOREIGN KEY(booker_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        text VARCHAR(512) NOT NULL,
                                        item_id BIGINT NOT NULL,
                                        author_id BIGINT NOT NULL,
                                        created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        CONSTRAINT PK_COMMENT PRIMARY KEY (id),
                                        CONSTRAINT FK_ITEMS_ID FOREIGN KEY(item_id) REFERENCES items(id),
                                        CONSTRAINT FK_AUTHOR_ID FOREIGN KEY(author_id) REFERENCES users(id)
);