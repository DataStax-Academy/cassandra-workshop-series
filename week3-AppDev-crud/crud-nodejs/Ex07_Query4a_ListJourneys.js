const connection = require('./db_connection')

console.log("========================================")
console.log("Start exercise")

const selectJourneys  = 'select * from spacecraft_journey_catalog where spacecraft_name=?'
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'

connection.client
.execute(selectJourneys, [spacecraft_name])
.then(function(result){
    result.rows.forEach(row => {
        console.log('- Journey: %s Summary: %s', row.journey_id.toString(), row.summary)
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