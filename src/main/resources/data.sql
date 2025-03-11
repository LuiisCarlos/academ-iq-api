INSERT INTO roles (authority) VALUES ('ADMIN');
INSERT INTO roles (authority) VALUES ('USER');

INSERT INTO files (filename, content_type, size, url, extension)
VALUES ('default-user-avatar.png', 'image/png', 2048, 'https://localhost:8888/api/v1/files/default-user-avatar.png', 'png');
