// query1 : find users whose hometown citys the specified city. 

function find_user(city, dbname){
    db = db.getSiblingDB(dbname);
    return db.users.find({"hometown.city" : city}, {user_id: 1, _id: 0}).toArray().map(({user_id}) => user_id);
}
