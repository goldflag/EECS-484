CREATE TABLE USERS
        (USER_ID NUMBER,
        FIRST_NAME VARCHAR2(100) NOT NULL,
        LAST_NAME VARCHAR2(100) NOT NULL,
        YEAR_OF_BIRTH INTEGER,
        MONTH_OF_BIRTH INTEGER,
        DAY_OF_BIRTH INTEGER,
        GENDER VARCHAR2(100),
        CONSTRAINT user_id_key PRIMARY KEY (USER_ID));
        
CREATE TABLE FRIENDS
        (USER1_ID NUMBER NOT NULL,
        USER2_ID NUMBER NOT NULL,
        CONSTRAINT no_dupe_friends UNIQUE (USER1_ID, USER2_ID),
        CONSTRAINT foreign_USER1_ID
            FOREIGN KEY (USER1_ID)
            REFERENCES USERS(USER_ID)
            ON DELETE CASCADE,
        CONSTRAINT foreign_USER2_ID
            FOREIGN KEY (USER2_ID)
            REFERENCES USERS(USER_ID)
            ON DELETE CASCADE
        );

CREATE TRIGGER order_friends_pairs
    BEFORE INSERT ON FRIENDS
    FOR EACH ROW
        DECLARE temp NUMBER;
        BEGIN
            IF :NEW.USER1_ID > :NEW.USER2_ID THEN
                temp := :NEW.USER2_ID;
                :NEW.USER2_ID := :NEW.USER1_ID;
                :NEW.USER1_ID := temp;
            END IF ;

        END;
/

CREATE TABLE CITIES
        (CITY_ID INTEGER GENERATED AS IDENTITY,
        CITY_NAME VARCHAR2(100) NOT NULL,
        STATE_NAME VARCHAR2(100) NOT NULL,
        COUNTRY_NAME VARCHAR2(100) NOT NULL,
        CONSTRAINT no_dupe_cities UNIQUE (CITY_NAME, STATE_NAME, COUNTRY_NAME),
        PRIMARY KEY (CITY_ID));

CREATE TABLE USER_CURRENT_CITIES
        (USER_ID NUMBER UNIQUE NOT NULL,
        CURRENT_CITY_ID INTEGER NOT NULL,
        CONSTRAINT foreign_cities1
            FOREIGN KEY (CURRENT_CITY_ID)
            REFERENCES CITIES(CITY_ID)
            ON DELETE CASCADE,
        CONSTRAINT foreign_user1
            FOREIGN KEY (USER_ID)
            REFERENCES USERS(USER_ID)
            ON DELETE CASCADE
        );

CREATE TABLE USER_HOMETOWN_CITIES
        (USER_ID NUMBER UNIQUE NOT NULL,
        HOMETOWN_CITY_ID INTEGER  NOT NULL,
        CONSTRAINT foreign_cities2
            FOREIGN KEY (HOMETOWN_CITY_ID)
            REFERENCES CITIES(CITY_ID)
            ON DELETE CASCADE,
        CONSTRAINT foreign_user2
            FOREIGN KEY (USER_ID)
            REFERENCES USERS(USER_ID)
            ON DELETE CASCADE
        );

CREATE TABLE MESSAGES
        (MESSAGE_ID NUMBER PRIMARY KEY,
        SENDER_ID NUMBER NOT NULL,
        RECEIVER_ID NUMBER NOT NULL,
        MESSAGE_CONTENT VARCHAR2(2000) NOT NULL,
        SENT_TIME TIMESTAMP NOT NULL,
        CONSTRAINT foreign_sender
            FOREIGN KEY (SENDER_ID)
            REFERENCES USERS(USER_ID)
            ON DELETE CASCADE,
        CONSTRAINT foreign_reciever
            FOREIGN KEY (RECEIVER_ID)
            REFERENCES USERS(USER_ID)
            ON DELETE CASCADE
        );
        
CREATE TABLE PROGRAMS
        (PROGRAM_ID INTEGER GENERATED AS IDENTITY,
        INSTITUTION VARCHAR2(100) NOT NULL,
        CONCENTRATION VARCHAR2(100) NOT NULL,
        DEGREE VARCHAR2(100) NOT NULL,
        CONSTRAINT no_dupe_programs UNIQUE (INSTITUTION, CONCENTRATION, DEGREE),
        CONSTRAINT program_key PRIMARY KEY (PROGRAM_ID));

CREATE TABLE EDUCATION
        (USER_ID NUMBER NOT NULL,
        PROGRAM_ID INTEGER NOT NULL,
        PROGRAM_YEAR INTEGER NOT NULL,
        CONSTRAINT no_dupe_user_programs UNIQUE (USER_ID, PROGRAM_ID),
        CONSTRAINT foreign_USER_ID
            FOREIGN KEY (USER_ID)
            REFERENCES USERS(USER_ID)
            ON DELETE CASCADE,
        CONSTRAINT foreign_PROGRAM_ID
            FOREIGN KEY (PROGRAM_ID)
            REFERENCES PROGRAMS(PROGRAM_ID)
            ON DELETE CASCADE
);

