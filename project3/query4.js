// query 4: find user pairs (A,B) that meet the following constraints:
// i) user A is male and user B is female
// ii) their Year_Of_Birth difference is less than year_diff
// iii) user A and B are not friends
// iv) user A and B are from the same hometown city
// The following is the schema for output pairs:
// [
//      [user_id1, user_id2],
//      [user_id1, user_id3],
//      [user_id4, user_id2],
//      ...
//  ]
// user_id is the field from the users collection. Do not use the _id field in users.

function suggest_friends(year_diff, dbname) {
  db = db.getSiblingDB(dbname);
  var pairs = [];

  var males = [];
  var females = [];

  //Putting males and females in 2 seperate ARRAYS
  males = db.users.find({ gender: "male" }).toArray();
  females = db.users.find({ gender: "female" }).toArray();

  //Add a bool variable so you dont get double pairs
  var friends = false;

  //Looping through all boys and girls
  for (var boy = 0; boy < males.length; boy++) {
    for (var girl = 0; girl < females.length; girl++) {
      //Check to see if same city

      if (
        males[boy]["hometown"]["country"] ==
          females[girl]["hometown"]["country"] &&
        males[boy]["hometown"]["state"] == females[girl]["hometown"]["state"] &&
        males[boy]["hometown"]["city"] == females[girl]["hometown"]["city"]
      ) {
        //Check to see if year constraint is met. Use absolute value so you get both higher and lower
        if (Math.abs(males[boy]["YOB"] - females[girl]["YOB"]) < year_diff) {
          friends = false;

          for (var i = 0; i < males[boy]["friends"].length && !friends; i++) {
            if (males[boy]["friends"][i] == females[girl]["user_id"]) {
              friends = true;
            }
          }
          for (var i = 0; i < females[girl]["friends"].length && !friends; i++) {
            if (females[girl]["friends"][i] == males[boy]["user_id"]) {
              friends = true;
            }
          }
          //Now push back non friend pairs
          if (!friends) {
            pairs.push([males[boy]["user_id"], females[girl]["user_id"]]);
          }
          // if (!friends) {
          //   pairs.push([
          //     males[boy]["user_id"] +
          //       " " +
          //       males[boy]["gender"] +
          //       " " +
          //       males[boy]["hometown"]["city"] +
          //       " " +
          //       males[boy]["YOB"],
          //     females[girl]["user_id"] +
          //       " " +
          //       females[girl]["gender"] +
          //       " " +
          //       females[girl]["hometown"]["city"] +
          //       " " +
          //       females[girl]["YOB"],
          //   ]);
          // }
        }
      }
    }
  }

  return pairs;
}
