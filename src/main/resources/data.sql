INSERT INTO roles (authority) VALUES ('USER');
INSERT INTO roles (authority) VALUES ('ADMIN');
INSERT INTO roles (authority) VALUES ('ENDPOINT_ADMIN');
INSERT INTO roles (authority) VALUES ('ACADEMIQ_ADMIN');

INSERT INTO files (is_image, filename, content_type, size, url, extension)
    VALUES (true, 'default-user-avatar.png', 'image/png', 4096, 'http://localhost:8888/api/v1/files/default-user-avatar.png', 'png');
INSERT INTO files (is_image, filename, content_type, size, url, extension)
    VALUES (true, 'academiq-logo.png', 'image/png', 175625, 'http://localhost:8888/api/v1/files/academiq-logo.png', 'png');
INSERT INTO files (is_image, filename, content_type, size, url, extension)
    VALUES (true, 'default-video-thumbnail.jpg', 'image/jpg', 8192, 'http://localhost:8888/api/v1/files/default-video-thumbnail.jpg', 'jpg');
INSERT INTO files (is_image, filename, content_type, size, url, extension)
    VALUES (false, 'video-example.mp4', 'image/mp4', 49880892, 'http://localhost:8888/api/v1/files/video-example.mp4', 'mp4');
