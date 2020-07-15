#!/usr/bin/env python3
from db_connection import Connection
import uuid

print('========================================')
print('Start exercise')

spacecraft_name = 'Crew Dragon Endeavour,SpaceX'
journey_id      = uuid.UUID('230995ee-c697-11ea-b7a1-8c85907c08dd')

try:
    connection = Connection()
    output = connection.session.execute(
        "SELECT * FROM spacecraft_journey_catalog WHERE spacecraft_name=%s AND journey_id=%s",
        [spacecraft_name, journey_id]
    )
    for row in output:
       print('Journey has been found')
       print('- Uid:\t\t ', row.journey_id);
       print('- Spacecraft:\t', row.spacecraft_name);
       print('- Summary:\t', row.summary);
       print('- Active:\t', row.active);
       print('- Takeoff:\t', row.start);
       print('- Landing:\t', row.end);
except Exception as e: 
    print(e)
    print('Failure')
else:

    print('Success')
    print('Closing connection (up to 10s)')
finally:
    connection.close()
print('========================================')