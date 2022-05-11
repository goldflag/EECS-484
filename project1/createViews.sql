CREATE VIEW VIEW_USER_INFORMATION(USER_ID, FIRST_NAME, LAST_NAME, YEAR_OF_BIRTH, MONTH_OF_BIRTH, DAY_OF_BIRTH, GENDER, CURRENT_CITY, CURRENT_STATE, CURRENT_COUNTRY, HOMETOWN_CITY, HOMETOWN_STATE, HOMETOWN_COUNTRY, INSTITUTION_NAME, PROGRAM_YEAR, PROGRAM_CONCENTRATION, PROGRAM_DEGREE)
        AS SELECT  U.USER_ID, U.FIRST_NAME, U.LAST_NAME, U.YEAR_OF_BIRTH, U.MONTH_OF_BIRTH, U.DAY_OF_BIRTH, U.GENDER, C.CURRENT_CITY, C.CURRENT_STATE, C.CURRENT_COUNTRY, C2.HOMETOWN_CITY, C2.HOMETOWN_STATE, C2.HOMETOWN_COUNTRY, P.INSTITUTION, E.PROGRAM_YEAR, P.CONCENTRATION, P.DEGREE
        FROM USERS U
        LEFT JOIN USER_CURRENT_CITIIES UCC ON U.USER_ID = UCC.USER_ID
        LEFT JOIN CITIES C ON UCC.CURRENT_CITY_ID = C.CITY_ID
        LEFT JOIN USER_HOMETOWN_CITIES UHC ON U.USER_ID = UHC.USER_ID
        LEFT JOIN CITIES C2 ON UHC.HOMETOWN_CITY_ID = C2.CITY_ID
        LEFT JOIN EDUCATION E ON U.USER_ID = E.USER_ID
        LEFT JOIN PROGRAMS P ON E.PROGRAM_ID = P.PROGRAM_ID;
        
        


CREATE VIEW VIEW_ARE_FRIENDS(USER1_ID, USER2_ID)
        AS SELECT * FROM FRIENDS;


CREATE VIEW VIEW_PHOTO_INFORMATION(ALBUM_ID, OWNER_ID, COVER_PHOTO_ID, ALBUM_NAME, ALBUM_CREATED_TIME, ALBUM_MODIFIED_TIME, ALBUM_LINK, ALBUM_VISIBILITY, PHOTO_ID, PHOTO_CAPTION, PHOTO_CREATED_TIME, PHOTO_MODIFIED_TIME, PHOTO_LINK)
        AS SELECT P.ALBUM_ID, A.ALBUM_OWNER_ID, A.COVER_PHOTO_ID, A.ALBUM_NAME, A.ALBUM_CREATED_TIME, A.ALBUM_MODIFIED_TIME, A.ALBUM_LINK, A.ALBUM_VISIBILITY, P.PHOTO_ID, P.PHOTO_CAPTION, P.PHOTO_CREATED_TIME, P.PHOTO_MODIFIED_TIME, P.PHOTO_LINK
        FROM PHOTOS P
        RIGHT JOIN ALBUMS A ON P.ALBUM_ID = A.ALBUM_ID;





CREATE VIEW VIEW_EVENT_INFORMATION(EVENT_ID, EVENT_CREATOR_ID, EVENT_NAME, EVENT_TAGLINE, EVENT_DESCRIPTION, EVENT_HOST, EVENT_TYPE, EVENT_SUBTYPE, EVENT_ADDRESS, EVENT_CITY, EVENT_STATE, EVENT_COUNTRY, EVENT_START_TIME, EVENT_END_TIME)
        AS SELECT UE.EVENT_ID, UE.EVENT_CREATOR_ID, UE.EVENT_NAME, UE.EVENT_TAGLINE, UE.EVENT_DESCRIPTION, UE.EVENT_HOST, UE.EVENT_TYPE, UE.EVENT_SUBTYPE, UE.EVENT_ADDRESS, C.CITY_NAME, C.STATE_NAME, C.COUNTRY_NAME, UE.EVENT_START_TIME, UE.EVENT_END_TIME
        FROM USER_EVENTS UE 
        INNER JOIN CITIES C ON UE.EVENT_CITY_ID = C.CITY_ID;



CREATE VIEW VIEW_TAG_INFORMATION(TAG_PHOTO_ID, TAG_SUBJECT_ID, TAG_CREATED_TIME, TAG_X, TAG_Y)
        AS SELECT T.TAG_PHOTO_ID, T.TAG_SUBJECT_ID, T.TAG_CREATED_TIME, T.TAG_X, T.TAG_Y
        FROM TAGS T;
