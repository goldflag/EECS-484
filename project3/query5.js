// find the oldest friend for each user who has a friend. 
// For simplicity, use only year of birth to determine age, if there is a tie, use the one with smallest user_id
// return a javascript object : key is the user_id and the value is the oldest_friend id
// You may find query 2 and query 3 helpful. You can create selections if you want. Do not modify users collection.
//
//You should return something like this:(order does not matter)
//{user1:userx1, user2:userx2, user3:userx3,...}

function oldest_friend(dbname){
  db = db.getSiblingDB(dbname);

  const monke = db.flat_users.aggregate([
    { $lookup:
        {
           from: "users",
           localField: "friends",
           foreignField: "user_id",
           as: "fren"
        }
    }
  ]).toArray();

  let results = {};
  monke.forEach(val => {
    if (!(val.user_id in results)) {
      results[val.user_id] = {
        id: val.friends,
        age: val.fren[0].YOB
      };
    }
    if (val.fren[0].YOB >= results[val.user_id].age && val.fren[0].user_id < results[val.user_id].id) {
      results[val.user_id] = {
        id: val.friends,
        age: val.fren[0].YOB
      };
    }
  });
  // TODO: implement oldest friends
  // return an javascript object described above
  return results
}
