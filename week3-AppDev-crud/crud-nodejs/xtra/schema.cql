// User credentials, keyed by email address so we can authenticate
CREATE TABLE IF NOT EXISTS user_credentials (
    email     text,
    password  text,
    userid    uuid,
    PRIMARY KEY (email)
);