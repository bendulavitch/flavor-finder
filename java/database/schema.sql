BEGIN TRANSACTION;


DROP TABLE IF EXISTS skipped;
DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(200) NOT NULL,
    role VARCHAR(50) NOT NULL
);


CREATE TABLE restaurants (
    restaurant_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    rating FLOAT CHECK (rating BETWEEN 0 AND 5),
    distance FLOAT CHECK (distance >= 0),
    kid_friendly BOOLEAN NOT NULL
);


CREATE TABLE favorites (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    endpoint VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, endpoint),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);



CREATE TABLE skipped (
    skipped_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    CONSTRAINT FK_skipped_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT FK_skipped_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id) ON DELETE CASCADE,
    UNIQUE (user_id, restaurant_id)
);

COMMIT TRANSACTION;
