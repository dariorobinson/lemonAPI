INSERT INTO users (user_id, username, discriminator) VALUES ('e05e00f9-bf63-4947-b504-02859c33158e', 'tester', '#3214');

INSERT INTO playlists (playlist_id, name, description) VALUES ('3b98fe20-1198-41e3-9469-16295006040b', 'playlist1', 'This is a description');

INSERT INTO user_playlist_role (user_id, playlist_id, user_role) VALUES('e05e00f9-bf63-4947-b504-02859c33158e', '3b98fe20-1198-41e3-9469-16295006040b', 'CREATOR');