from util.data_type_util import uuid_from_string, format_timestamp


# Python object for the spacecraft_pressure_over_time database table
# see the schema.cql file in this project's root directory for table details
# Basic data conversions are handled in this class
class SpacecraftPressure(object):

    def __init__(self, spacecraft_name, journey_id, data):
        self.spacecraft_name = spacecraft_name
        self.journey_id = uuid_from_string(journey_id)
        self.pressure = float(data.get('pressure'))
        self.pressure_unit = data.get('pressure_unit', 'kPa')
        self.reading_time = format_timestamp(data.get('reading_time'))

    def to_string(self):
        return 'SpacecraftPressure [spacecraft_name={sc_n}, journey_id={j_id}, pressure={p}, pressure_unit={pu}, ' \
               'reading_time={r}]'.format(sc_n=self.spacecraft_name, j_id=self.journey_id, p=self.pressure,
                                          pu=self.pressure_unit, r=self.reading_time)
