BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);
CREATE TABLE restaurants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    image VARCHAR(255),
    rating FLOAT,
    distance FLOAT,
    kid_friendly BOOLEAN
);

CREATE TABLE favorites (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    restaurant_id INT REFERENCES restaurants(id)
);

CREATE TABLE skipped (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    restaurant_id INT REFERENCES restaurants(id)
);
COMMIT TRANSACTION;
