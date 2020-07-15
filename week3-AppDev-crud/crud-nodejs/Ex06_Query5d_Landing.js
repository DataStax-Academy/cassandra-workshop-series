const connection = require('./db_connection')

// Defining our journey
const journey_id      = UUID.fromString('<enter_your_journey_id>')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'

console.log("========================================")
console.log("Start exercise")

connection.client.execute(
    'UPDATE spacecraft_journey_catalog SET active=false, end=? WHERE spacecraft_name=? AND journey_id=?',
    [ Date.now(), spacecraft_name, journey_id],
    { prepare : true }
)
.then(function (result){
	console.log("Journey %s has now landed", journey_id.toString());
    connection.client.shutdown()
    console.log("SUCCESS")
})
.catch(function (error){
     console.log(error.message)
     connection.client.shutdown()
     console.log("FAILED")
});

console.log("========================================")