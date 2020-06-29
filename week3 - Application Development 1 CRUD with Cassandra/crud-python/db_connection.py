#!/usr/bin/env python3      
from dse.cluster import Cluster
from dse.auth import PlainTextAuthProvider

class Connection:
    def __init__(self):
        self.secure_connect_bundle='/home/ubuntu/workspace/creds.zip'
        self.path_to_creds=''
        self.cluster = Cluster(
            cloud={
                'secure_connect_bundle': self.secure_connect_bundle
            },
            auth_provider=PlainTextAuthProvider('KVUser', 'KVPassword')
        )
        self.session = self.cluster.connect()
    def close(self):
        self.cluster.shutdown()
        self.session.shutdown()