CREATE TABLE USER_EVENTS
        (EVENT_ID NUMBER,
        EVENT_CREATOR_ID NUMBER NOT NULL,
        EVENT_NAME VARCHAR2(100) NOT NULL,
        EVENT_TAGLINE VARCHAR2(100),
        EVENT_DESCRIPTION VARCHAR2(100),
        EVENT_HOST VARCHAR2(100),
        EVENT_TYPE VARCHAR2(100),
        EVENT_SUBTYPE VARCHAR2(100),
        EVENT_ADDRESS VARCHAR2(2000),
        EVENT_CITY_ID INTEGER NOT NULL,
        EVENT_START_TIME TIMESTAMP,
        EVENT_END_TIME TIMESTAMP,
        CONSTRAINT event_key PRIMARY KEY (EVENT_ID),
        CONSTRAINT foreign_creator
            FOREIGN KEY (EVENT_CREATOR_ID)
            REFERENCES USERS(USER_ID)
            ON DELETE CASCADE,
        CONSTRAINT city_location
            FOREIGN KEY (EVENT_CITY_ID)
            REFERENCES CITIES(CITY_ID)
            ON DELETE CASCADE
        );

CREATE TABLE PARTICIPANTS
        (EVENT_ID NUMBER NOT NULL,
        USER_ID NUMBER NOT NULL,
        CONFIRMATION VARCHAR2(100) CHECK( CONFIRMATION IN ('ATTENDING','UNSURE','DECLINES', 'NOT_REPLIED') ) NOT NULL,
        CONSTRAINT partcipant
            FOREIGN KEY (USER_ID)
            REFERENCES USERS(USER_ID)
            ON DELETE CASCADE,
        CONSTRAINT foreign_event
            FOREIGN KEY (EVENT_ID)
            REFERENCES USER_EVENTS(EVENT_ID)
            ON DELETE CASCADE,
        PRIMARY KEY (EVENT_ID, USER_ID)
        );

CREATE TABLE ALBUMS
        (ALBUM_ID NUMBER,
        ALBUM_OWNER_ID NUMBER NOT NULL,
        ALBUM_NAME VARCHAR2(100) NOT NULL,
        ALBUM_CREATED_TIME TIMESTAMP NOT NULL,
        ALBUM_MODIFIED_TIME TIMESTAMP,
        ALBUM_LINK VARCHAR2(100) NOT NULL,
        ALBUM_VISIBILITY VARCHAR2(100) CHECK( ALBUM_VISIBILITY IN ('EVERYONE','FRIENDS','FRIENDS_OF_FRIENDS', 'MYSELF') ),
        COVER_PHOTO_ID NUMBER NOT NULL,
        CONSTRAINT album_key PRIMARY KEY (ALBUM_ID),
        CONSTRAINT foreign_owner
            FOREIGN KEY (ALBUM_OWNER_ID)
            REFERENCES USERS(USER_ID)
            ON DELETE CASCADE
        );

CREATE TABLE PHOTOS
        (PHOTO_ID NUMBER NOT NULL,
        ALBUM_ID NUMBER NOT NULL,
        PHOTO_CAPTION VARCHAR2(2000),
        PHOTO_CREATED_TIME TIMESTAMP NOT NULL,
        PHOTO_MODIFIED_TIME TIMESTAMP,
        PHOTO_LINK VARCHAR2(2000) NOT NULL,
        CONSTRAINT photo_key PRIMARY KEY (PHOTO_ID),
        CONSTRAINT foreign_album
            FOREIGN KEY (ALBUM_ID)
            REFERENCES ALBUMS(ALBUM_ID)
            ON DELETE CASCADE
        INITIALLY DEFERRED DEFERRABLE);

CREATE TABLE TAGS
        (TAG_PHOTO_ID NUMBER NOT NULL,
        TAG_SUBJECT_ID NUMBER NOT NULL,
        TAG_CREATED_TIME TIMESTAMP NOT NULL,
        TAG_X NUMBER NOT NULL,
        TAG_Y NUMBER NOT NULL,
        PRIMARY KEY (TAG_PHOTO_ID, TAG_SUBJECT_ID),
        CONSTRAINT foreign_tag_photo2
            FOREIGN KEY (TAG_PHOTO_ID)
            REFERENCES PHOTOS(PHOTO_ID)
            ON DELETE CASCADE,
        CONSTRAINT foreign_tag_person
            FOREIGN KEY (TAG_SUBJECT_ID)
            REFERENCES USERS(USER_ID)
            ON DELETE CASCADE
        );


ALTER TABLE ALBUMS
ADD CONSTRAINT foreign_cover_photo
FOREIGN KEY (COVER_PHOTO_ID)
REFERENCES PHOTOS(PHOTO_ID)
INITIALLY DEFERRED DEFERRABLE;