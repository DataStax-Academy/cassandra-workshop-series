const connection = require('./db_connection')
const TimeUuid = require('cassandra-driver').types.TimeUuid;

console.log("========================================")
console.log("Start exercise")

// Defining our journey
const journey_id = TimeUuid.fromString('84121060-c66e-11ea-a82e-f931183227ac')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'

// Get query
const queryMetrics = 'select * from spacecraft_speed_over_time where spacecraft_name=? AND journey_id=?'

offset = 0;
connection.client.execute(queryMetrics, [spacecraft_name, journey_id])
.then(function(result){
    result.rows.forEach(row => {
    	console.log("idx:%s, time=%s, value=%s", offset, row.reading_time, row.speed)
    	offset = offset + 1
    })
   connection.client.shutdown()
    console.log("SUCCESS")
})
.catch(function(error){
    console.log(error.message)
    connection.client.shutdown()
    console.log("FAILED")
});

console.log("========================================")