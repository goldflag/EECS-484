// find the oldest friend for each user who has a friend. 
// For simplicity, use only year of birth to determine age, if there is a tie, use the one with smallest user_id
// return a javascript object : key is the user_id and the value is the oldest_friend id
// You may find query 2 and query 3 helpful. You can create selections if you want. Do not modify users collection.
//
//You should return something like this:(order does not matter)
//{user1:userx1, user2:userx2, user3:userx3,...}

function oldest_friend(dbname){
  db = db.getSiblingDB(dbname);
  const userInfoMap = {};
  const allUsers = db.users.find({}, {user_id: 1, YOB: 1, _id: 0 }).toArray();
  allUsers.forEach(({ user_id, YOB }) => user_id && (userInfoMap[user_id] = YOB));
  
  const frensMap = {};
  db.users.aggregate( [ { $unwind: "$friends" }, {$project : {user_id : 1, friends : 1, _id : 0} }, {$out : "flat_users"} ] );
  const friendsPars = db.flat_users.find().toArray().map(({ user_id, friends}) => [user_id, friends]);
  friendsPars.forEach(([fren1, fren2]) => {

    if (!(fren1 in frensMap)) {
      frensMap[fren1] = {
        fren: fren2,
        age: userInfoMap[fren2]
      }
    }
    if (!(fren2 in frensMap)) {
      frensMap[fren2] = {
        fren: fren1,
        age: userInfoMap[fren1]
      }
    }

    if (userInfoMap[fren2] <= frensMap[fren1].age || (userInfoMap[fren2] == frensMap[fren1].age && frensMap[fren1].fren > fren2)) {
      frensMap[fren1] = {
        fren: fren2,
        age: userInfoMap[fren2]
      }
    }
    if (userInfoMap[fren1] <= frensMap[fren2].age || ( userInfoMap[fren1] == frensMap[fren2].age && frensMap[fren2].fren > fren1)) {
      frensMap[fren2] = {
        fren: fren1,
        age: userInfoMap[fren1]
      }
    }
  });

  const res = {};
  Object.entries(frensMap).forEach(([key, val]) => {
    res[key] = val.fren;
  });
  return res;
}
