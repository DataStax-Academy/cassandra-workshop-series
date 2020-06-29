from dao.session_manager import SessionManager
from dao.spacecraft_journey_catalog_dao import SpacecraftJourneyCatalogDAO
from dao.spacecraft_location_dao import SpacecraftLocationDAO
from dao.spacecraft_pressure_dao import SpacecraftPressureDAO
from dao.spacecraft_speed_dao import SpacecraftSpeedDAO
from dao.spacecraft_temperature_dao import SpacecraftTemperatureDAO


class AstraService(object):

    spacecraft_journey_catalog_dao = None
    spacecraft_location_dao = None
    spacecraft_pressure_dao = None
    spacecraft_speed_dao = None
    spacecraft_temperature_dao = None
    _session_manager = SessionManager()
    _session = None

    def get_session(self):
        if self._session is None:
            self._session = self._session_manager.get_instance().connect()

        return self._session

    def save_credentials(self, username, password, keyspace, secure_connect_bundle_path):
        self._session_manager.save_credentials(username, password, keyspace, secure_connect_bundle_path)

    def test_credentials(self, username, password, keyspace, secure_connection_bundle_path):
        return self._session_manager.test_credentials(username, password, keyspace, secure_connection_bundle_path)

    def connect(self):
        return self._session_manager.connect()

    def check_connection(self):
        return self._session_manager.check_connection()

    def get_spacecraft_journey_catalog_dao(self):
        if self.spacecraft_journey_catalog_dao is None:
            self.spacecraft_journey_catalog_dao = SpacecraftJourneyCatalogDAO(self.get_session())

        return self.spacecraft_journey_catalog_dao

    def get_spacecraft_location_dao(self):
        if self.spacecraft_location_dao is None:
            self.spacecraft_location_dao = SpacecraftLocationDAO(self.get_session())

        return self.spacecraft_location_dao

    def get_spacecraft_pressure_dao(self):
        if self.spacecraft_pressure_dao is None:
            self.spacecraft_pressure_dao = SpacecraftPressureDAO(self.get_session())

        return self.spacecraft_pressure_dao

    def get_spacecraft_speed_dao(self):
        if self.spacecraft_speed_dao is None:
            self.spacecraft_speed_dao = SpacecraftSpeedDAO(self.get_session())

        return self.spacecraft_speed_dao

    def get_spacecraft_temperature_dao(self):
        if self.spacecraft_temperature_dao is None:
            self.spacecraft_temperature_dao = SpacecraftTemperatureDAO(self.get_session())

        return self.spacecraft_temperature_dao

    def create_new_journey_for_spacecraft(self, spacecraft_name, journey_id, start, end, active, summary):
        return self.get_spacecraft_journey_catalog_dao().write_journey(spacecraft_name, journey_id,
                                                                       start, end, active, summary)

    def get_all_spacecraft_journeys(self):
        return self.get_spacecraft_journey_catalog_dao().get_all_journeys()

    def get_all_journeys_for_spacecraft(self, spacecraft_name):
        return self.get_spacecraft_journey_catalog_dao().get_all_journeys_for_spacecraft(spacecraft_name)

    def get_single_journey_for_spacecraft(self, spacecraft_name, journey_id):
        return self.get_spacecraft_journey_catalog_dao().get_single_journey_for_spacecraft(spacecraft_name, journey_id)

    def get_location_readings_for_spacecraft_journey(self, spacecraft_name, journey_id, page_size, page_state):
        return self.get_spacecraft_location_dao().get_location_readings_for_journey(spacecraft_name, journey_id,
                                                                                    page_size, page_state)

    def save_location_reading_for_spacecraft_journey(self, spacecraft_name, journey_id, data):
        self.get_spacecraft_location_dao().write_readings(spacecraft_name, journey_id, data)

    def get_pressure_readings_for_spacecraft_journey(self, spacecraft_name, journey_id, page_size, page_state):
        return self.get_spacecraft_pressure_dao().get_pressure_readings_for_journey(spacecraft_name, journey_id,
                                                                                    page_size, page_state)

    def save_pressure_reading_for_spacecraft_journey(self, spacecraft_name, journey_id, data):
        self.get_spacecraft_pressure_dao().write_readings(spacecraft_name, journey_id, data)

    def get_speed_readings_for_spacecraft_journey(self, spacecraft_name, journey_id, page_size, page_state):
        return self.get_spacecraft_speed_dao().get_speed_readings_for_journey(spacecraft_name, journey_id,
                                                                              page_size, page_state)

    def save_speed_reading_for_spacecraft_journey(self, spacecraft_name, journey_id, data):
        self.get_spacecraft_speed_dao().write_readings(spacecraft_name, journey_id, data)

    def get_temperature_readings_for_spacecraft_journey(self, spacecraft_name, journey_id, page_size, page_state):
        return self.get_spacecraft_temperature_dao().get_temperature_readings_for_journey(spacecraft_name, journey_id,
                                                                                          page_size, page_state)

    def save_temperature_reading_for_spacecraft_journey(self, spacecraft_name, journey_id, data):
        self.get_spacecraft_temperature_dao().write_readings(spacecraft_name, journey_id, data)


astra_service = AstraService()
