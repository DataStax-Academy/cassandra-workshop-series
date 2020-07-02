from util.data_type_util import uuid_from_string, format_timestamp


# Python object for the location_udt User Defined Type
# see the schema.cql file in this project's root directory for table details
class LocationUDT(object):

    def __init__(self, x_coordinate, y_coordinate, z_coordinate):
        self.x_coordinate = x_coordinate
        self.y_coordinate = y_coordinate
        self.z_coordinate = z_coordinate


# Python object for the spacecraft_location_over_time database table
# see the schema.cql file in this project's root directory for table details
# Basic data conversions are handled in this class
class SpacecraftLocation(object):

    def __init__(self, spacecraft_name, journey_id, data):
        self.spacecraft_name = spacecraft_name
        self.journey_id = uuid_from_string(journey_id)
        self.location = LocationUDT(data.get('location').get('x_coordinate'),
                                    data.get('location').get('y_coordinate'),
                                    data.get('location').get('z_coordinate'))
        self.location_unit = data.get('location_unit', 'km,km,km')
        self.reading_time = format_timestamp(data.get('reading_time'))

    def to_string(self):
        return 'SpacecraftLocation [spacecraft_name={sc_n}, journey_id={j_id}, location={loc}, location_unit={lu}, ' \
               'reading_time={r}]'.format(sc_n=self.spacecraft_name, j_id=self.journey_id, loc=self.location,
                                          lu=self.location_unit, r=self.reading_time)
