// query2 : unwind friends and create a collection called 'flat_users' 
// where each document has the following schema:
/*
{
  user_id:xxx
  friends:xxx
}
*/

function unwind_friends(dbname){
    db = db.getSiblingDB(dbname);
    // TODO: unwind friends
     //Unwind Friends, use $project to pass along user_id & friends, $out to flat_users
    //Set user_id and friends to 1 to show. Hide  _id so set to 0  
    db.users.aggregate( [ { $unwind: "$friends" }, {$project : {user_id : 1, friends : 1, _id : 0} }, {$out : "flat_users"} ] );
 
}
