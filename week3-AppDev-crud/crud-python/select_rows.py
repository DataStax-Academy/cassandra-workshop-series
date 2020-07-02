#!/usr/bin/env python3
from db_connection import Connection

# this is a select statement in python
connection = Connection()
output = connection.session.execute("SELECT * FROM killrvideo.user_credentials WHERE email = %s",
['cv@datastax.com'])
for row in output:
    print(row)
connection.close()