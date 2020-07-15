#!/usr/bin/env python3
from db_connection import Connection
import uuid

print('========================================')
print('Start exercise')

journey_id = uuid.uuid1()

try:
    connection = Connection()
    output = connection.session.execute(
        "INSERT INTO spacecraft_journey_catalog (spacecraft_name, journey_id, active, summary) VALUES (%s,%s,%s,%s)",
        ['Crew Dragon Endeavour,SpaceX', journey_id , bool('false'),'Bring Astronauts to ISS']
    )
except Exception as e: 
    print(e)
    print('Failure')
else:
    print('Journey created ', journey_id)
    print('Success')
    print('Closing connection (up to 10s)')
finally:
    connection.close()
print('========================================')
