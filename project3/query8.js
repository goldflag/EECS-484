// query 8: Find the city average friend count per user using MapReduce
// Using the same terminology in query 6, we are asking you to write the mapper,
// reducer and finalizer to find the average friend count for each city.


var city_average_friendcount_mapper = function() {
  // implement the Map function of average friend count
  
  emit(this.hometown.city, {"Users": 1, "friends": this.friends.length});
};

var city_average_friendcount_reducer = function(key, values) {
  // implement the reduce function of average friend count
  var cityUsers = 0;
  var citySum = 0;
  
  
  for(var i = 0; i < values.length; i++){
    
    cityUsers = cityUsers + values[i]["Users"];
    citySum = citySum + values[i]["friends"];
  }
  
  var ret = {"friends": citySum, "Users": cityUsers};
  
  return ret;
};

var city_average_friendcount_finalizer = function(key, reduceVal) {
  // We've implemented a simple forwarding finalize function. This implementation 
  // is naive: it just forwards the reduceVal to the output collection.
  // Feel free to change it if needed.
  
  //Return Average friend for city
  
  return reduceVal["friends"] / reduceVal["Users"];
}
