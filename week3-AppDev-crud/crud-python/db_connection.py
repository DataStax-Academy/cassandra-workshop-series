#!/usr/bin/env python3      
from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider

# This is the Zip file you downloaded
SECURE_CONNECT_BUNDLE = '/Users/cedricklunven/Downloads/secure-connect-devworkshopdb.zip'
# This is the username, recommended value was KVUser
USERNAME = "todouser";
# This is the password, recommended value was KVPassword
PASSWORD = "todopassword";
# This is the keyspace name, recommended value was killrvideo
KEYSPACE = "todoapp"; 

class Connection:
    def __init__(self):
        self.secure_connect_bundle=SECURE_CONNECT_BUNDLE
        self.path_to_creds=''
        self.cluster = Cluster(
            cloud={
                'secure_connect_bundle': self.secure_connect_bundle
            },
            auth_provider=PlainTextAuthProvider(USERNAME, PASSWORD)
        )
        self.session = self.cluster.connect(KEYSPACE)
    def close(self):
        self.cluster.shutdown()
        self.session.shutdown()
