import os
from service.astra_service import astra_service
from dao.session_manager import SessionManager
import logging
from flask import Flask
from flask_cors import CORS
from controller.credentials_controller import credentials_controller
from controller.spacecraft_journey_controller import spacecraft_journey_controller
from controller.spacecraft_instruments_controller import spacecraft_instruments_controller


logging.basicConfig(filename='myapp.log', level=logging.INFO)
app = Flask(__name__)

# Register blueprints from controllers
# see Flask docs for details https://flask.palletsprojects.com/en/1.1.x/blueprints/
app.register_blueprint(credentials_controller)
app.register_blueprint(spacecraft_journey_controller)
app.register_blueprint(spacecraft_instruments_controller)

# Must enable CORS as UI requests will be coming from different port
# flask-cors is a separate plugin for Flask
# see Flask CORS docs for details https://flask-cors.readthedocs.io/en/latest/
CORS(app)

@app.route("/")
def hello():
    if os.getenv('USE_ASTRA') == 'false':
        SessionManager.initialized = True
        astra_service.connect()
        return "Hi, connected to the local database"
    return "Hi, I am the Python backend API. Please connect me to Astra via UI."

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=9090, debug=True)
