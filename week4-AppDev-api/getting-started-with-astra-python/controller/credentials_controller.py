import os
from flask import Blueprint, request
from flask_cors import CORS

from service.astra_service import astra_service

credentials_controller = Blueprint('credentials_controller', __name__)

CORS(credentials_controller)


# This controller handles the functionality for connecting to the database
#
# Here we define the REST API endpoints and call our Astra Service
# to send the request to the underlying Data Access Objects
@credentials_controller.route("/api/credentials", methods=['GET', 'POST'])
def connect():
    if request.method == 'POST':
        temp_zip_path = os.path.abspath('temp_bundle.zip')
        temp_zip = open(temp_zip_path, 'wb')
        with temp_zip as f:
            f.write(request.get_data())

        try:
            astra_service.save_credentials(request.args['username'], request.args['password'],
                                            request.args['keyspace'], temp_zip_path)
            astra_service.connect()
        finally:
            os.remove(temp_zip_path)

        return {'connected': True}, 200

    if request.method == 'GET':

        resp = astra_service.check_connection()

        if resp is True:
            status_code = 200
        else:
            status_code = 401

        return str(resp), status_code


@credentials_controller.route("/api/credentials/test", methods=['POST'])
def test_credentials():

    temp_zip_path = os.path.abspath('temp_bundle.zip')
    temp_zip = open(temp_zip_path, 'wb')
    with temp_zip as f:
        f.write(request.get_data())

    resp = {'success': False}
    status_code = 400

    try:
        test_connection = astra_service.test_credentials(request.args['username'], request.args['password'],
                                                          request.args['keyspace'], temp_zip_path)
        resp = {'success': test_connection}
        if resp['success'] is True:
            status_code = 200
        else:
            status_code = 401
    finally:
        os.remove(temp_zip_path)
        return resp, status_code





