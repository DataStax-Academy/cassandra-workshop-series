#!/usr/bin/env python3
from db_connection import Connection
import uuid
import datetime

#Defining our journey
journey_id = uuid.UUID('230995ee-c697-11ea-b7a1-8c85907c08dd')
spacecraft_name = 'Crew Dragon Endeavour,SpaceX'

print("========================================")
print("Start exercise")
print("9..8..7..6..5..4..3..2..1 Ignition")

# this is a update statement in python
try:
    connection = Connection()
    connection.session.execute(
        "UPDATE spacecraft_journey_catalog SET active=true, start= %s WHERE spacecraft_name= %s AND journey_id= %s",
        [datetime.datetime.now(), spacecraft_name, journey_id ])
except Exception as e: 
    print(e)
    print('Failure')
else:
    print("Journey {} has now taken off".format(str(journey_id)))
    print('Success')
finally:
    connection.close()

print('========================================')    