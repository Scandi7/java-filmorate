CREATE TABLE IF NOT EXISTS mpa_ratings
(
    mpa_id      INT AUTO_INCREMENT PRIMARY KEY,
    rating      VARCHAR NOT NULL,
    description VARCHAR,
    /*CONSTRAINT unique_rating_name UNIQUE (rating)*/
);

CREATE TABLE IF NOT EXISTS films
(
    film_id      INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR NOT NULL,
    description  VARCHAR(200),
    release_date DATE    NOT NULL,
    duration     INT     NOT NULL,
    mpa_id       INT,
    FOREIGN KEY (mpa_id) REFERENCES mpa_ratings (mpa_id)
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR NOT NULL,
    /*CONSTRAINT unique_genre_name UNIQUE (name)*/
);

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id  INT,
    genre_id INT,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  INT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR NOT NULL,
    login    VARCHAR NOT NULL,
    name     VARCHAR,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS likes
(
    film_id INT,
    user_id INT,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS friendships
(
    user_id   INT,
    friend_id INT,
    status    VARCHAR,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (friend_id) REFERENCES users (user_id)
);