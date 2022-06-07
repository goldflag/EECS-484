// query1 : find users whose hometown citys the specified city. 

function find_user(city, dbname){
    db = db.getSiblingDB(dbname);
    var results = [];
    // TODO: return a Javascript array of user_ids. 
    // db.users.find(...);

    results = db.users.find({"hometown.city" : city}, {user_id: 1, _id: 0}).toArray();
    
    return results;
    
}
