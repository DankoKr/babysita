CREATE TABLE poster
(
    id   int     NOT NULL AUTO_INCREMENT,
    title varchar(50) NOT NULL,
    description varchar(500) NOT NULL,
    imageUrl varchar(500) NOT NULL,
    eventDate   DATE,
    isAppointed BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);
