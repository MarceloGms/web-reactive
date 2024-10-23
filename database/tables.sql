-- Drop existing tables if they exist
DROP TABLE IF EXISTS media_users CASCADE;
DROP TABLE IF EXISTS media CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create media table
CREATE TABLE media (
    identifier      BIGSERIAL PRIMARY KEY,
    title           VARCHAR(512) NOT NULL,
    release_date    DATE,
    average_rating  FLOAT(8) CHECK (0 <= average_rating AND average_rating <= 10),
    type            BOOLEAN NOT NULL
);

-- Create users table
CREATE TABLE users (
    identifier BIGSERIAL PRIMARY KEY,
    name       VARCHAR(512) NOT NULL,
    age        SMALLINT CHECK (age >= 0),
    gender     VARCHAR(512) CHECK (gender IN ('M', 'F'))
);

-- Create media_users table to establish a many-to-many relationship
CREATE TABLE media_users (
    media_identifier BIGINT NOT NULL,
    users_identifier BIGINT NOT NULL,
    PRIMARY KEY (media_identifier, users_identifier),
    FOREIGN KEY (media_identifier) REFERENCES media (identifier) ON DELETE CASCADE,
    FOREIGN KEY (users_identifier) REFERENCES users (identifier) ON DELETE CASCADE
);
