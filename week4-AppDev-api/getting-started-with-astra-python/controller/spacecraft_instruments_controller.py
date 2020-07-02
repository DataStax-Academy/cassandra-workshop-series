import codecs
from flask import Blueprint, request
from flask_cors import CORS

from service.astra_service import astra_service

spacecraft_instruments_controller = Blueprint('spacecraft_instruments_controller', __name__)

CORS(spacecraft_instruments_controller)


# This controller handles the all of the GET and POST REST API calls for the instrument tables, specifically for
#     spacecraft_location_over_time
#     spacecraft_pressure_over_time
#     spacecraft_speed_over_time
#     spacecraft_temperature_over_time
#
# Here we define the REST API endpoints and call our Astra Service
# to send the request to the underlying Data Access Objects
@spacecraft_instruments_controller.route('/api/spacecraft/<spacecraft_name>/<journey_id>/instruments/location',
                                         methods=['GET', 'POST'])
def location_reading_for_spacecraft_journey(spacecraft_name, journey_id):
    if request.method == 'POST':
        astra_service.save_location_reading_for_spacecraft_journey(spacecraft_name, journey_id, request.get_json())
        return {'success': True}, 200

    if request.method == 'GET':
        result = astra_service.get_location_readings_for_spacecraft_journey(spacecraft_name, journey_id,
                                                                             request.args.get('pagesize', 25),
                                                                             request.args.get('pagestate', None))

        resp = {'pageSize': request.args.get('pagesize', 25),
                'pageState': codecs.encode(result.paging_state, 'hex').decode('UTF-8') if result.paging_state else None,
                'data': result.current_rows}

        return resp, 200


@spacecraft_instruments_controller.route('/api/spacecraft/<spacecraft_name>/<journey_id>/instruments/pressure',
                                         methods=['GET', 'POST'])
def pressure_reading_for_spacecraft_journey(spacecraft_name, journey_id):
    if request.method == 'POST':
        astra_service.save_pressure_reading_for_spacecraft_journey(spacecraft_name, journey_id, request.get_json())
        return {'success': True}, 200

    if request.method == 'GET':
        result = astra_service.get_pressure_readings_for_spacecraft_journey(spacecraft_name, journey_id,
                                                                             request.args.get('pagesize', 25),
                                                                             request.args.get('pagestate', None))

        resp = {'pageSize': request.args.get('pagesize', 25),
                'pageState': codecs.encode(result.paging_state, 'hex').decode('UTF-8') if result.paging_state else None,
                'data': result.current_rows}

        return resp, 200


@spacecraft_instruments_controller.route('/api/spacecraft/<spacecraft_name>/<journey_id>/instruments/speed',
                                         methods=['GET', 'POST'])
def speed_reading_for_spacecraft_journey(spacecraft_name, journey_id):
    if request.method == 'POST':
        astra_service.save_speed_reading_for_spacecraft_journey(spacecraft_name, journey_id, request.get_json())
        return {'success': True}, 200

    if request.method == 'GET':
        result = astra_service.get_speed_readings_for_spacecraft_journey(spacecraft_name, journey_id,
                                                                          request.args.get('pagesize', 25),
                                                                          request.args.get('pagestate', None))

        resp = {'pageSize': request.args.get('pagesize', 25),
                'pageState': codecs.encode(result.paging_state, 'hex').decode('UTF-8') if result.paging_state else None,
                'data': result.current_rows}

        return resp, 200


@spacecraft_instruments_controller.route('/api/spacecraft/<spacecraft_name>/<journey_id>/instruments/temperature',
                                         methods=['GET', 'POST'])
def temperature_reading_for_spacecraft_journey(spacecraft_name, journey_id):
    if request.method == 'POST':
        astra_service.save_temperature_reading_for_spacecraft_journey(spacecraft_name, journey_id, request.get_json())
        return {'success': True}, 200

    if request.method == 'GET':
        result = astra_service.get_temperature_readings_for_spacecraft_journey(spacecraft_name, journey_id,
                                                                                request.args.get('pagesize', 25),
                                                                                request.args.get('pagestate', None))

        resp = {'pageSize': request.args.get('pagesize', 25),
                'pageState': codecs.encode(result.paging_state, 'hex').decode('UTF-8') if result.paging_state else None,
                'data': result.current_rows}

        return resp, 200




