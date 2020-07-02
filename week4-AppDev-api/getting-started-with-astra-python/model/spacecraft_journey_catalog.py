from util.data_type_util import uuid_from_string, format_timestamp


# Python object for the spacecraft_journey_catalog database table
# see the schema.cql file in this project's root directory for table details
# Basic data conversions are handled in this class
class SpacecraftJourneyCatalog(object):

    def __init__(self, spacecraft_name, journey_id, start, end, active, summary):
        self.spacecraft_name = spacecraft_name
        self.journey_id = uuid_from_string(journey_id)
        self.start = format_timestamp(start)
        self.end = format_timestamp(end)
        self.active = active
        self.summary = summary

    def to_string(self):
        return 'SpacecraftJourneyCatalog [spacecraft_name={sc_n}, journey_id={j_id}, start={s}, end={e}, ' \
               'active={a}, summary={sum}]'.format(sc_n=self.spacecraft_name, j_id=self.journey_id, s=self.start,
                                                   e=self.end, a=self.active, sum=self.summary)
