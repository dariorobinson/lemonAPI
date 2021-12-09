drop table if exists user_playlist_role;
drop table if exists song_playlist_order;
drop table if exists users;
drop table if exists playlists;
drop table if exists songs;

drop type if exists role;
drop type if exists access;

create type access as enum ('PRIVATE', 'PUBLIC');

--unsure if I want to keep this "role" enum since we're possibly allowing it to be null
create type role as enum('CREATOR', 'EDITOR', 'VIEWER');

create table users (
	user_id varchar primary key,
	username varchar not null,
	discriminator varchar not null,
	unique (username, discriminator)
);

create table playlists (
	playlist_id varchar primary key,
	name varchar not null,
	description varchar,
	access_type access not null default 'PRIVATE'
);

create table songs (
	song_url varchar primary key,
	name varchar,
	duration time
);

create table user_playlist_role (
	user_id varchar,
	playlist_id varchar,
	role_type varchar not null,
	primary key(user_id, playlist_id)
);

create table song_playlist_order (
	song_url varchar,
	playlist_id varchar,
	song_order int not null,
	primary key(song_url, playlist_id)
);

select * from users;
select * from user_playlist_role;
select * from playlists;
