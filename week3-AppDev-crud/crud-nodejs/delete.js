const connection = require('./db_connection')

// this is a delete statement in nodejs
connection.client.execute(
    'DELETE FROM killrvideo.user_credentials WHERE email = ?',
    ['cv@datastax.com']
)
.then(function (result){
    console.log('Success')
    connection.client.shutdown()
})
.catch(function (error){
     console.log(error.message)
     connection.client.shutdown()
});