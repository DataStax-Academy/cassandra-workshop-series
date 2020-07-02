const connection = require('./db_connection')

// this is a select statement in nodejs
connection.client
.execute('SELECT * FROM killrvideo.user_credentials WHERE email = ?',
['cv@datastax.com'])
.then(function(result){
    result.rows.forEach(row => {
        console.log(row)
    })
    connection.client.shutdown()
})
.catch(function(error){
    console.log(error.message)
    connection.client.shutdown()
});