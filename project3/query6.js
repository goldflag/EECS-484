// query6 : Find the Average friend count per user for users
//
// Return a decimal variable as the average user friend count of all users
// in the users document.

function find_average_friendcount(dbname) {
  db = db.getSiblingDB(dbname);

  var users = db.users.aggregate([
    { $project: { count: { $size: { $ifNull: ["$friends", []] } } } },
  ]).toArray();

  return users.reduce((prev, curr) => prev + curr.count, 0) / users.length;
}
