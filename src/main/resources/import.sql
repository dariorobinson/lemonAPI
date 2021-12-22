INSERT INTO users (user_id, username, discriminator) VALUES ('123', 'CreatorUser', '3214');
INSERT INTO users (user_id, username, discriminator) VALUES ('1234', 'EditorUser', '32141');
INSERT INTO users (user_id, username, discriminator) VALUES ('12345', 'ViewerUser', '321412');
INSERT INTO playlists(playlist_id, access, description, name) VALUES ('5b5d925d-e30e-48aa-a72c-61eeca661b6f', 'PUBLIC', 'Public playlist', 'playlist1');
INSERT INTO user_playlist(playlist_id, user_id, user_role) VALUES ('5b5d925d-e30e-48aa-a72c-61eeca661b6f', '123', 'CREATOR');
INSERT INTO user_playlist(playlist_id, user_id, user_role) VALUES ('5b5d925d-e30e-48aa-a72c-61eeca661b6f', '1234', 'EDITOR');

INSERT INTO songs(song_url, name) VALUES ('youtube.com', 'youtube.com website');
INSERT INTO songs(song_url, name, duration) VALUES ('test.com', 'test.com website', 10.04)
INSERT INTO songs(song_url, name, duration) VALUES ('test', 'test website', 1000000)
INSERT INTO song_playlist(song_url, playlist_id, song_order) VALUES ('youtube.com', '5b5d925d-e30e-48aa-a72c-61eeca661b6f', 1)