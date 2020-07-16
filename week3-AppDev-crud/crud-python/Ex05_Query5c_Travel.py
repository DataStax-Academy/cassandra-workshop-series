#!/usr/bin/env python3
from db_connection import Connection
import uuid
import random
import datetime
import time
from cassandra.query import BatchStatement
journey_id = uuid.UUID('9f76fe1e-c778-11ea-9c18-acde48001122')
spacecraft_name = 'Bettina'
insertSpeed       = 'INSERT INTO spacecraft_speed_over_time (spacecraft_name,journey_id,speed,reading_time,speed_unit) VALUES (?,?,?,?,?)'
insertTemperature = 'INSERT INTO spacecraft_temperature_over_time (spacecraft_name,journey_id,temperature,reading_time,temperature_unit) VALUES (?,?,?,?,?)'
insertPressure    = 'INSERT INTO spacecraft_pressure_over_time (spacecraft_name,journey_id,pressure,reading_time,pressure_unit) VALUES (?,?,?,?,?)'
insertLocation    = 'INSERT INTO spacecraft_location_over_time (spacecraft_name,journey_id,location,reading_time,location_unit) VALUES (?,?,?,?,?)'
try:
    connection = Connection()
    prepared_insertSpeed = connection.session.prepare(insertSpeed)
    prepared_insertTemperature = connection.session.prepare(insertTemperature)
    prepared_insertPressure = connection.session.prepare(insertPressure)
    prepared_insertLocation = connection.session.prepare(insertLocation)
    for i in range (0,50):
        speed        = 300+i+random.randint(0,10)
        pressure     = random.randint(0,20)
        temperature  = random.randint(0,300)
        x            = 13+i
        y            = 14+i
        z            = 36+i
        readingTime = datetime.datetime.now()
        class Location(object):
            def __init__(self, x_coordinate, y_coordinate, z_coordinate):
                self.x_coordinate = x_coordinate
                self.y_coordinate = y_coordinate
                self.z_coordinate = z_coordinate
        time.sleep(0.1)
        print("{}/50 - Travelling..".format(i))
        batch = BatchStatement()
        batch.add(prepared_insertLocation, [spacecraft_name, journey_id, Location(x,y,z),readingTime,'AU' ])
        batch.add(prepared_insertSpeed, [spacecraft_name, journey_id, speed,readingTime,'km/hour' ])
        batch.add(prepared_insertTemperature, [spacecraft_name, journey_id, pressure, readingTime,'Pa' ])
        batch.add(prepared_insertPressure, [spacecraft_name, journey_id, temperature, readingTime,'K' ])
        connection.session.execute(batch)
except:
    print('Failure')
else:
    print("Reading saved for journey {}".format(str(journey_id)))
    print('Success')
finally:
    connection.close()