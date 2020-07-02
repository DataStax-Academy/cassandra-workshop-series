from flask import Blueprint, request, jsonify
from flask_cors import CORS
from cassandra.util import min_uuid_from_time
from datetime import datetime, timedelta
from service.astra_service import astra_service

spacecraft_journey_controller = Blueprint('spacecraft_journey_controller', __name__)

CORS(spacecraft_journey_controller)


# This controller handles the all of the GET and POST REST API calls for the spacecraft_journey_catalog table
#
# Here we define the REST API endpoints and call our Astra Service
# to send the request to the underlying Data Access Objects
@spacecraft_journey_controller.route('/api/spacecraft')
def get_all_journeys():
    result = astra_service.get_all_spacecraft_journeys()
    return jsonify(result.current_rows), 200


@spacecraft_journey_controller.route('/api/spacecraft/<spacecraft_name>', methods=['GET', 'POST'])
def journeys_for_spacecraft(spacecraft_name):
    if request.method == 'POST':
        now = datetime.utcnow()
        journey_id = min_uuid_from_time(now)
        start = now
        end = now + timedelta(seconds=1000)
        active = True
        summary = request.get_data(as_text=True)

        astra_service.create_new_journey_for_spacecraft(spacecraft_name, journey_id, start, end, active, summary)

        return str(journey_id), 200

    if request.method == 'GET':
        result = astra_service.get_all_journeys_for_spacecraft(spacecraft_name)

        return jsonify(result.current_rows), 200




