from flask import Flask
from flask_cors import CORS
from controller.credentials_controller import credentials_controller
from controller.spacecraft_journey_controller import spacecraft_journey_controller
from controller.spacecraft_instruments_controller import spacecraft_instruments_controller

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

if __name__ == '__main__':
    app.run()
