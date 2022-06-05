import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.Vector;



//json.simple 1.1
// import org.json.simple.JSONObject;
// import org.json.simple.JSONArray;

// Alternate implementation of JSON modules.
import org.json.JSONObject;
import org.json.JSONArray;

public class GetData{
	
    static String prefix = "project3.";
	
    // You must use the following variable as the JDBC connection
    Connection oracleConnection = null;
	
    // You must refer to the following variables for the corresponding 
    // tables in your database

    String cityTableName = null;
    String userTableName = null;
    String friendsTableName = null;
    String currentCityTableName = null;
    String hometownCityTableName = null;
    String programTableName = null;
    String educationTableName = null;
    String eventTableName = null;
    String participantTableName = null;
    String albumTableName = null;
    String photoTableName = null;
    String coverPhotoTableName = null;
    String tagTableName = null;

    // This is the data structure to store all users' information
    // DO NOT change the name
    JSONArray users_info = new JSONArray();		// declare a new JSONArray

	
    // DO NOT modify this constructor
    public GetData(String u, Connection c) {
	super();
	String dataType = u;
	oracleConnection = c;
	// You will use the following tables in your Java code
	cityTableName = prefix+dataType+"_CITIES";
	userTableName = prefix+dataType+"_USERS";
	friendsTableName = prefix+dataType+"_FRIENDS";
	currentCityTableName = prefix+dataType+"_USER_CURRENT_CITIES";
	hometownCityTableName = prefix+dataType+"_USER_HOMETOWN_CITIES";
	programTableName = prefix+dataType+"_PROGRAMS";
	educationTableName = prefix+dataType+"_EDUCATION";
	eventTableName = prefix+dataType+"_USER_EVENTS";
	albumTableName = prefix+dataType+"_ALBUMS";
	photoTableName = prefix+dataType+"_PHOTOS";
	tagTableName = prefix+dataType+"_TAGS";
    }
	
	
	
	
    //implement this function

