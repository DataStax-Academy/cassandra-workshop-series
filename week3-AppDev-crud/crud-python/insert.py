#!/usr/bin/env python3
from db_connection import Connection
import uuid

# this is a insert statement in python
try:
    connection = Connection()
    output = connection.session.execute(
        "INSERT INTO killrvideo.user_credentials (email, password, userid) VALUES (%s, %s, %s)", 
        ('cv@datastax.com', '3@$tC0@$tC@ss@ndr@', uuid.UUID('{55555555-5555-5555-5555-555555555555}'))
    )
except:
    print('Failure')
else:
    print('Success')
finally:
    connection.close()