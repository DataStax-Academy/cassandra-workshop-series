#!/usr/bin/env python3
from db_connection import Connection

# this is a delete statement in python
try:
    connection = Connection()
    output = connection.session.execute(
        "DELETE FROM killrvideo.user_credentials WHERE email = %s",
        ['cv@datastax.com']
    )
except:
    print('Failure')
else:
    print('Success')
finally:
    connection.close()