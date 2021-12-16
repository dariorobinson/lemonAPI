INSERT INTO users (user_id, username, discriminator) VALUES ('123', 'tester', '#3214');
INSERT INTO playlists(playlist_id, access, description, name) VALUES ('5b5d925d-e30e-48aa-a72c-61eeca661b6f', 'PUBLIC', 'Public playlist', 'playlist1');
INSERT INTO user_playlist_role(playlist_id, user_id, user_role) VALUES ('5b5d925d-e30e-48aa-a72c-61eeca661b6f', '123', 'CREATOR');
INSERT INTO songs(song_url) VALUES ('youtube.com');
INSERT INTO songs(song_url) VALUES ('test.com')