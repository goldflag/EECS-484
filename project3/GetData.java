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
                frens.USER2_ID fren_ID
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
            ORDER BY users.USER_ID asc;

            DESC PROJECT3.PUBLIC_CITIES;
            DESC PROJECT3.PUBLIC_friends;
            "    COLLECT(UNIQUE(frens.USER2_ID)) as ID2 \n" +

         */

        try (Statement stmt = oracleConnection.createStatement()) {
            String query = "SELECT  \n" +
                "    users.USER_ID,  \n" +
                "    users.FIRST_NAME,  \n" +
                "    users.LAST_NAME,  \n" +
                "    users.YEAR_OF_BIRTH ,  \n" +
                "    users.MONTH_OF_BIRTH,  \n" +
                "    users.DAY_OF_BIRTH,  \n" +
                "    users.GENDER, \n" +
                "    cityc.CITY_NAME, \n" +
                "    cityc.STATE_NAME, \n" +
                "    cityc.COUNTRY_NAME, \n" +
                "    cityc.CITY_NAME, \n" +
                "    cityc.STATE_NAME, \n" +
                "    cityc.COUNTRY_NAME, \n" +
                "    frens.USER2_ID fren_ID \n" +
                "FROM project3.PUBLIC_USERS users  \n" +
                "INNER JOIN project3.PUBLIC_USER_CURRENT_CITIES current_city  \n" +
                "ON users.USER_ID = current_city.USER_ID  \n" +
                "INNER JOIN project3.PUBLIC_USER_HOMETOWN_CITIES hometown_city \n" + 
                "ON users.USER_ID = hometown_city.USER_ID  \n" +
                "INNER JOIN PROJECT3.PUBLIC_CITIES cityc  \n" +
                "ON current_city.CURRENT_CITY_ID = cityc.CITY_ID  \n" +
                "INNER JOIN PROJECT3.PUBLIC_CITIES cityh \n" +
                "ON hometown_city.HOMETOWN_CITY_ID = cityh.CITY_ID  \n" +
                "INNER JOIN PROJECT3.PUBLIC_FRIENDS frens  \n" +
                "ON frens.USER1_ID = users.USER_ID  \n" +
                "ORDER BY users.USER_ID asc ";

            ResultSet rs = stmt.executeQuery(query);

            JSONObject monke = new JSONObject();
            JSONArray frens = new JSONArray();
            Integer currentId = -1; 
            while (rs.next()) {
                if (rs.getInt("USER_ID") != currentId ) {
                    if (currentId != -1) {
                        monke.put("frens", frens);
                        users_info.put(monke);
                        monke = new JSONObject();
                        frens = new JSONArray();
                    }
                    currentId = rs.getInt("USER_ID");
                    monke = new JSONObject();
                    monke.put("user_id", rs.getInt("USER_ID"));
                    monke.put("gender", rs.getString("GENDER"));
                    monke.put("MOB", rs.getInt("MONTH_OF_BIRTH"));
                    monke.put("DOB", rs.getInt("DAY_OF_BIRTH"));
                    monke.put("YOB", rs.getInt("YEAR_OF_BIRTH"));
                    monke.put("first_name", rs.getString("FIRST_NAME"));
                    monke.put("last_name", rs.getString("LAST_NAME"));
                    JSONObject hometown = new JSONObject();
                    hometown.put("country", rs.getString("COUNTRY_NAME"));
                    hometown.put("city", rs.getString("CITY_NAME"));
                    hometown.put("state", rs.getString("STATE_NAME"));
                    monke.put("hometown", hometown);
                    JSONObject current = new JSONObject();
                    current.put("country", rs.getString("COUNTRY_NAME"));
                    current.put("city", rs.getString("CITY_NAME"));
                    current.put("state", rs.getString("STATE_NAME"));
                    monke.put("current", current);
                    System.out.println(rs.getInt("USER_ID"));
                }
                frens.put(rs.getInt("fren_ID"));
            }
            monke.put("friends", frens);
            users_info.put(monke);
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


