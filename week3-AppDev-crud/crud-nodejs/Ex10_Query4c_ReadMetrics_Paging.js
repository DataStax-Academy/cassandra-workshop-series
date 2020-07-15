const connection = require('./db_connection')
const TimeUuid = require('cassandra-driver').types.TimeUuid;

// Defining our journey
const journey_id      = TimeUuid.fromString('84121060-c66e-11ea-a82e-f931183227ac')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'

async function exercice10() {
 // Page 1
 const options1 = { prepare: true , fetchSize: 5 };
 const queryMetrics = 'select * from spacecraft_speed_over_time where spacecraft_name=? AND journey_id=?'
 const result1 = await connection.client.execute(queryMetrics, [spacecraft_name, journey_id], options1);
 console.log("Page1: %i items", result1.rows.length);
 var offset = 0;
 result1.rows.forEach(row => {
 		offset = offset + 1
    	console.log("idx:%s, time=%s, value=%s", offset, row.reading_time, row.speed)
 })
 let pageState = result1.pageState;

 // Page 2
 const options2 = { pageState, prepare: true, fetchSize: 5 };
 const result2 = await connection.client.execute(queryMetrics, [spacecraft_name, journey_id], options2);
 console.log("Page2: %i items", result2.rows.length);
 result2.rows.forEach(row => {
    	console.log("idx:%s, time=%s, value=%s", offset, row.reading_time, row.speed)
 })

 connection.client.shutdown();
}

console.log("========================================")
console.log("Start exercise")
exercice10();
console.log("========================================")