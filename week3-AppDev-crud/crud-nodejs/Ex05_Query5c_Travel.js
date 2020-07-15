const connection = require('./db_connection')
const Uuid = require('cassandra-driver').types.Uuid;
const TimeUuid = require('cassandra-driver').types.TimeUuid;
const executeConcurrent = require('cassandra-driver').concurrent.executeConcurrent;

// Defining our journey
const journey_id = TimeUuid.fromString('84121060-c66e-11ea-a82e-f931183227ac')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'

const insertSpeed       = 'INSERT INTO spacecraft_speed_over_time (spacecraft_name,journey_id,speed,reading_time,speed_unit) VALUES (?,?,?,?,?)'
const insertTemperature = 'INSERT INTO spacecraft_temperature_over_time (spacecraft_name,journey_id,temperature,reading_time,temperature_unit) VALUES (?,?,?,?,?)'
const insertPressure    = 'INSERT INTO spacecraft_pressure_over_time (spacecraft_name,journey_id,pressure,reading_time,pressure_unit) VALUES (?,?,?,?,?)'
const insertLocation    = 'INSERT INTO spacecraft_location_over_time (spacecraft_name,journey_id,location,reading_time,location_unit) VALUES (?,?,?,?,?)'

async function executeQueries() {
    console.log("========================================")
    console.log("Start exercise")

    function sleep(millis) {
        return new Promise(resolve => setTimeout(resolve, millis));
    }

    var i;
    for (i = 1; i <= 50; i++) {
        var speed        = 300+i+Math.random()*10
        var pressure     = Math.random()*20
        var temperature  = Math.random()*300
        var x            = 13+i
        var y            = 14+i
        var z            = 36+i
        var readingTime = new Date();
        
        var location = {
        x_coordinate: x,
        y_coordinate: y,
        z_coordinate: z
        }

        var myBatch = [
        { query: insertSpeed, params: [spacecraft_name, journey_id, speed,readingTime,'km/hour' ] },
        { query: insertTemperature, params: [spacecraft_name, journey_id, pressure, readingTime,'Pa' ] },
        { query: insertPressure, params: [spacecraft_name, journey_id, temperature, readingTime,'K' ] },
        { query: insertLocation, params: [spacecraft_name, journey_id, location,readingTime,'AU' ] }
        ]

            await sleep(1000)
            console.log("{%i/50} - Travelling..",i)

            const result = await executeConcurrent(connection.client, myBatch, { prepare: true })
            .then(function (result){
            })
            .catch(function (error){
                console.log(error.message)
            });
    }
    console.log("Reading saved for journey %s", journey_id.toString());
    console.log("========================================");
    await connection.client.shutdown();
}

executeQueries();
