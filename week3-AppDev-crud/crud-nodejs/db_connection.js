const cassandra = require('cassandra-driver');

function init_connection(){
    var connection = {}
    connection.client = new cassandra.Client({ 
        cloud: { secureConnectBundle: '/Users/cedricklunven/Downloads/secure-connect-devworkshopdb.zip' },
        credentials: { username: 'todouser', password: 'todopassword' } 
    });
    return connection
}

connection = init_connection()

module.exports = connection;