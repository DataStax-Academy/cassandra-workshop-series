CREATE TABLE IF NOT EXISTS spacecraft_journey_catalog (
    spacecraft_name text,
    journey_id timeuuid,
    start timestamp,
    end timestamp,
    active boolean,
    summary text,
    PRIMARY KEY ((spacecraft_name), journey_id)
) WITH CLUSTERING ORDER BY (journey_id desc);

CREATE TABLE IF NOT EXISTS spacecraft_speed_over_time (
    spacecraft_name text,
    journey_id timeuuid,
    speed double,
    reading_time timestamp,
    speed_unit text,
    PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
) WITH CLUSTERING ORDER BY (reading_time DESC);

CREATE TABLE IF NOT EXISTS spacecraft_temperature_over_time (
    spacecraft_name text,
    journey_id timeuuid,
    temperature double,
    temperature_unit text,
    reading_time timestamp,
    PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
) WITH CLUSTERING ORDER BY (reading_time DESC);

CREATE TABLE IF NOT EXISTS spacecraft_pressure_over_time (
    spacecraft_name text,
    journey_id timeuuid,
    pressure double,
    pressure_unit text,
    reading_time timestamp,
    PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
) WITH CLUSTERING ORDER BY (reading_time DESC);

CREATE TYPE IF NOT EXISTS location_udt (
    x_coordinate double,
    y_coordinate double,
    z_coordinate double);

CREATE TABLE IF NOT EXISTS spacecraft_location_over_time (
    spacecraft_name text,
    journey_id timeuuid,
    location frozen<location_udt>,
    location_unit text,
    reading_time timestamp,
    PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
) WITH CLUSTERING ORDER BY (reading_time DESC);