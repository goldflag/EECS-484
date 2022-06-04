// query1 : find users whose hometown citys the specified city. 

function find_user(city, dbname){
    db = db.getSiblingDB(dbname);
    var results = [];
   
    //Find hometown city who matches city and return user_id with it
    
    results = db.users.find({"hometown.city" : city}, {user_id: 1, _id: 0});
    
                                                               
    return results;
}
