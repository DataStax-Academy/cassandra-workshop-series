const connection = require('./db_connection')
const Uuid = require('cassandra-driver').types.Uuid;
const TimeUuid = require('cassandra-driver').types.TimeUuid;

// this is an insert statement in nodejs
const journey_id = TimeUuid.now();
const insert = 'INSERT INTO todoapp.spacecraft_journey_catalog (spacecraft_name, journey_id, active, summary) VALUES (?,?,?,?);';
const params = ['Crew Dragon Endeavour,SpaceX', journey_id ,true,'Bring Astronauts to ISS'];
connection.client.execute(insert, params)
.then(function (result){
	console.log("========================================")
	console.log("Start exercise")
	console.log("Journey created : %s", journey_id.toString())
    console.log("SUCCESS")
    console.log("========================================")
    connection.client.shutdown()
})
.catch(function (error){
     console.log(error.message)
     connection.client.shutdown()
});