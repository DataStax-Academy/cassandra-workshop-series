from util.cql_file_util import get_cql_schema_string_from_file
from util.data_type_util import uuid_from_string
from model.spacecraft_journey_catalog import SpacecraftJourneyCatalog


# Data Access Object for the spacecraft_journey_catalog database table
# Contains CQL Statements and DataStax Driver APIs for reading and writing from the database
class SpacecraftJourneyCatalogDAO(object):

    table_name = "spacecraft_journey_catalog"

    create_stmt = get_cql_schema_string_from_file(table_name)

    insert_stmt = 'INSERT INTO {table_name} (spacecraft_name, journey_id, start, end, active, summary) ' \
                  'VALUES (:spacecraft_name,:journey_id,:start,:end,:active,:summary);'.format(table_name=table_name)

    select_all_journeys_stmt = 'SELECT * FROM {table_name};'.format(table_name=table_name)

    select_all_journeys_for_spacecraft_stmt = 'SELECT * FROM {table_name} WHERE spacecraft_name = :spacecraft_name;' \
                                              ''.format(table_name=table_name)

    select_single_journey_for_spacecraft_stmt = 'SELECT * FROM {table_name} ' \
                                                'WHERE spacecraft_name = :spacecraft_name AND journey_id = :journey_id;' \
                                                ''.format(table_name=table_name)

    def __init__(self, _session):
        self._session = _session
        self.maybe_create_schema()
        self.insert_prep_stmt = _session.prepare(self.insert_stmt)
        self.select_all_prep_stmt = _session.prepare(self.select_all_journeys_stmt)
        self.select_all_for_spacecraft_prep_stmt = _session.prepare(self.select_all_journeys_for_spacecraft_stmt)
        self.select_single_for_spacecraft_prep_stmt = _session.prepare(self.select_single_journey_for_spacecraft_stmt)

    def maybe_create_schema(self):
        self._session.execute(self.create_stmt)

    def write_journey(self, spacecraft_name, journey_id, start, end, active, summary):
        # We use the DataStax Driver's Async API here to write the rows to the database in a non-blocking fashion

        def handle_success(results):
            pass

        def handle_error(exception):
            raise Exception('Failed to write row: ' + exception)

        this_journey = SpacecraftJourneyCatalog(spacecraft_name, journey_id, start, end, active, summary)

        insert_future = self._session.execute_async(self.insert_prep_stmt.bind({
            'spacecraft_name': this_journey.spacecraft_name,
            'journey_id': this_journey.journey_id,
            'start': this_journey.start,
            'end': this_journey.end,
            'active': this_journey.active,
            'summary': this_journey.summary}
        ))

        insert_future.add_callbacks(handle_success, handle_error)

    def get_all_journeys(self):
        result = self._session.execute(self.select_all_prep_stmt)
        return result

    def get_all_journeys_for_spacecraft(self, spacecraft_name):
        result = self._session.execute(self.select_all_for_spacecraft_prep_stmt.bind({
            'spacecraft_name': spacecraft_name}
        ))
        return result

    def get_single_journey_for_spacecraft(self, spacecraft_name, journey_id):
        result = self._session.execute(self.select_single_for_spacecraft_prep_stmt.bind({
            'spacecraft_name': spacecraft_name,
            'journey_id': uuid_from_string(journey_id)}
        ))

        return result
