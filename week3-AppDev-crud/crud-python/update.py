#!/usr/bin/env python3
from db_connection import Connection

# this is a update statement in python
try:
    connection = Connection()
    connection.session.execute(
        "UPDATE killrvideo.user_credentials SET password = %s WHERE email = %s",
        ['Cr1st1n@sN3wP@ssW0rd', 'cv@datastax.com']
    )
except:
    print('Failure')
else:
    print('Success')
finally:
    connection.close()