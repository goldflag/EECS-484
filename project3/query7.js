// query 7: Find the number of users born in each month using MapReduce


//Website below for help
//https://www.mongodb.com/docs/manual/tutorial/map-reduce-examples/

var num_month_mapper = function() {
  // Implement the map function
  // emit(this.MOB, {"Users": 1});
  if (typeof this.MOB === 'number') {
    emit(this.MOB, 1);
  }
}

var num_month_reducer = function(key, values) {
  // Implement the reduce function
  // var sum = 0;
  
  // for(var i = 0; i < values.length; i++){
  //   sum = sum + values[i]["Users"];
  // }
  
  // var ret = {"Users": sum};
    
  // return ret;
  return Array.sum(values);
}

var num_month_finalizer = function(key, reduceVal) {
  // We've implemented a simple forwarding finalize function. This implementation 
  // is naive: it just forwards the reduceVal to the output collection.
  // Feel free to change it if needed. 
  
  //Not sure what to return, left as is
  var ret = reduceVal;
  return ret;
}
