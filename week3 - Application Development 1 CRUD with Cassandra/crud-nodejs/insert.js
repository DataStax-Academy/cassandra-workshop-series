const connection = require('./db_connection')
const Uuid = require('cassandra-driver').types.Uuid;

// this is an insert statement in nodejs
const insert = 'INSERT INTO killrvideo.user_credentials (email, password, userid) VALUES (?,?,?)';
const params = ['cv@datastax.com', '3@$tC0@$tC@ss@ndr@', Uuid.fromString('55555555-5555-5555-5555-555555555555')];
connection.client.execute(insert, params)
.then(function (result){
    console.log('Success')
    connection.client.shutdown()
})
.catch(function (error){
     console.log(error.message)
     connection.client.shutdown()
});