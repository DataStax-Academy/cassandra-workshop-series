const connection = require('./db_connection')

// this is a update statement in nodejs
connection.client.execute(
    'UPDATE killrvideo.user_credentials SET password = ? WHERE email = ?',
    ['Cr1st1n@sN3wP@ssW0rd', 'cv@datastax.com'],
    { prepare : true }
)
.then(function (result){
    console.log('Success')
    connection.client.shutdown()
})
.catch(function (error){
     console.log(error.message)
     connection.client.shutdown()
});