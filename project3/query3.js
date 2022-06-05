//query3
//create a collection "cities" to store every user that lives in every city
//Each document(city) has following schema:
/*
{
  _id: city
  users:[userids]
}
*/

function cities_table(dbname) {
    db = db.getSiblingDB(dbname);
   
  //Use aggregate and group to set id to cities and group users together by city. Then make array of users push back user_IDs of those who live in city
  //Next output using $out to a collection called "cities
  db.users.aggregate( [ { $group: { _id: "$hometown.city", users: { $push: "$user_id"} } }, {$out: "cities"} ] );


    // Returns nothing. Instead, it creates a collection inside the datbase.

}
