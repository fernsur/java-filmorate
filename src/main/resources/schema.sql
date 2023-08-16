DROP TABLE IF EXISTS PUBLIC.FILM_GENRE;
DROP TABLE IF EXISTS PUBLIC.FRIENDS;
DROP TABLE IF EXISTS PUBLIC.FILM_LIKE;
DROP TABLE IF EXISTS PUBLIC.FILMS;
DROP TABLE IF EXISTS PUBLIC.MPA_RATING;
DROP TABLE IF EXISTS PUBLIC.GENRES;
DROP TABLE IF EXISTS PUBLIC.USERS;

CREATE TABLE IF NOT EXISTS PUBLIC.MPA_RATING (
	MPA_RATING_ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(20) NOT NULL,
	CONSTRAINT MPA_RATING_PK PRIMARY KEY (MPA_RATING_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.GENRES (
	GENRE_ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(20) NOT NULL,
	CONSTRAINT GENRES_PK PRIMARY KEY (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
	USER_ID INTEGER NOT NULL AUTO_INCREMENT,
	EMAIL CHARACTER VARYING(50) NOT NULL,
	LOGIN CHARACTER VARYING(50) NOT NULL,
	NAME CHARACTER VARYING(50) NOT NULL,
	BIRTHDAY DATE NOT NULL,
	CONSTRAINT USERS_PK PRIMARY KEY (USER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILMS (
	FILM_ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(100) NOT NULL,
	DESCRIPTION CHARACTER VARYING(200) NOT NULL,
	RELEASE_DATE DATE NOT NULL,
	DURATION INTEGER NOT NULL,
	MPA_RATING_ID INTEGER NOT NULL,
	CONSTRAINT FILMS_PK PRIMARY KEY (FILM_ID),
    CONSTRAINT FILMS_FK FOREIGN KEY (MPA_RATING_ID) REFERENCES PUBLIC.MPA_RATING(MPA_RATING_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_GENRE (
	FILM_ID INTEGER NOT NULL,
	GENRE_ID INTEGER NOT NULL,
	CONSTRAINT FILM_GENRE_PK PRIMARY KEY (FILM_ID,GENRE_ID),
    CONSTRAINT FILM_GENRE_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID)
    ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FILM_GENRE_FK_1 FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRES(GENRE_ID)
	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_LIKE (
	FILM_ID INTEGER NOT NULL,
	USER_ID INTEGER NOT NULL,
	CONSTRAINT FILM_LIKE_PK PRIMARY KEY (FILM_ID,USER_ID),
    CONSTRAINT FILM_LIKE_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID)
    ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FILM_LIKE_FK_1 FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID)
	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDS (
	USER_ID INTEGER NOT NULL,
	FRIEND_ID INTEGER NOT NULL,
	CONSTRAINT FRIENDS_PK PRIMARY KEY (USER_ID,FRIEND_ID),
    CONSTRAINT FRIENDS_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID)
    ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FRIENDS_FK_1 FOREIGN KEY (FRIEND_ID) REFERENCES PUBLIC.USERS(USER_ID)
    ON DELETE CASCADE ON UPDATE CASCADE
);