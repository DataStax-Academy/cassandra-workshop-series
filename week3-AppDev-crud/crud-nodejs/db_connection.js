const cassandra = require('cassandra-driver');

// This is the Zip file you downloaded
const SECURE_CONNECT_BUNDLE = '/Users/cedricklunven/Downloads/secure-connect-devworkshopdb.zip'
// This is the username, recommended value was KVUser
const USERNAME = "KVUser";
// This is the password, recommended value was KVPassword
const PASSWORD = "KVPassword";
// This is the keyspace name, recommended value was killrvideo
const KEYSPACE = "killrvideo"; 

// Init the connection and return the client
function init_connection(){
    var connection = {}
    connection.client = new cassandra.Client({ 
        cloud: { secureConnectBundle: SECURE_CONNECT_BUNDLE },
        credentials: { username: USERNAME, password: PASSWORD } 
    });
    return connection
}

connection = init_connection()

module.exports = connection;