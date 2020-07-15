const connection = require('./db_connection')
const Uuid 		 = require('cassandra-driver').types.Uuid;
const TimeUuid 	 = require('cassandra-driver').types.TimeUuid;

// Defining our journey
const journey_id      = UUID.fromString('<enter_your_journey_id>')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'

const insertSpeed       = 'INSERT INTO spacecraft_speed_over_time (spacecraft_name,journey_id,speed,reading_time,speed_unit) VALUES (?,?,?,?,?)'
const insertTemperature = 'INSERT INTO spacecraft_temperature_over_time (spacecraft_name,journey_id,temperature,reading_time,temperature_unit) VALUES (?,?,?,?,?)'
const insertPressure    = 'INSERT INTO spacecraft_pressure_over_time (spacecraft_name,journey_id,pressure,reading_time,pressure_unit) VALUES (?,?,?,?,?)'
const insertLocation    = 'INSERT INTO spacecraft_location_over_time (spacecraft_name,journey_id,location,reading_time,location_unit) VALUES (?,?,?,?,?)'

console.log("========================================")
console.log("Start exercise")

// Batch Statements
// Loop
// Insert time
// Random infos
connection.client.execute(insert, params)
.then(function (result){
	console.log("SUCCESS")
    connection.client.shutdown()
})
.catch(function (error){
     console.log(error.message)
     connection.client.shutdown()
});

console.log("Reading saved for journey %s", journey_id.toString());
console.log("========================================")
