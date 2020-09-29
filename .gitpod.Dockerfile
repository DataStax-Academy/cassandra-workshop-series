FROM gitpod/workspace-full

RUN pip install cassandra-driver && npm install -g cassandra-driver
