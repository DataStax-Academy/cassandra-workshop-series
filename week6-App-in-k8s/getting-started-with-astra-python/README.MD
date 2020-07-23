# Getting Started with Apache Cassandra™ and Python using DataStax Astra

This sample Python backend provides a REST API service that is used with the [Getting Started with Astra UI](https://github.com/DataStax-Examples/getting-started-with-astra-ui) to show a
simple example of how to connect to and query DataStax Astra databases.

Contributor(s):
* [Chris Splinter](https://github.com/csplinter)
* [Madhavan Sridharan](https://github.com/msmygit)

## Objectives
- How to connect to DataStax Astra using the secure connect bundle
- How to share a DataStax Driver Session throughout a Python application
- How to expose a basic REST API using the DataStax Driver

## Project Layout
- [getting_started_with_astra.py](getting_started_with_astra.py) - entrypoint for the backend, registers controller blueprints with Flask app
- [schema.cql](schema.cql) - database schema used by the application
- [service](service/) - acts as the middle-man to take requests from the controllers and calls the corresponding dao methods.
Note how this and [session_manager.py](dao/session_manager.py) are used to share a single DataStax Driver Session across API requests, this is a best practice.
- [model](model/) - defines the Python objects that correspond to the database tables
- [dao](dao/) - methods for accessing the database, contains the DataStax Driver API calls
- [controller](controller/) - defines the API endpoints using Flask decorators

## How this works
This project is built in Python and uses Flask to expose a REST API backend for use with the [Getting Started with Astra UI](https://github.com/DataStax-Examples/getting-started-with-astra-ui).

This application is the middle man that receives requests from the UI web page and serves data from the underlying DataStax Astra database.

## Setup & Running

### Setup
If you are familiar with Python, then you've likely gotten your hands on Python virtual environments.
We'll be leveraging pyenv while setting up this backend, which will serve our
Spacecraft frontend that will have you flying through the stars.

If you aren't familiar with Python, hop over to our [official documentation](https://helpdocs.datastax.com/aws/dscloud/astra/dscloudPythonDriver.html#Installingpyenv,Python,andvirtualenv)
for setting that up on your machine, and come back here after you have it installed ( specifically after Step 5 of the Procedure ).

Now that we have that out of the way, we'll use pyenv to install Python 3.6.9
```
pyenv install 3.6.9
```

Next create a new virtualenv using that Python version we just installed.

```
pyenv virtualenv 3.6.9 astra-venv
```

Almost off to the races, go ahead and activate that virtualenv

```
pyenv activate astra-venv
```

Woot, now 3 quick dependencies ( Flask, Flask CORS,  and the DataStax Cassandra Driver )

```
pip install Flask flask-cors cassandra-driver
```

Last one, clone this repo
```
git clone https://github.com/DataStax-Examples
/getting-started-with-astra-python.git
```

### Running

If everything above went smoothly, fingers crossed, then we are ready to rock.
Go to the directory that you just cloned this repo into
```
cd getting-started-with-astra-python
```

Fire up the engines
```
FLASK_ENV=development FLASK_APP=getting_started_with_astra.py flask run
```

You should be met with the following output, note that it's running on `localhost` and port `5000`
```
 * Serving Flask app "getting_started_with_astra.py" (lazy loading)
 * Environment: development
 * Debug mode: on
 * Running on http://127.0.0.1:5000/ (Press CTRL+C to quit)
 * Restarting with stat
 * Debugger is active!
 * Debugger PIN: 204-527-831
```

Once the backend is running, you can start the [Getting Started with Astra UI](https://github.com/DataStax-Examples/getting-started-with-astra-ui) in order to use a web page that leverages this backend.
