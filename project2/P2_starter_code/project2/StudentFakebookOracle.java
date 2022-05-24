package project2;
import java.util.*;


import java.util.Collections;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

/*
    The StudentFakebookOracle class is derived from the FakebookOracle class and implements
    the abstract query functions that investigate the database provided via the <connection>
    parameter of the constructor to discover specific information.
*/
public final class StudentFakebookOracle extends FakebookOracle {
    // [Constructor]
    // REQUIRES: <connection> is a valid JDBC connection
    public StudentFakebookOracle(Connection connection) {
        oracle = connection;
    }
    
    @Override
    // Query 0
    // -----------------------------------------------------------------------------------
    // GOALS: (A) Find the total number of users for which a birth month is listed
    //        (B) Find the birth month in which the most users were born
    //        (C) Find the birth month in which the fewest users (at least one) were born
    //        (D) Find the IDs, first names, and last names of users born in the month
    //            identified in (B)
    //        (E) Find the IDs, first names, and last name of users born in the month
    //            identified in (C)
    //
    // This query is provided to you completed for reference. Below you will find the appropriate
    // mechanisms for opening up a statement, executing a query, walking through results, extracting
    // data, and more things that you will need to do for the remaining nine queries
    public BirthMonthInfo findMonthOfBirthInfo() throws SQLException {
        try (Statement stmt = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly)) {
            // Step 1
            // ------------
            // * Find the total number of users with birth month info
            // * Find the month in which the most users were born
            // * Find the month in which the fewest (but at least 1) users were born
            ResultSet rst = stmt.executeQuery(
                "SELECT COUNT(*) AS Birthed, Month_of_Birth " +         // select birth months and number of uses with that birth month
                "FROM " + UsersTable + " " +                            // from all users
                "WHERE Month_of_Birth IS NOT NULL " +                   // for which a birth month is available
                "GROUP BY Month_of_Birth " +                            // group into buckets by birth month
                "ORDER BY Birthed DESC, Month_of_Birth ASC");           // sort by users born in that month, descending; break ties by birth month
            
            int mostMonth = 0;
            int leastMonth = 0;
            int total = 0;
            while (rst.next()) {                       // step through result rows/records one by one
                if (rst.isFirst()) {                   // if first record
                    mostMonth = rst.getInt(2);         //   it is the month with the most
                }
                if (rst.isLast()) {                    // if last record
                    leastMonth = rst.getInt(2);        //   it is the month with the least
                }
                total += rst.getInt(1);                // get the first field's value as an integer
            }
            BirthMonthInfo info = new BirthMonthInfo(total, mostMonth, leastMonth);
            
            // Step 2
            // ------------
            // * Get the names of users born in the most popular birth month
            rst = stmt.executeQuery(
                "SELECT User_ID, First_Name, Last_Name " +                // select ID, first name, and last name
                "FROM " + UsersTable + " " +                              // from all users
                "WHERE Month_of_Birth = " + mostMonth + " " +             // born in the most popular birth month
                "ORDER BY User_ID");                                      // sort smaller IDs first
                
            while (rst.next()) {
                info.addMostPopularBirthMonthUser(new UserInfo(rst.getLong(1), rst.getString(2), rst.getString(3)));
            }

            // Step 3
            // ------------
            // * Get the names of users born in the least popular birth month
            rst = stmt.executeQuery(
                "SELECT User_ID, First_Name, Last_Name " +                // select ID, first name, and last name
                "FROM " + UsersTable + " " +                              // from all users
                "WHERE Month_of_Birth = " + leastMonth + " " +            // born in the least popular birth month
                "ORDER BY User_ID");                                      // sort smaller IDs first
                
            while (rst.next()) {
                info.addLeastPopularBirthMonthUser(new UserInfo(rst.getLong(1), rst.getString(2), rst.getString(3)));
            }


            // Step 4
            // ------------
            // * Close resources being used
            rst.close();
            stmt.close();                            // if you close the statement first, the result set gets closed automatically

            return info;

        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return new BirthMonthInfo(-1, -1, -1);
        }
    }
    
    @Override
    // Query 1
    // -----------------------------------------------------------------------------------
    // GOALS: (A) The first name(s) with the most letters
    //        (B) The first name(s) with the fewest letters
    //        (C) The first name held by the most users
    //        (D) The number of users whose first name is that identified in (C)
    public FirstNameInfo findNameInfo() throws SQLException {
        try (Statement stmt = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly)) {
            /*
                EXAMPLE DATA STRUCTURE USAGE
                ============================================
                FirstNameInfo info = new FirstNameInfo();
                info.addLongName("Aristophanes");
                info.addLongName("Michelangelo");
                info.addLongName("Peisistratos");
                info.addShortName("Bob");
                info.addShortName("Sue");
                info.addCommonName("Harold");
                info.addCommonName("Jessica");
                info.setCommonNameCount(42);
                return info;
            */

            FirstNameInfo info = new FirstNameInfo();

            ResultSet rst = stmt.executeQuery(
                "SELECT DISTINCT(FIRST_NAME), MONKE FROM ( " +     
                "SELECT FIRST_NAME, length(FIRST_NAME) AS MONKE FROM " + UsersTable + " " +   
                ") ORDER BY MONKE DESC");


            int count1 = 0;
            ArrayList<String> count1Arr = new ArrayList<String>();
            while (rst.next()) {
                if (count1 != 0) {
                    if (rst.getInt(2) != count1) {
                        break;
                    }
                }
                count1 = rst.getInt(2);
                count1Arr.add(rst.getString(1));
            }
            Collections.sort(count1Arr);

            for (String i : count1Arr) {
                info.addLongName(i);
            }

            rst = stmt.executeQuery(
                "SELECT DISTINCT(FIRST_NAME), MONKE FROM ( " +     
                "SELECT FIRST_NAME, length(FIRST_NAME) AS MONKE FROM " + UsersTable + " " +   
                ") ORDER BY MONKE ASC ");

            int count2 = 0;
            ArrayList<String> count2Arr = new ArrayList<String>();
            while (rst.next()) {
                if (count2 != 0) {
                    if (rst.getInt(2) != count2) {
                        break;
                    }
                }
                count2 = rst.getInt(2);
                count2Arr.add(rst.getString(1));

            }
            Collections.sort(count2Arr);

            for (String i : count2Arr) {
                info.addShortName(i);
            }

            rst = stmt.executeQuery(
                "SELECT * FROM ( " +     
                "SELECT COUNT(FIRST_NAME) AS CT, FIRST_NAME FROM " + UsersTable + " GROUP BY FIRST_NAME " +   
                ") ORDER BY CT DESC");


            int count3 = 0;
            ArrayList<String> count3Arr = new ArrayList<String>();

            while (rst.next()) {
                if (count3 != 0) {
                    if (rst.getInt(1) != count3) {
                        break;
                    }
                }
                count3 = rst.getInt(1);
                count3Arr.add(rst.getString(2));

                info.setCommonNameCount(rst.getInt(1));
            }

            Collections.sort(count3Arr);
            for (String i : count3Arr) {
                info.addCommonName(i);
            }

            return info;
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return new FirstNameInfo();
        }
    }
    
    @Override
    // Query 2
    // -----------------------------------------------------------------------------------
    // GOALS: (A) Find the IDs, first names, and last names of users without any friends
    //
    // Be careful! Remember that if two users are friends, the Friends table only contains
    // the one entry (U1, U2) where U1 < U2.
    public FakebookArrayList<UserInfo> lonelyUsers() throws SQLException {
        FakebookArrayList<UserInfo> results = new FakebookArrayList<UserInfo>(", ");
        
        try (Statement stmt = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly)) {
            /*
                EXAMPLE DATA STRUCTURE USAGE
                ============================================
                UserInfo u1 = new UserInfo(15, "Abraham", "Lincoln");
                UserInfo u2 = new UserInfo(39, "Margaret", "Thatcher");
                results.add(u1);
                results.add(u2);

                SELECT USER_ID, FIRST_NAME, LAST_NAME FROM project2.PUBLIC_USERS users 
                LEFT JOIN project2.PUBLIC_FRIENDS friends 
                ON users.USER_ID = friends.USER1_ID OR users.USER_ID = friends.USER2_ID 
                WHERE friends.USER2_ID IS NULL AND friends.USER1_ID IS NULL;
            */

            ResultSet rst = stmt.executeQuery(
                "SELECT USER_ID, FIRST_NAME, LAST_NAME FROM " + UsersTable + " users " +
                "LEFT JOIN " + FriendsTable + " friends " + 
                "ON users.USER_ID = friends.USER1_ID OR users.USER_ID = friends.USER2_ID " + 
                "WHERE friends.USER2_ID IS NULL AND friends.USER1_ID IS NULL");  

            while (rst.next()) {
                results.add(new UserInfo(rst.getLong(1), rst.getString(2), rst.getString(3)));
            }

        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return results;
    }
    
    @Override
    // Query 3
    // -----------------------------------------------------------------------------------
    // GOALS: (A) Find the IDs, first names, and last names of users who no longer live
    //            in their hometown (i.e. their current city and their hometown are different)
    public FakebookArrayList<UserInfo> liveAwayFromHome() throws SQLException {
        FakebookArrayList<UserInfo> results = new FakebookArrayList<UserInfo>(", ");
        
        try (Statement stmt = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly)) {
            /*
                EXAMPLE DATA STRUCTURE USAGE
                ============================================
                UserInfo u1 = new UserInfo(9, "Meryl", "Streep");
                UserInfo u2 = new UserInfo(104, "Tom", "Hanks");
                results.add(u1);
                results.add(u2);
CurrentCitiesTable HometownCitiesTable

                SELECT users.USER_ID, users.FIRST_NAME, users.LAST_NAME FROM project2.PUBLIC_USERS users 
                INNER JOIN project2.PUBLIC_USER_CURRENT_CITY current_city 
                ON users.USER_ID = current_city.USER_ID 
                INNER JOIN project2.PUBLIC_USER_HOMETOWN_CITY hometown_city 
                ON users.USER_ID = hometown_city.USER_ID 
                WHERE hometown_city.HOMETOWN_CITY_ID != current_city.CURRENT_CITY_ID
                ORDER BY users.USER_ID asc;
            */

            ResultSet rst = stmt.executeQuery(
                "SELECT users.USER_ID, users.FIRST_NAME, users.LAST_NAME FROM " + UsersTable + " users " +
                "INNER JOIN " + CurrentCitiesTable + " current_city " +
                "ON users.USER_ID = current_city.USER_ID " +
                "INNER JOIN " + HometownCitiesTable + " hometown_city " +
                "ON users.USER_ID = hometown_city.USER_ID " + 
                "WHERE hometown_city.HOMETOWN_CITY_ID != current_city.CURRENT_CITY_ID " + 
                "ORDER BY users.USER_ID asc");  

            while (rst.next()) {
                results.add(new UserInfo(rst.getLong(1), rst.getString(2), rst.getString(3)));
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return results;
    }
    
    @Override
    // Query 4
    // -----------------------------------------------------------------------------------
    // GOALS: (A) Find the IDs, links, and IDs and names of the containing album of the top
    //            <num> photos with the most tagged users
    //        (B) For each photo identified in (A), find the IDs, first names, and last names
    //            of the users therein tagged
    public FakebookArrayList<TaggedPhotoInfo> findPhotosWithMostTags(int num) throws SQLException {
        FakebookArrayList<TaggedPhotoInfo> results = new FakebookArrayList<TaggedPhotoInfo>("\n");
        
        try (Statement stmt = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly)) {
            /*
                EXAMPLE DATA STRUCTURE USAGE
                ============================================
                PhotoInfo p = new PhotoInfo(80, 5, "www.photolink.net", "Winterfell S1");
                UserInfo u1 = new UserInfo(3901, "Jon", "Snow");
                UserInfo u2 = new UserInfo(3902, "Arya", "Stark");
                UserInfo u3 = new UserInfo(3903, "Sansa", "Stark");
                TaggedPhotoInfo tp = new TaggedPhotoInfo(p);
                tp.addTaggedUser(u1);
                tp.addTaggedUser(u2);
                tp.addTaggedUser(u3);
                results.add(tp);


                WITH first AS (
                    SELECT * FROM (
                        SELECT * FROM (
                            SELECT COUNT(TAG_PHOTO_ID) as ct, TAG_PHOTO_ID FROM project2.PUBLIC_TAGS GROUP BY TAG_PHOTO_ID
                        ) ORDER BY ct DESC
                    ) monke WHERE ROWNUM <= 10
                ),
                second AS (
                    SELECT photos.PHOTO_ID, photos.ALBUM_ID, photos.PHOTO_LINK FROM first
                    INNER JOIN project2.PUBLIC_PHOTOS photos
                    ON first.TAG_PHOTO_ID = photos.PHOTO_ID
                )
                SELECT
                    second.PHOTO_ID, second.ALBUM_ID, second.PHOTO_LINK, albums.ALBUM_NAME
                FROM second
                INNER JOIN project2.PUBLIC_ALBUMS albums
                ON albums.ALBUM_ID = second.ALBUM_ID;

                SELECT users.USER_ID, users.FIRST_NAME, users.LAST_NAME FROM project2.PUBLIC_TAGS tags 
                INNER JOIN project2.PUBLIC_USERS users 
                ON users.USER_ID = tags.TAG_SUBJECT_ID 
                WHERE tags.TAG_PHOTO_ID = '648'
                ORDER by users.USER_ID ASC;
            */

            String query = "WITH first AS ( " +
                "    SELECT * FROM ( " +
                "        SELECT * FROM ( " +
                "            SELECT COUNT(TAG_PHOTO_ID) as ct, TAG_PHOTO_ID FROM " + TagsTable + " GROUP BY TAG_PHOTO_ID " +
                "        ) ORDER BY ct DESC " +
                "    ) monke WHERE ROWNUM <= " + num + " " +
                "), " +
                "second AS ( " +
                "    SELECT photos.PHOTO_ID, photos.ALBUM_ID, photos.PHOTO_LINK FROM first " +
                "    INNER JOIN " + PhotosTable + " photos " +
                "    ON first.TAG_PHOTO_ID = photos.PHOTO_ID " +
                ") " +
                "SELECT " +
                "    second.PHOTO_ID, second.ALBUM_ID, second.PHOTO_LINK, albums.ALBUM_NAME " +
                "FROM second " +
                "INNER JOIN " + AlbumsTable + " albums " +
                "ON albums.ALBUM_ID = second.ALBUM_ID";
            System.out.println(num);
            System.out.println(query);

            ResultSet rst = stmt.executeQuery(query);  


            while (rst.next()) {
                // System.out.println(rst.getInt(1), rst.getInt(2), rst.getString(3), rst.getString(4));
                PhotoInfo p = new PhotoInfo(rst.getInt(1), rst.getInt(2), rst.getString(3), rst.getString(4));
                Statement ree = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly);
                ResultSet monke = ree.executeQuery(
                    "SELECT users.USER_ID, users.FIRST_NAME, users.LAST_NAME FROM " + TagsTable + " tags " +
                    "INNER JOIN " + UsersTable + " users " +
                    "ON users.USER_ID = tags.TAG_SUBJECT_ID  " +
                    "WHERE tags.TAG_PHOTO_ID = '"+ rst.getInt(1) + "' " +
                    "ORDER by users.USER_ID ASC"
                );

                TaggedPhotoInfo tp = new TaggedPhotoInfo(p);

                while (monke.next()) {
                    // System.out.println(monke.getLong(1), monke.getString(2), monke.getString(3));
                    tp.addTaggedUser(new UserInfo(monke.getLong(1), monke.getString(2), monke.getString(3)));
                }   

                results.add(tp);
            }

        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return results;
    }
    
    @Override
    // Query 5
    // -----------------------------------------------------------------------------------
    // GOALS: (A) Find the IDs, first names, last names, and birth years of each of the two
    //            users in the top <num> pairs of users that meet each of the following
    //            criteria:
    //              (i) same gender
    //              (ii) tagged in at least one common photo
    //              (iii) difference in birth years is no more than <yearDiff>
    //              (iv) not friends
    //        (B) For each pair identified in (A), find the IDs, links, and IDs and names of
    //            the containing album of each photo in which they are tagged together
    public FakebookArrayList<MatchPair> matchMaker(int num, int yearDiff) throws SQLException {
        FakebookArrayList<MatchPair> results = new FakebookArrayList<MatchPair>("\n");

        System.out.println(num);
        System.out.println(yearDiff);
        
        try (Statement stmt = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly)) {
            /*
                EXAMPLE DATA STRUCTURE USAGE
                ============================================
                UserInfo u1 = new UserInfo(93103, "Romeo", "Montague");
                UserInfo u2 = new UserInfo(93113, "Juliet", "Capulet");
                MatchPair mp = new MatchPair(u1, 1597, u2, 1597);
                PhotoInfo p = new PhotoInfo(167, 309, "www.photolink.net", "Tragedy");
                mp.addSharedPhoto(p);
                results.add(mp);

                INNER JOIN project2.PUBLIC_TAGS tags1
                ON tags1.TAG_SUBJECT_ID = user1.USER_ID
                INNER JOIN project2.PUBLIC_TAGS tags2
                ON tags2.TAG_SUBJECT_ID = user2.USER_ID
                INNER JOIN project2.PUBLIC_TAGS tags3
                ON tags3.TAG_PHOTO_ID = tags1.TAG_PHOTO_ID AND tags3.TAG_PHOTO_ID = tags2.TAG_PHOTO_ID 

                    tags1.TAG_PHOTO_ID,
                    tags2.TAG_PHOTO_ID,
                    tags3.TAG_PHOTO_ID

                WITH monke as (
                    SELECT 
                        user1.USER_ID AS USER_ID1, 
                        user1.FIRST_NAME AS USER1_FIRST_NAME, 
                        user1.LAST_NAME AS USER1_LAST_NAME, 
                        user1.YEAR_OF_BIRTH AS USER1_BIRTH, 
                        user2.USER_ID AS USER_ID2,
                        user2.FIRST_NAME AS USER2_FIRST_NAME,
                        user2.LAST_NAME AS USER2_LAST_NAME,
                        user2.YEAR_OF_BIRTH AS USER2_BIRTH,
                        tags1.TAG_PHOTO_ID 
                    FROM project2.PUBLIC_USERS user1
                    INNER JOIN project2.PUBLIC_USERS user2
                    ON user1.gender = user2.gender AND user1.USER_ID < user2.USER_ID AND ABS(user1.YEAR_OF_BIRTH - user2.YEAR_OF_BIRTH) <= 1000
                    INNER JOIN project2.PUBLIC_TAGS tags1
                    ON tags1.TAG_SUBJECT_ID = user1.USER_ID
                    INNER JOIN project2.PUBLIC_TAGS tags2
                    ON tags2.TAG_SUBJECT_ID = user2.USER_ID
                    INNER JOIN project2.PUBLIC_TAGS tags3
                    ON tags3.TAG_PHOTO_ID = tags1.TAG_PHOTO_ID AND tags3.TAG_PHOTO_ID = tags2.TAG_PHOTO_ID 
                    FULL OUTER JOIN project2.PUBLIC_FRIENDS friends
                    ON user1.USER_ID = friends.USER1_ID AND user2.USER_ID = friends.USER2_ID 
                    WHERE friends.USER1_ID IS NULL AND friends.USER2_ID IS NULL
                    GROUP BY user1.USER_ID, user2.USER_ID, user1.YEAR_OF_BIRTH, user2.YEAR_OF_BIRTH, user1.FIRST_NAME, user1.LAST_NAME, user2.FIRST_NAME, user2.LAST_NAME, tags1.TAG_PHOTO_ID
                    ORDER BY user1.USER_ID, user2.USER_ID ASC
                )
                SELECT COUNT(monke.TAG_PHOTO_ID) 
                FROM MONKE WHERE ROWNUM <= 1000 
                GROUP BY monke.USER_ID1, monke.USER1_FIRST_NAME, monke.USER1_LAST_NAME, monke.USER1_BIRTH, monke.USER_ID2, monke.USER2_FIRST_NAME, monke.USER2_LAST_NAME, monke.USER2_BIRTH;

                SELECT photos.PHOTO_ID, photos.ALBUM_ID, photos.PHOTO_LINK

                WITH query as (
                    SELECT monke.TAG_PHOTO_ID FROM (
                        SELECT TAG_PHOTO_ID FROM project2.PUBLIC_TAGS WHERE TAG_SUBJECT_ID = 182
                    ) monke
                    INNER JOIN project2.PUBLIC_TAGS tags
                    ON tags.TAG_SUBJECT_ID = 561 AND monke.TAG_PHOTO_ID = tags.TAG_PHOTO_ID           
                ),
                query2 as (
                    SELECT photos.PHOTO_ID, photos.ALBUM_ID, photos.PHOTO_LINK FROM project2.PUBLIC_PHOTOS photos
                    JOIN query ON query.TAG_PHOTO_ID = photos.PHOTO_ID
                )
                SELECT query2.PHOTO_ID, query2.ALBUM_ID, query2.PHOTO_LINK, albums.ALBUM_NAME FROM query2
                JOIN project2.PUBLIC_ALBUMS albums ON albums.ALBUM_ID = query2.ALBUM_ID;

            */

            ResultSet monke = stmt.executeQuery(
                "SELECT * FROM (" +
                    "SELECT " +
                        "user1.USER_ID AS USER_ID1,  " +
                        "user1.FIRST_NAME AS USER1_FIRST_NAME,  " +
                        "user1.LAST_NAME AS USER1_LAST_NAME, " +
                        "user1.YEAR_OF_BIRTH AS USER1_BIRTH,  " +
                        "user2.USER_ID AS USER_ID2, " +
                        "user2.FIRST_NAME AS USER2_FIRST_NAME, " +
                        "user2.LAST_NAME AS USER2_LAST_NAME, " +
                        "user2.YEAR_OF_BIRTH AS USER2_BIRTH " +
                    "FROM " + UsersTable + " user1 " +
                    "INNER JOIN " + UsersTable + " user2 " +
                    "ON user1.gender = user2.gender AND user1.USER_ID < user2.USER_ID AND ABS(user1.YEAR_OF_BIRTH - user2.YEAR_OF_BIRTH) <= " + yearDiff + " " +
                    "INNER JOIN " + TagsTable + " tags1 " +
                    "ON tags1.TAG_SUBJECT_ID = user1.USER_ID " +
                    "INNER JOIN " + TagsTable + " tags2 " +
                    "ON tags2.TAG_SUBJECT_ID = user2.USER_ID " +
                    "INNER JOIN " + TagsTable + " tags3 " +
                    "ON tags3.TAG_PHOTO_ID = tags1.TAG_PHOTO_ID AND tags3.TAG_PHOTO_ID = tags2.TAG_PHOTO_ID  " +
                    "FULL OUTER JOIN " + FriendsTable + " friends " +
                    "ON user1.USER_ID = friends.USER1_ID AND user2.USER_ID = friends.USER2_ID  " +
                    "WHERE friends.USER1_ID IS NULL AND friends.USER2_ID IS NULL " +
                    "GROUP BY user1.USER_ID, user2.USER_ID, user1.YEAR_OF_BIRTH, user2.YEAR_OF_BIRTH, user1.FIRST_NAME, user1.LAST_NAME, user2.FIRST_NAME, user2.LAST_NAME, tags1.TAG_PHOTO_ID " +
                    "ORDER BY user1.USER_ID, user2.USER_ID ASC " +
                ") WHERE ROWNUM <= " + num
            );

            while (monke.next()) {
                UserInfo u1 = new UserInfo(monke.getLong(1), monke.getString(2), monke.getString(3));
                UserInfo u2 = new UserInfo(monke.getLong(5), monke.getString(6), monke.getString(7));

                MatchPair mp = new MatchPair(u1, monke.getLong(4), u2, monke.getLong(8));
                Statement ree = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly);

                ResultSet monke2 = ree.executeQuery(
                    "WITH query as ( " +
                    "    SELECT monke.TAG_PHOTO_ID FROM ( " +
                    "        SELECT TAG_PHOTO_ID FROM " + TagsTable + " WHERE TAG_SUBJECT_ID = " + monke.getLong(1) + " " +
                    "    ) monke " +
                    "    INNER JOIN " + TagsTable + " tags " +
                    "    ON tags.TAG_SUBJECT_ID = " + monke.getLong(5) + " AND monke.TAG_PHOTO_ID = tags.TAG_PHOTO_ID " +
                    "), " +
                    "query2 as ( " +
                    "    SELECT photos.PHOTO_ID, photos.ALBUM_ID, photos.PHOTO_LINK FROM " + PhotosTable + " photos " +
                    "    JOIN query ON query.TAG_PHOTO_ID = photos.PHOTO_ID " +
                    ") " +
                    "SELECT query2.PHOTO_ID, query2.ALBUM_ID, query2.PHOTO_LINK, albums.ALBUM_NAME FROM query2 " +
                    "JOIN " + AlbumsTable + " albums ON albums.ALBUM_ID = query2.ALBUM_ID"
                );

                while (monke2.next()) {
                    PhotoInfo p = new PhotoInfo(monke2.getLong(1), monke2.getLong(2), monke2.getString(3), monke2.getString(4));
                    System.out.println(p);
                    mp.addSharedPhoto(p);
                }
                results.add(mp);
            }   

        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return results;
    }
    
    @Override
    // Query 6
    // -----------------------------------------------------------------------------------
    // GOALS: (A) Find the IDs, first names, and last names of each of the two users in
    //            the top <num> pairs of users who are not friends but have a lot of
    //            common friends
    //        (B) For each pair identified in (A), find the IDs, first names, and last names
    //            of all the two users' common friends
    public FakebookArrayList<UsersPair> suggestFriends(int num) throws SQLException {
        FakebookArrayList<UsersPair> results = new FakebookArrayList<UsersPair>("\n");
        
        try (Statement stmt = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly)) {
            /*
                EXAMPLE DATA STRUCTURE USAGE
                ============================================
                UserInfo u1 = new UserInfo(16, "The", "Hacker");
                UserInfo u2 = new UserInfo(80, "Dr.", "Marbles");
                UserInfo u3 = new UserInfo(192, "Digit", "Le Boid");
                UsersPair up = new UsersPair(u1, u2);
                up.addSharedFriend(u3);
                results.add(up);
            */
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return results;
    }
    
    @Override
    // Query 7
    // -----------------------------------------------------------------------------------
    // GOALS: (A) Find the name of the state or states in which the most events are held
    //        (B) Find the number of events held in the states identified in (A)
    public EventStateInfo findEventStates() throws SQLException {
        try (Statement stmt = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly)) {
            /*
                EXAMPLE DATA STRUCTURE USAGE
                ============================================
                EventStateInfo info = new EventStateInfo(50);
                info.addState("Kentucky");
                info.addState("Hawaii");
                info.addState("New Hampshire");
                return info;



                SELECT * FROM (
                    SELECT COUNT(cities.STATE_NAME) AS COUNT, cities.STATE_NAME FROM project2.PUBLIC_USER_EVENTS events
                    JOIN project2.PUBLIC_CITIES cities 
                    ON cities.CITY_ID = events.EVENT_CITY_ID
                    GROUP BY cities.STATE_NAME
                ) ORDER BY count desc;
            */


            ResultSet monke = stmt.executeQuery(
                "SELECT * FROM ( " +
                "    SELECT COUNT(cities.STATE_NAME) AS COUNT, cities.STATE_NAME FROM " + EventsTable + " events " +
                "    JOIN " + CitiesTable + " cities  " +
                "    ON cities.CITY_ID = events.EVENT_CITY_ID " +
                "    GROUP BY cities.STATE_NAME " +
                ") ORDER BY count desc "
            );

            long eventCount = 0;

            ArrayList<String> states = new ArrayList<String>();

            while (monke.next()) {
                if (eventCount != 0 && monke.getLong(1) < eventCount) {
                    break;
                }
                eventCount = monke.getLong(1);
                states.add(monke.getString(2));
            }

            EventStateInfo info = new EventStateInfo(eventCount);
            for (String state : states) {
                info.addState(state);
            }

            return info;
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return new EventStateInfo(-1);
        }
    }
    
    @Override
    // Query 8
    // -----------------------------------------------------------------------------------
    // GOALS: (A) Find the ID, first name, and last name of the oldest friend of the user
    //            with User ID <userID>
    //        (B) Find the ID, first name, and last name of the youngest friend of the user
    //            with User ID <userID>
    public AgeInfo findAgeInfo(long userID) throws SQLException {
        try (Statement stmt = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly)) {
            /*
                EXAMPLE DATA STRUCTURE USAGE
                ============================================
                UserInfo old = new UserInfo(12000000, "Galileo", "Galilei");
                UserInfo young = new UserInfo(80000000, "Neil", "deGrasse Tyson");
                return new AgeInfo(old, young);

                CREATE VIEW FRENS AS 
                SELECT 
                    USERS.USER_ID,
                    users.FIRST_NAME,
                    users.LAST_NAME,
                    users.YEAR_OF_BIRTH,
                    users.MONTH_OF_BIRTH,
                    users.DAY_OF_BIRTH
                FROM (
                    SELECT * FROM project2.PUBLIC_FRIENDS frens
                    WHERE frens.USER2_ID = 500 or frens.USER1_ID = 500
                ) monke
                JOIN project2.PUBLIC_USERS users 
                ON users.USER_ID = monke.USER1_ID or users.USER_ID = monke.USER2_ID
                WHERE users.USER_ID != 500
                ORDER BY 
                    users.YEAR_OF_BIRTH,
                    users.MONTH_OF_BIRTH,
                    users.DAY_OF_BIRTH; 

            */

            stmt.executeUpdate(
                "CREATE VIEW FRENS AS " +
                "SELECT " +
                "    users.USER_ID, " +
                "    users.FIRST_NAME, " +
                "    users.LAST_NAME, " +
                "    users.YEAR_OF_BIRTH, " +
                "    users.MONTH_OF_BIRTH, " +
                "    users.DAY_OF_BIRTH " +
                "FROM ( " +
                "    SELECT * FROM " + FriendsTable + " frens" +
                "    WHERE frens.USER2_ID = " + userID + " or frens.USER1_ID = " + userID + " " +
                ") monke " +
                "JOIN " + UsersTable + " users " +
                "ON users.USER_ID = monke.USER1_ID or users.USER_ID = monke.USER2_ID " +
                "WHERE users.USER_ID != " + userID + " " +
                "ORDER BY " +
                "    users.YEAR_OF_BIRTH asc, " +
                "    users.MONTH_OF_BIRTH asc, " +
                "    users.DAY_OF_BIRTH asc "
            );


            UserInfo old = new UserInfo(12000000, "Galileo", "Galilei");
            UserInfo young = new UserInfo(80000000, "Neil", "deGrasse Tyson");
            ResultSet smallest = stmt.executeQuery("SELECT * FROM FRENS ");
            while (smallest.next()) {
                young = new UserInfo(smallest.getLong(1), smallest.getString(2), smallest.getString(3));
                break;
            }

            ResultSet largest = stmt.executeQuery("SELECT * FROM FRENS ORDER BY YEAR_OF_BIRTH desc, MONTH_OF_BIRTH desc, DAY_OF_BIRTH desc ");
            while (largest.next()) {
                old = new UserInfo(largest.getLong(1), largest.getString(2), largest.getString(3));
                break;
            }

            stmt.executeUpdate("DROP VIEW FRENS ");

            return new AgeInfo(young, old);
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return new AgeInfo(new UserInfo(-1, "ERROR", "ERROR"), new UserInfo(-1, "ERROR", "ERROR"));
        }
    }
    
    @Override
    // Query 9
    // -----------------------------------------------------------------------------------
    // GOALS: (A) Find all pairs of users that meet each of the following criteria
    //              (i) same last name
    //              (ii) same hometown
    //              (iii) are friends
    //              (iv) less than 10 birth years apart
    public FakebookArrayList<SiblingInfo> findPotentialSiblings() throws SQLException {
        FakebookArrayList<SiblingInfo> results = new FakebookArrayList<SiblingInfo>("\n");
        
        try (Statement stmt = oracle.createStatement(FakebookOracleConstants.AllScroll, FakebookOracleConstants.ReadOnly)) {
            /*
                EXAMPLE DATA STRUCTURE USAGE
                ============================================
                UserInfo u1 = new UserInfo(81023, "Kim", "Kardashian");
                UserInfo u2 = new UserInfo(17231, "Kourtney", "Kardashian");
                SiblingInfo si = new SiblingInfo(u1, u2);
                results.add(si);


                SELECT 
                    user1.USER_ID AS USER_ID1, 
                    user1.FIRST_NAME AS USER1_FIRST_NAME, 
                    user1.LAST_NAME AS USER1_LAST_NAME, 
                    user2.USER_ID AS USER_ID2,
                    user2.FIRST_NAME AS USER2_FIRST_NAME,
                    user2.LAST_NAME AS USER2_LAST_NAME
                FROM project2.PUBLIC_USERS user1
                INNER JOIN project2.PUBLIC_USERS user2
                ON user1.LAST_NAME = user2.LAST_NAME AND user1.USER_ID < user2.USER_ID AND ABS(user1.YEAR_OF_BIRTH - user2.YEAR_OF_BIRTH) <= 10
                JOIN project2.PUBLIC_FRIENDS friends
                ON user1.USER_ID = friends.USER1_ID AND user2.USER_ID = friends.USER2_ID 
                JOIN project2.PUBLIC_USER_HOMETOWN_CITY hometowns1
                ON user1.USER_ID = hometowns1.USER_ID
                JOIN project2.PUBLIC_USER_HOMETOWN_CITY hometowns2
                ON user2.USER_ID = hometowns2.USER_ID
                WHERE hometowns2.HOMETOWN_CITY_ID = hometowns1.HOMETOWN_CITY_ID
                GROUP BY user1.USER_ID, user2.USER_ID, user1.YEAR_OF_BIRTH, user2.YEAR_OF_BIRTH, user1.FIRST_NAME, user1.LAST_NAME, user2.FIRST_NAME, user2.LAST_NAME
                ORDER BY user1.USER_ID, user2.USER_ID ASC
            */


            ResultSet res = stmt.executeQuery("
                SELECT 
                    user1.USER_ID AS USER_ID1, 
                    user1.FIRST_NAME AS USER1_FIRST_NAME, 
                    user1.LAST_NAME AS USER1_LAST_NAME, 
                    user2.USER_ID AS USER_ID2,
                    user2.FIRST_NAME AS USER2_FIRST_NAME,
                    user2.LAST_NAME AS USER2_LAST_NAME
                FROM project2.PUBLIC_USERS user1
                INNER JOIN project2.PUBLIC_USERS user2
                ON user1.LAST_NAME = user2.LAST_NAME AND user1.USER_ID < user2.USER_ID AND ABS(user1.YEAR_OF_BIRTH - user2.YEAR_OF_BIRTH) <= 10
                JOIN project2.PUBLIC_FRIENDS friends
                ON user1.USER_ID = friends.USER1_ID AND user2.USER_ID = friends.USER2_ID 
                JOIN project2.PUBLIC_USER_HOMETOWN_CITY hometowns1
                ON user1.USER_ID = hometowns1.USER_ID
                JOIN project2.PUBLIC_USER_HOMETOWN_CITY hometowns2
                ON user2.USER_ID = hometowns2.USER_ID
                WHERE hometowns2.HOMETOWN_CITY_ID = hometowns1.HOMETOWN_CITY_ID
                GROUP BY user1.USER_ID, user2.USER_ID, user1.YEAR_OF_BIRTH, user2.YEAR_OF_BIRTH, user1.FIRST_NAME, user1.LAST_NAME, user2.FIRST_NAME, user2.LAST_NAME
                ORDER BY user1.USER_ID, user2.USER_ID ASC
            ");

            while (res.next()) {
                UserInfo u1 = new UserInfo(res.getLong(1), res.getString(2), res.getString(3));
                UserInfo u2 = new UserInfo(res.getLong(4), res.getString(5), res.getString(6));
                SiblingInfo si = new SiblingInfo(u1, u2);
                results.add(si);
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return results;
    }
    
    // Member Variables
    private Connection oracle;
    private final String UsersTable = FakebookOracleConstants.UsersTable;
    private final String CitiesTable = FakebookOracleConstants.CitiesTable;
    private final String FriendsTable = FakebookOracleConstants.FriendsTable;
    private final String CurrentCitiesTable = FakebookOracleConstants.CurrentCitiesTable;
    private final String HometownCitiesTable = FakebookOracleConstants.HometownCitiesTable;
    private final String ProgramsTable = FakebookOracleConstants.ProgramsTable;
    private final String EducationTable = FakebookOracleConstants.EducationTable;
    private final String EventsTable = FakebookOracleConstants.EventsTable;
    private final String AlbumsTable = FakebookOracleConstants.AlbumsTable;
    private final String PhotosTable = FakebookOracleConstants.PhotosTable;
    private final String TagsTable = FakebookOracleConstants.TagsTable;
}