    @SuppressWarnings("unchecked")
    public JSONArray toJSON() throws SQLException{ 

    	JSONArray users_info = new JSONArray();
		
	    // Your implementation goes here....

        /*
            SELECT * FROM project3.PUBLIC_USERS users
            INNER JOIN project3.PUBLIC_USER_HOMETOWN_CITIES hometown 
            ON users.USER_ID = hometown.USER_ID 
            INNER JOIN project3.PUBLIC_USER_CURRENT_CITIES current  
            ON users.USER_ID = current.USER_ID;



            SELECT 
                users.USER_ID, 
                users.FIRST_NAME, 
                users.LAST_NAME, 
                users.YEAR_OF_BIRTH , 
                users.MONTH_OF_BIRTH, 
                users.DAY_OF_BIRTH, 
                users.GENDER,
                cityc.CITY_NAME,
                cityc.STATE_NAME,
                cityc.COUNTRY_NAME,
                cityc.CITY_NAME,
                cityc.STATE_NAME,
                cityc.COUNTRY_NAME,
                COLLECT(UNIQUE(frens.USER2_ID)) as ID2
            FROM project3.PUBLIC_USERS users 
            INNER JOIN project3.PUBLIC_USER_CURRENT_CITIES current_city 
            ON users.USER_ID = current_city.USER_ID 
            INNER JOIN project3.PUBLIC_USER_HOMETOWN_CITIES hometown_city 
            ON users.USER_ID = hometown_city.USER_ID 
            INNER JOIN PROJECT3.PUBLIC_CITIES cityc 
            ON current_city.CURRENT_CITY_ID = cityc.CITY_ID 
            INNER JOIN PROJECT3.PUBLIC_CITIES cityh
            ON hometown_city.HOMETOWN_CITY_ID = cityh.CITY_ID 
            INNER JOIN PROJECT3.PUBLIC_FRIENDS frens 
            ON frens.USER1_ID = users.USER_ID 
            WHERE users.USER_ID = 585
            GROUP BY 
                users.USER_ID, 
                users.FIRST_NAME, 
                users.LAST_NAME, 
                users.YEAR_OF_BIRTH , 
                users.MONTH_OF_BIRTH, 
                users.DAY_OF_BIRTH, 
                users.GENDER,
                cityc.CITY_NAME,
                cityc.STATE_NAME,
                cityc.COUNTRY_NAME,
                cityc.CITY_NAME,
                cityc.STATE_NAME,
                cityc.COUNTRY_NAME
            ORDER BY users.USER_ID asc;

            DESC PROJECT3.PUBLIC_CITIES;
            DESC PROJECT3.PUBLIC_friends;
         */

        try (Statement stmt = oracleConnection.createStatement()) {
            String query = "SELECT  " +
                "    users.USER_ID,  " +
                "    users.FIRST_NAME,  " +
                "    users.LAST_NAME,  " +
                "    users.YEAR_OF_BIRTH ,  " +
                "    users.MONTH_OF_BIRTH,  " +
                "    users.DAY_OF_BIRTH,  " +
                "    users.GENDER, " +
                "    cityc.CITY_NAME, " +
                "    cityc.STATE_NAME, " +
                "    cityc.COUNTRY_NAME, " +
                "    cityc.CITY_NAME, " +
                "    cityc.STATE_NAME, " +
                "    cityc.COUNTRY_NAME, " +
                "    COLLECT(UNIQUE(frens.USER2_ID)) as ID2 " +
                "FROM project3.PUBLIC_USERS users  " +
                "INNER JOIN project3.PUBLIC_USER_CURRENT_CITIES current_city  " +
                "ON users.USER_ID = current_city.USER_ID  " +
                "INNER JOIN project3.PUBLIC_USER_HOMETOWN_CITIES hometown_city " + 
                "ON users.USER_ID = hometown_city.USER_ID  " +
                "INNER JOIN PROJECT3.PUBLIC_CITIES cityc  " +
                "ON current_city.CURRENT_CITY_ID = cityc.CITY_ID  " +
                "INNER JOIN PROJECT3.PUBLIC_CITIES cityh " +
                "ON hometown_city.HOMETOWN_CITY_ID = cityh.CITY_ID  " +
                "INNER JOIN PROJECT3.PUBLIC_FRIENDS frens  " +
                "ON frens.USER1_ID = users.USER_ID  " +
                "WHERE users.USER_ID = 585 " +
                "GROUP BY  " +
                "    users.USER_ID,  " +
                "    users.FIRST_NAME,  " +
                "    users.LAST_NAME,  " +
                "    users.YEAR_OF_BIRTH ,  " +
                "    users.MONTH_OF_BIRTH,  " +
                "    users.DAY_OF_BIRTH,  " +
                "    users.GENDER, " +
                "    cityc.CITY_NAME, " +
                "    cityc.STATE_NAME, " +
                "    cityc.COUNTRY_NAME, " +
                "    cityc.CITY_NAME, " +
                "    cityc.STATE_NAME, " +
                "    cityc.COUNTRY_NAME " +
                "ORDER BY users.USER_ID asc ";
            System.out.println(query);

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println(rs.getInt("USER_ID"));
            }
            // while (rs.next()) {
            //     String coffeeName = rs.getString("COF_NAME");
            //     int supplierID = rs.getInt("SUP_ID");
            //     float price = rs.getFloat("PRICE");
            //     int sales = rs.getInt("SALES");
            //     int total = rs.getInt("TOTAL");
            //     System.out.println(coffeeName + ", " + supplierID + ", " + price +
            //                     ", " + sales + ", " + total);
            //     JSONObject jsonObject = new JSONObject();
            //     users_info.add(jsonObject);
            // }
        }         
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    	
		
		return users_info;
    }

    // This outputs to a file "output.json"
    public void writeJSON(JSONArray users_info) {
	// DO NOT MODIFY this function
	try {
	    FileWriter file = new FileWriter(System.getProperty("user.dir")+"/output.json");
	    file.write(users_info.toString());
	    file.flush();
	    file.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
		
    }
}
