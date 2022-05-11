-- INSERT INTO USERS

CREATE VIEW users_view AS
SELECT
    USER_ID, 
    FIRST_NAME, 
    LAST_NAME, 
    YEAR_OF_BIRTH,
    MONTH_OF_BIRTH,
    DAY_OF_BIRTH, 
    GENDER
FROM 
    project1.PUBLIC_USER_INFORMATION
GROUP BY       
    USER_ID, 
    FIRST_NAME, 
    LAST_NAME, 
    YEAR_OF_BIRTH,
    MONTH_OF_BIRTH,
    DAY_OF_BIRTH, 
    GENDER;

INSERT INTO USERS (
    USER_ID, 
    FIRST_NAME, 
    LAST_NAME, 
    YEAR_OF_BIRTH, 
    MONTH_OF_BIRTH, 
    DAY_OF_BIRTH, 
    GENDER
)
SELECT 
    USER_ID, 
    FIRST_NAME, 
    LAST_NAME, 
    YEAR_OF_BIRTH,
    MONTH_OF_BIRTH,
    DAY_OF_BIRTH, 
    GENDER
FROM users_view;

-- INSERT INTO FRIENDS

INSERT INTO FRIENDS ( USER1_ID, USER2_ID ) 
SELECT USER1_ID, USER2_ID FROM project1.PUBLIC_ARE_FRIENDS;

-- INSERT INTO CITIES

INSERT INTO CITIES (
    CITIES.CITY_NAME, CITIES.STATE_NAME, CITIES.COUNTRY_NAME
)
SELECT 
    project1.PUBLIC_USER_INFORMATION.CURRENT_CITY, 
    project1.PUBLIC_USER_INFORMATION.CURRENT_STATE, 
    project1.PUBLIC_USER_INFORMATION.CURRENT_COUNTRY 
FROM project1.PUBLIC_USER_INFORMATION;

INSERT INTO CITIES (
    CITIES.CITY_NAME, CITIES.STATE_NAME, CITIES.COUNTRY_NAME
)
SELECT 
    project1.PUBLIC_USER_INFORMATION.HOMETOWN_CITY, 
    project1.PUBLIC_USER_INFORMATION.HOMETOWN_STATE, 
    project1.PUBLIC_USER_INFORMATION.HOMETOWN_COUNTRY 
FROM project1.PUBLIC_USER_INFORMATION;

DELETE
FROM
  CITIES
WHERE
  CITY_ID NOT IN
  (
    SELECT
      MIN(CITY_ID)
    FROM
      CITIES
    GROUP BY
      CITY_NAME,
      STATE_NAME,
      COUNTRY_NAME
  );

-- INSERT INTO USER_CURRENT_CITIES

INSERT INTO USER_CURRENT_CITIES (
    USER_CURRENT_CITIES.USER_ID, USER_CURRENT_CITIES.CURRENT_CITY_ID
)
SELECT 
    project1.PUBLIC_USER_INFORMATION.USER_ID, 
    CITIES.CITY_ID 
FROM project1.PUBLIC_USER_INFORMATION
JOIN CITIES ON project1.PUBLIC_USER_INFORMATION.CURRENT_CITY = CITIES.CITY_NAME;

-- INSERT INTO USER_HOMETOWN_CITIES

INSERT INTO USER_HOMETOWN_CITIES (
    USER_HOMETOWN_CITIES.USER_ID, USER_HOMETOWN_CITIES.HOMETOWN_CITY_ID
)
SELECT 
    project1.PUBLIC_USER_INFORMATION.USER_ID, 
    CITIES.CITY_ID 
FROM project1.PUBLIC_USER_INFORMATION
JOIN CITIES ON project1.PUBLIC_USER_INFORMATION.HOMETOWN_CITY = CITIES.CITY_NAME;

-- INSERT INTO PROGRAMS

CREATE VIEW instutition_view AS
SELECT
    INSTITUTION_NAME,
    PROGRAM_CONCENTRATION,
    PROGRAM_DEGREE
FROM 
    project1.PUBLIC_USER_INFORMATION
WHERE INSTITUTION_NAME IS NOT NULL
GROUP BY       
    INSTITUTION_NAME,
    PROGRAM_CONCENTRATION,
    PROGRAM_DEGREE;

INSERT INTO PROGRAMS (
    INSTITUTION, CONCENTRATION, DEGREE
)
SELECT
    INSTITUTION_NAME, 
    PROGRAM_CONCENTRATION, 
    PROGRAM_DEGREE 
FROM instutition_view;

