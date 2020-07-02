from util.data_type_util import uuid_from_string, format_timestamp


# Python object for the spacecraft_speed_over_time database table
# see the schema.cql file in this project's root directory for table details
# Basic data conversions are handled in this class
class SpacecraftSpeed(object):

    def __init__(self, spacecraft_name, journey_id, data):
        self.spacecraft_name = spacecraft_name
        self.journey_id = uuid_from_string(journey_id)
        self.speed = float(data.get('speed'))
        self.speed_unit = data.get('speed_unit', 'km/h')
        self.reading_time = format_timestamp(data.get('reading_time'))

    def to_string(self):
        return 'SpacecraftSpeed [spacecraft_name={sc_n}, journey_id={j_id}, speed={s}, speed_unit={su}, ' \
               'reading_time={r}]'.format(sc_n=self.spacecraft_name, j_id=self.journey_id, s=self.speed,
                                          su=self.speed_unit, r=self.reading_time)
