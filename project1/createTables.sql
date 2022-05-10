CREATE TABLE USERS
        (USER_ID NUMBER PRIMARY KEY,
        FIRST_NAME VARCHAR2(100) NOT NULL,
        LAST_NAME VARCHAR2(100) NOT NULL,
        YEAR_OF_BIRTH INTEGER,
        MONTH_OF_BIRTH INTEGER,
        DAY_OF_BIRTH INTEGER,
        GENER VARCHAR2(100));
        
CREATE TABLE FRIENDS
        (USER1_ID NUMBER NOT NULL,
        USER2_ID NUMBER NOT NULL);

CREATE TABLE CITIES
        (CITY_ID INTEGER PRIMARY KEY,
        CITY_NAME VARCHAR2(100) NOT NULL,
        STATE_NAME VARCHAR2(100) NOT NULL,
        COUNTRY_NAME VARCHAR2(100) NOT NULL);

CREATE TABLE USER_CURRENT_CITIES
        (USER_ID NUMBER,
        CURRENT_CITY_ID INTEGER);

CREATE TABLE USER_HOMETOWN_CITIES
        (USER_ID NUMBER,
        HOMETOWN_CITY_ID INTEGER);

CREATE TABLE MESSAGES
        (MESSAGE_ID NUMBER PRIMARY KEY,
        SENDER_ID NUMBER,
        RECEIVER_ID NUMBER,
        MESSAGE_CONTENT VARCHAR2(2000) NOT NULL,
        SENT_TIME TIMESTAMP NOT NULL);
        
CREATE TABLE PROGRAMS
        (PROGRAM_ID INTEGER PRIMARY KEY,
        INSTITUTION VARCHAR2(100) NOT NULL,
        CONCENTRATION VARCHAR2(100) NOT NULL,
        DEGREE VARCHAR2(100) NOT NULL);

CREATE TABLE EDUCATION
        (USER_ID NUMBER,
        PROGRAM_ID INTEGER,
        PROGRAM_YEAR INTEGER NOT NULL);

CREATE TABLE USER_EVENTS
        (EVENT_ID NUMBER PRIMARY KEY,
        EVENT_CREATOR_ID NUMBER,
        EVENT_NAME VARCHAR2(100) NOT NULL,
        EVENT_TAGLINE VARCHAR2(100),
        EVENT_DESCRIPTION VARCHAR2(100),
        EVENT_HOST VARCHAR2(100),
        EVENT_TYPE VARCHAR2(100),
        EVENT_SUBTYPE VARCHAR2(100),
        EVENT_ADDRESS VARCHAR2(2000),
        EVENT_CITY_ID INTEGER,
        EVENT_START_TIME TIMESTAMP,
        EVENT_END_TIME TIMESTAMP);

CREATE TABLE PARTICIPANTS
        (EVENT_ID NUMBER,
        USER_ID NUMBER,
        CONFIRMATION VARCHAR2(100));

CREATE TABLE ALBUMS
        (ALBUM_ID NUMBER PRIMARY KEY,
        ALBUM_OWNER_ID NUMBER,
        ALBUM_NAME VARCHAR2(100) NOT NULL,
        ALBUM_CREATED_TIME TIMESTAMP NOT NULL,
        ALBUM_MODIFIED_TIME TIMESTAMP,
        ALBUM_LINK VARCHAR2(100) NOT NULL,
        ALBUM_VISIBILTY VARCHAR2(100),
        COVER_PHOTO_ID NUMBER NOT NULL);

CREATE TABLE PHOTOS
        (PHOTO_ID NUMBER,
        ALBUM_ID NUMBER,
        PHOTO_CAPTION VARCHAR2(2000),
        PHOTO_CREATED_TIME TIMESTAMP NOT NULL,
        PHOTO_MODIFIED_TIME TIMESTAMP,
        PHOTO_LINK VARCHAR2(2000) NOT NULL);

CREATE TABLE TAGS
        (TAG_PHOTO_ID NUMBER,
        TAG_SUBJECT_ID NUMBER,
        TAG_CREATED_TIME TIMESTAMP
        TAG_X NUMBER NOT NULL,
        TAG_Y NUMBER NOT NULL);










