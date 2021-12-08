drop table if exists users_playlists;
drop table if exists songs_playlists;
drop table if exists users;
drop table if exists playlists;
drop table if exists songs;

drop type if exists role;
drop type if exists access;

create table users (
	user_id varchar primary key,
	username varchar unique not null,
	password varchar not null,
	email varchar unique not null
);

create type access as enum ('private', 'public');

create table playlists (
	playlist_id varchar primary key,
	name varchar not null,
	description varchar,
	access access not null default 'private'
);

create table songs (
	song_url varchar primary key,
	name varchar,
	duration time
);

create type role as enum('creator', 'editor', 'viewer', 'unaccessible');

create table users_playlists (
	user_id varchar,
	playlist_id varchar,
	role_type role default 'unaccessible',
	constraint fk_user foreign key(user_id) references users(user_id),
	constraint fk_playlist foreign key(playlist_id) references playlists(playlist_id)
);

create table songs_playlists (
	song_url varchar,
	playlist_id varchar,
	song_order int not null,
	constraint fk_song foreign key(song_url) references songs(song_url),
	constraint fk_playlist foreign key(playlist_id) references playlists(playlist_id)
);
