// query6 : Find the Average friend count per user for users
//
// Return a decimal variable as the average user friend count of all users
// in the users document.

function find_average_friendcount(dbname){
  db = db.getSiblingDB(dbname)

  //Array of all users, dont need _id
  
  var users = db.users.find( { }, {user_id: 1, friends: 1, _id: 0}).toArray();
  
  //Set friends equal to zero
  
  var totalFriends = 0;
  
  //Loop through users and add up all friends length
  for(var i = 0; i < users.length(); users++){
    totalFriends += users[i]["friends"].length;
  }
  
  
  var average = totalFriends / users.length();  

  
  return average;
}
