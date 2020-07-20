CREATE KEYSPACE IF NOT EXISTS killrvideo WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS killrvideo.user_credentials (
    email text,
    pass text,
    userid uuid,
    PRIMARY KEY (email)
);

CREATE TABLE IF NOT EXISTS killrvideo.users (
    userid uuid,
    firstname text,
    lastname text,
    email text,
    created_date timestamp,
    PRIMARY KEY (userid)
);

CREATE TABLE IF NOT EXISTS killrvideo.videos (
    videoid uuid,
    userid uuid,
    name text,
    description text,
    location text,
    location_type int,
    preview_image_location text,
    tags set<text>,
    added_date timestamp,
    PRIMARY KEY (videoid)
);

CREATE TABLE IF NOT EXISTS killrvideo.user_videos (
    userid uuid,
    added_date timestamp,
    videoid uuid,
    name text,
    preview_image_location text,
    PRIMARY KEY (userid, added_date, videoid)
) WITH CLUSTERING ORDER BY (added_date DESC, videoid ASC);

CREATE TABLE IF NOT EXISTS killrvideo.latest_videos (
    yyyymmdd text,
    added_date timestamp,
    videoid uuid,
    userid uuid,
    name text,
    preview_image_location text,
    PRIMARY KEY (yyyymmdd, added_date, videoid)
) WITH CLUSTERING ORDER BY (added_date DESC, videoid ASC);

CREATE TABLE IF NOT EXISTS killrvideo.video_ratings (
    videoid uuid,
    rating_counter counter,
    rating_total counter,
    PRIMARY KEY (videoid)
);

CREATE TABLE IF NOT EXISTS killrvideo.video_ratings_by_user (
    videoid uuid,
    userid uuid,
    rating int,
    PRIMARY KEY (videoid, userid)
);

CREATE TABLE IF NOT EXISTS killrvideo.video_playback_stats (
    videoid uuid,
    views counter,
    PRIMARY KEY (videoid)
);

CREATE TABLE IF NOT EXISTS killrvideo.video_recommendations (
    userid uuid,
    added_date timestamp,
    videoid uuid,
    rating float,
    authorid uuid,
    name text,
    preview_image_location text,
    PRIMARY KEY(userid, added_date, videoid)
) WITH CLUSTERING ORDER BY (added_date DESC, videoid ASC);

CREATE TABLE IF NOT EXISTS killrvideo.video_recommendations_by_video (
    videoid uuid,
    userid uuid,
    rating float,
    added_date timestamp STATIC,
    authorid uuid STATIC,
    name text STATIC,
    preview_image_location text STATIC,
    PRIMARY KEY(videoid, userid)
);

CREATE TABLE IF NOT EXISTS killrvideo.videos_by_tag (
    tag text,
    videoid uuid,
    added_date timestamp,
    userid uuid,
    name text,
    preview_image_location text,
    tagged_date timestamp,
    PRIMARY KEY (tag, videoid)
);

CREATE TABLE IF NOT EXISTS killrvideo.tags_by_letter (
    first_letter text,
    tag text,
    PRIMARY KEY (first_letter, tag)
);

CREATE TABLE IF NOT EXISTS killrvideo.comments_by_video (
    videoid uuid,
    commentid timeuuid,
    userid uuid,
    comment text,
    PRIMARY KEY (videoid, commentid)
) WITH CLUSTERING ORDER BY (commentid DESC);

CREATE TABLE IF NOT EXISTS killrvideo.comments_by_user (
    userid uuid,
    commentid timeuuid,
    videoid uuid,
    comment text,
    PRIMARY KEY (userid, commentid)
) WITH CLUSTERING ORDER BY (commentid DESC);

CREATE TABLE IF NOT EXISTS killrvideo.uploaded_video_destinations (
    upload_url text,
    assetid text,
    filename text,
    locatorid text,
    PRIMARY KEY (upload_url)
);

CREATE TABLE IF NOT EXISTS killrvideo.uploaded_video_jobs (
    videoid uuid,
    upload_url text,
    jobid text,
    PRIMARY KEY (videoid)
);

CREATE TABLE IF NOT EXISTS killrvideo.uploaded_video_jobs_by_jobid (
    jobid text,
    videoid uuid,
    upload_url text,
    PRIMARY KEY (jobid)
);

CREATE TABLE IF NOT EXISTS killrvideo.encoding_job_notifications (
    videoid uuid,
    status_date timestamp,
    etag text,
    jobid text,
    newstate text,
    oldstate text,
    PRIMARY KEY (videoid, status_date, etag)
) WITH CLUSTERING ORDER BY (status_date DESC, etag ASC);

CREATE TABLE IF NOT EXISTS killrvideo.sample_data_job_log (
    job_name text,
    scheduled_run_time timestamp,
    actual_run_time timestamp,
    PRIMARY KEY (job_name, scheduled_run_time, actual_run_time)
) WITH CLUSTERING ORDER BY (scheduled_run_time DESC, actual_run_time DESC)
  AND default_time_to_live = 1209600;

CREATE TABLE IF NOT EXISTS killrvideo.sample_data_leases (
    name text,
    owner text,
    PRIMARY KEY (name)
) WITH default_time_to_live = 180;

CREATE TABLE IF NOT EXISTS killrvideo.sample_data_users (
    userid uuid,
    PRIMARY KEY(userid)
);

CREATE TABLE IF NOT EXISTS killrvideo.sample_data_videos (
    videoid uuid,
    PRIMARY KEY (videoid)
);

CREATE TABLE IF NOT EXISTS killrvideo.sample_data_youtube_videos (
    sourceid text,
    published_at timestamp,
    youtube_video_id text,
    name text,
    description text,
    used boolean,
    PRIMARY KEY (sourceid, published_at, youtube_video_id)
) WITH CLUSTERING ORDER BY (published_at DESC, youtube_video_id ASC);