-- SELECT DISTINCT(INSTITUTION_NAME) FROM project1.PUBLIC_USER_INFORMATION;
-- SELECT DISTINCT(PROGRAM_CONCENTRATION) FROM project1.PUBLIC_USER_INFORMATION;
-- SELECT DISTINCT(PROGRAM_DEGREE) FROM project1.PUBLIC_USER_INFORMATION;


-- INSERT INTO EDUCATION

INSERT INTO EDUCATION 
    (USER_ID, PROGRAM_ID, PROGRAM_YEAR) 
SELECT 
    project1.PUBLIC_USER_INFORMATION.USER_ID, 
    PROGRAMS.PROGRAM_ID,
    project1.PUBLIC_USER_INFORMATION.PROGRAM_YEAR
FROM project1.PUBLIC_USER_INFORMATION
JOIN PROGRAMS ON 
    project1.PUBLIC_USER_INFORMATION.INSTITUTION_NAME = PROGRAMS.INSTITUTION AND
    project1.PUBLIC_USER_INFORMATION.PROGRAM_CONCENTRATION = PROGRAMS.CONCENTRATION AND
    project1.PUBLIC_USER_INFORMATION.PROGRAM_DEGREE = PROGRAMS.DEGREE;

-- INSERT INTO PHOTOS

INSERT INTO PHOTOS 
    (
        PHOTO_ID,
        ALBUM_ID,
        PHOTO_CAPTION,
        PHOTO_CREATED_TIME,
        PHOTO_MODIFIED_TIME,
        PHOTO_LINK
    )
SELECT 
    PHOTO_ID, 
    ALBUM_ID, 
    PHOTO_CAPTION,
    PHOTO_CREATED_TIME,
    PHOTO_MODIFIED_TIME,
    PHOTO_LINK
FROM project1.PUBLIC_PHOTO_INFORMATION;

-- INSERT INTO ALBUMS

INSERT INTO ALBUMS
    (
        ALBUM_ID,
        ALBUM_OWNER_ID,
        ALBUM_NAME,
        ALBUM_CREATED_TIME,
        ALBUM_MODIFIED_TIME,
        ALBUM_LINK,
        ALBUM_VISIBILITY,
        COVER_PHOTO_ID
    )
SELECT 
    ALBUM_ID, 
    OWNER_ID, 
    ALBUM_NAME,
    ALBUM_CREATED_TIME,
    ALBUM_MODIFIED_TIME,
    ALBUM_LINK,
    ALBUM_VISIBILITY,
    COVER_PHOTO_ID
FROM project1.PUBLIC_PHOTO_INFORMATION 
GROUP BY 
    ALBUM_ID,OWNER_ID, 
    ALBUM_NAME,
    ALBUM_CREATED_TIME,
    ALBUM_MODIFIED_TIME,
    ALBUM_LINK,
    ALBUM_VISIBILITY,
    COVER_PHOTO_ID;

-- INSERT INTO TAGS

INSERT INTO TAGS
    (
        TAG_PHOTO_ID,
        TAG_SUBJECT_ID,
        TAG_CREATED_TIME,
        TAG_X,
        TAG_Y
    )
SELECT 
    PHOTO_ID,
    TAG_SUBJECT_ID,
    TAG_CREATED_TIME,
    TAG_X_COORDINATE,
    TAG_Y_COORDINATE
FROM project1.PUBLIC_TAG_INFORMATION;

-- INSERT INTO EVENTS

INSERT INTO USER_EVENTS 
    (
        EVENT_ID,
        EVENT_CREATOR_ID,
        EVENT_NAME,
        EVENT_TAGLINE,
        EVENT_DESCRIPTION,
        EVENT_HOST,
        EVENT_TYPE,
        EVENT_SUBTYPE,
        EVENT_ADDRESS,
        EVENT_CITY_ID,
        EVENT_START_TIME,
        EVENT_END_TIME
    )
SELECT 
    EVENT_ID,
    EVENT_CREATOR_ID,
    EVENT_NAME,
    EVENT_TAGLINE,
    EVENT_DESCRIPTION,
    EVENT_HOST,
    EVENT_TYPE,
    EVENT_SUBTYPE,
    EVENT_ADDRESS,
    CITIES.CITY_ID,
    EVENT_START_TIME,
    EVENT_END_TIME
FROM project1.PUBLIC_EVENT_INFORMATION
JOIN CITIES ON project1.PUBLIC_EVENT_INFORMATION.EVENT_CITY = CITIES.CITY_NAME;