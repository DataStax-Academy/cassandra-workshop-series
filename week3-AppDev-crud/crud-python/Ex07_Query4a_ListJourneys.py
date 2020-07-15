#!/usr/bin/env python3
from db_connection import Connection
import uuid

print('========================================')
print('Start exercise')
try:
    connection = Connection()
    output = connection.session.execute(
        "select * from spacecraft_journey_catalog where spacecraft_name=%s",
        ['Crew Dragon Endeavour,SpaceX']
    )
    for row in output:
       print('- Journey;', row.journey_id, ' Summary:', row.summary)
       
except Exception as e: 
    print(e)
    print('Failure')
else:

    print('Success')
    print('Closing connection (up to 10s)')
finally:
    connection.close()
print('========================================')
