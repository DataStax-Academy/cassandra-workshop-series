#!/usr/bin/env python3
from db_connection import Connection
import uuid

print('========================================')
print('Start exercise')

spacecraft_name = 'Crew Dragon Endeavour,SpaceX'
journey_id      = uuid.UUID('84121060-c66e-11ea-a82e-f931183227ac')

try:
    connection = Connection()
    output = connection.session.execute(
        "select * from spacecraft_speed_over_time where spacecraft_name=%s AND journey_id=%s",
        [spacecraft_name, journey_id]
    )
    offset = 0
    for row in output:
       print("idx=", offset, "time=", row.reading_time, "value=", row.speed)
       offset = offset + 1
except Exception as e: 
    print(e)
    print('Failure')
else:

    print('Success')
    print('Closing connection (up to 10s)')
finally:
    connection.close()
print('========================================')