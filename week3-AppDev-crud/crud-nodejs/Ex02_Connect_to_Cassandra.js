const connection = require('./db_connection')
const Uuid = require('cassandra-driver').types.Uuid;

connection.client
.execute('SELECT * FROM system.local')
.then(function(result){
	console.log("========================================")
	console.log("Start exercise")
    result.rows.forEach(row => {
        console.log("Your are now connected to cluster '%s'", row.cluster_name)
    })
    connection.client.shutdown()
    console.log("SUCCESS")
    console.log("========================================")
})
.catch(function(error){
    console.log(error.message)
    connection.client.shutdown()
});