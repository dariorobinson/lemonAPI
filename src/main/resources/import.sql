INSERT INTO users (user_id, username, discriminator) VALUES ('123', 'CreatorUser', '3214');
INSERT INTO users (user_id, username, discriminator) VALUES ('1234', 'EditorUser', '32141');
INSERT INTO users (user_id, username, discriminator) VALUES ('12345', 'ViewerUser', '321412');
INSERT INTO users (user_id, username, discriminator) VALUES ('123456', 'TestUser', '3214123');
INSERT INTO users (user_id, username, discriminator) VALUES ('1234567', 'TestUserAgain', '32141234');

INSERT INTO playlists(playlist_id, access, description, name) VALUES ('5b5d925d-e30e-48aa-a72c-61eeca661b6f', 'PUBLIC', 'Public playlist', 'playlist1');
INSERT INTO playlists(playlist_id, access, description, name) VALUES ('092e7820-39fc-4619-bd21-e821758d2f64', 'PUBLIC', 'Public playlist1', 'playlist12');
INSERT INTO user_playlist(playlist_id, user_id, user_role) VALUES ('5b5d925d-e30e-48aa-a72c-61eeca661b6f', '123', 'CREATOR');
INSERT INTO user_playlist(playlist_id, user_id, user_role) VALUES ('5b5d925d-e30e-48aa-a72c-61eeca661b6f', '1234', 'EDITOR');
INSERT INTO user_playlist(playlist_id, user_id, user_role) VALUES ('5b5d925d-e30e-48aa-a72c-61eeca661b6f', '123456', 'EDITOR');
INSERT INTO user_playlist(playlist_id, user_id, user_role) VALUES ('092e7820-39fc-4619-bd21-e821758d2f64', '1234567', 'CREATOR');
INSERT INTO user_playlist(playlist_id, user_id, user_role) VALUES ('092e7820-39fc-4619-bd21-e821758d2f64', '123', 'CREATOR');

INSERT INTO songs(song_url, name) VALUES ('youtube.com', 'youtube.com website');
INSERT INTO songs(song_url, name, duration) VALUES ('test.com', 'test.com website', 10.04)
INSERT INTO songs(song_url, name, duration) VALUES ('test', 'test website', 1000000)
INSERT INTO song_playlist(song_url, playlist_id, song_order) VALUES ('youtube.com', '5b5d925d-e30e-48aa-a72c-61eeca661b6f', 1)
INSERT INTO song_playlist(song_url, playlist_id, song_order) VALUES ('test.com', '092e7820-39fc-4619-bd21-e821758d2f64', 1)