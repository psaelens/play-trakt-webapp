# --- !Ups
CREATE TABLE trakt_users (
	username varchar,
	password varchar
);

# --- !Downs
DROP TABLE IF EXISTS trakt_users;