DROP TABLE IF EXISTS media_users;
DROP TABLE IF EXISTS media;
DROP TABLE IF EXISTS users;

CREATE TABLE media (
	identifier	 BIGSERIAL,
	title		 VARCHAR(512),
	release_date	 DATE,
	average_rating FLOAT(8),
	type		 BOOL,
	PRIMARY KEY(identifier)
);

CREATE TABLE users (
	identifier BIGSERIAL,
	name	 VARCHAR(512),
	age	 SMALLINT,
	gender	 BOOL,
	PRIMARY KEY(identifier)
);

CREATE TABLE media_users (
	media_identifier BIGINT,
	users_identifier BIGINT,
	PRIMARY KEY(media_identifier,users_identifier)
);

ALTER TABLE media_users ADD CONSTRAINT media_users_fk1 FOREIGN KEY (media_identifier) REFERENCES media(identifier);
ALTER TABLE media_users ADD CONSTRAINT media_users_fk2 FOREIGN KEY (users_identifier) REFERENCES users(identifier);

