BEGIN TRANSACTION;


DROP TABLE IF EXISTS skipped;
DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS vote_sessions;
DROP TABLE IF EXISTS restaurant_options;
DROP TABLE IF EXISTS user_votes;


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

CREATE TABLE vote_sessions (
    id SERIAL PRIMARY KEY,
    creator_username VARCHAR(50) NOT NULL,
    query_type VARCHAR(20) NOT NULL,
    query_value VARCHAR(100) NOT NULL,
    room_code VARCHAR(10) UNIQUE NOT NULL,
    round INT NOT NULL DEFAULT 1, -- Tracks current round (1 or 2)
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE restaurant_options (
    id SERIAL PRIMARY KEY,
    vote_session_id INT NOT NULL REFERENCES vote_sessions(id),
    name VARCHAR(200) NOT NULL,
    place_id VARCHAR(200) NOT NULL,
    votes_round1 INT NOT NULL DEFAULT 0,
    votes_round2 INT NOT NULL DEFAULT 0
);

CREATE TABLE user_votes (
    id SERIAL PRIMARY KEY,
    vote_session_id INT NOT NULL REFERENCES vote_sessions(id),
    restaurant_option_id INT NOT NULL REFERENCES restaurant_options(id),
    username VARCHAR(50) NOT NULL,
    round INT NOT NULL, -- Which round the vote is for (1 or 2)
    in_favor BOOLEAN NOT NULL
);


COMMIT TRANSACTION;
