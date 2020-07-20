CREATE KEYSPACE IF NOT EXISTS killrvideo 
WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

use killrvideo;

// Users keyed by id
CREATE TABLE IF NOT EXISTS users (
    userid     uuid,
    firstname  text,
    lastname   text,
    email      text,
    created_date timestamp,
    PRIMARY KEY (userid)
);

// Videos by id
CREATE TABLE IF NOT EXISTS videos (
    videoid                uuid,
    userid                 uuid,
    name                   text,
    description            text,
    location               text,
    location_type          int,
    preview_image_location text,
    tags                   set<text>,
    added_date             timestamp,
    PRIMARY KEY (videoid)
);

// Comments for a given video
CREATE TABLE IF NOT EXISTS comments_by_video (
    videoid uuid,
    commentid timeuuid,
    userid uuid,
    comment text,
    PRIMARY KEY (videoid, commentid)
) WITH CLUSTERING ORDER BY (commentid DESC);

// Comments for a given user
CREATE TABLE IF NOT EXISTS comments_by_user (
    userid uuid,
    commentid timeuuid,
    videoid uuid,
    comment text,
    PRIMARY KEY (userid, commentid)
) WITH CLUSTERING ORDER BY (commentid DESC);

