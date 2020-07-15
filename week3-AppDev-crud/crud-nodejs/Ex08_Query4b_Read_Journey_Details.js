const connection = require('./db_connection')
const TimeUuid = require('cassandra-driver').types.TimeUuid;

console.log("========================================")
console.log("Start exercise")

// Defining our journey
const journey_id      = TimeUuid.fromString('84121060-c66e-11ea-a82e-f931183227ac')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'

// Query
const queryFindJourneyDetails = 'SELECT * FROM spacecraft_journey_catalog WHERE spacecraft_name=? AND journey_id=?'

connection.client.execute(queryFindJourneyDetails, [spacecraft_name, journey_id])
.then(function(result){
    result.rows.forEach(row => {
        console.log('Journey has been found')
        console.log('- Uid:\t\t %s', row.journey_id);
        console.log('- Spacecraft:\t %s', row.spacecraft_name);
        console.log('- Summary:\t %s', row.summary);
        console.log('- Active:\t %s', row.active);
        console.log('- Takeoff:\t %s', row.start);
        console.log('- Landing:\t %s', row.end);
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