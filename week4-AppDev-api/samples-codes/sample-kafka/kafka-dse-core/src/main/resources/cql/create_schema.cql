create keyspace demo_sdc WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

use demo_sdc;

CREATE TABLE stocks_ticks ( 
	symbol text,
	valueDate timestamp,
	value double,
	PRIMARY KEY (symbol, valueDate)
) WITH CLUSTERING ORDER BY (valueDate DESC);

