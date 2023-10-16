CREATE TABLE `users`
(
    id           int         NOT NULL AUTO_INCREMENT,
    username     varchar(20) NOT NULL,
    password     varchar(255) NOT NULL,
    email        varchar(50) NOT NULL,
    first_name    varchar(50),
    last_name     varchar(50),
    profile_image varchar(500),
    phone_number  varchar(50),
    address      varchar(50),
    role         varchar(50) NOT NULL,
    age          int,
    PRIMARY KEY (id),
    UNIQUE (username)
);

CREATE TABLE `parents`
(
    user_id int NOT NULL PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE `babysitters`
(
    user_id      int         NOT NULL PRIMARY KEY,
    gender      varchar(20),
    points      int,
    is_available boolean NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE `posters`
(
    id           int     NOT NULL AUTO_INCREMENT,
    title        varchar(50) NOT NULL,
    description  varchar(500) NOT NULL,
    image_url     varchar(500) NOT NULL,
    event_date    DATE,
    parent_id     int     NOT NULL,
    babysitter_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (parent_id) REFERENCES parents (user_id),
    FOREIGN KEY (babysitter_id) REFERENCES babysitters (user_id)
);
