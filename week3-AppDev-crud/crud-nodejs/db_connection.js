const cassandra = require('cassandra-driver');

function init_connection(){
    var connection = {}
    connection.client = new cassandra.Client({ 
        cloud: { secureConnectBundle: '/home/ubuntu/workspace/creds.zip' },
        credentials: { username: 'KVUser', password: 'KVPassword' } 
    });
    return connection
}

connection = init_connection()

module.exports = connection;