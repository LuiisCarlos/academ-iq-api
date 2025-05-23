INSERT INTO roles (authority) VALUES ('USER');
INSERT INTO roles (authority) VALUES ('ADMIN');
INSERT INTO roles (authority) VALUES ('ENDPOINT_ADMIN');
INSERT INTO roles (authority) VALUES ('ACADEMIQ_ADMIN');

INSERT INTO files (is_default_file, is_image, filename, content_type, size, url, extension)
    VALUES (true, true, 'default-user-avatar.png', 'image/png', 4096, 'http://localhost:8888/api/v1/files/default-user-avatar.png', 'png');
INSERT INTO files (is_default_file, is_image, filename, content_type, size, url, extension)
    VALUES (true, true, 'default-course-thumbnail.jpg', 'image/jpeg', 8192, 'http://localhost:8888/api/v1/files/default-course-thumbnail.jpg', 'jpg');
INSERT INTO files (is_default_file, is_image, filename, content_type, size, url, extension)
    VALUES (true, true, 'academiq-logo.png', 'image/png', 175625, 'http://localhost:8888/api/v1/files/academiq-logo.png', 'png');
INSERT INTO files (is_default_file, is_image, filename, content_type, size, url, extension)
    VALUES (false, true, 'video-example.mp4', 'video/mp4', 49880892, 'http://localhost:8888/api/v1/files/video-example.mp4', 'mp4');
INSERT INTO files (is_default_file, is_image, filename, content_type, size, url, extension)
    VALUES (false, true, '1742398160643_typescript_fundamentals.png', 'image/png', 190548, 'http://localhost:8888/api/v1/files/1742398160643_typescript_fundamentals.png', 'png');
INSERT INTO files (is_default_file, is_image, filename, content_type, size, url, extension)
    VALUES (false, true, '1742407711123_java_desde_cero.jpg', 'image/jpeg', 45140, 'http://localhost:8888/api/v1/files/1742407711123_java_desde_cero.jpg', 'jpg');

INSERT INTO courses (name, description, author, file_id, category, level, created_at, duration, rating)
    VALUES ('Fundamentos en Typescript', 'Aquí se verán los fundamentos en typescript', 'Luis Carlos', 5, 'Typescript', 'BEGINNER', '2025-03-19 16:21:000000', '00:00:00', 0.0);
INSERT INTO courses (name, description, author, file_id, category, level, created_at, duration, rating)
    VALUES ('Java desde cero', 'En este curso te enseñare a aprender java desde cero', 'Luis Carlos', 6, 'Java', 'BEGINNER', '2025-03-19 16:21:000000', '00:00:00', 0.0);

INSERT INTO sections (course_id, name)
    VALUES (1, 'Introduccion a Typescript');
INSERT INTO sections (course_id, name)
    VALUES (1, 'Ampliando conceptos');
INSERT INTO sections (course_id, name)
    VALUES (1, 'Proyecto final');

INSERT INTO section_file_junction (section_id, file_id)
    VALUES (1, 4);
INSERT INTO section_file_junction (section_id, file_id)
    VALUES (1, 4);
INSERT INTO section_file_junction (section_id, file_id)
    VALUES (2, 4);
INSERT INTO section_file_junction (section_id, file_id)
    VALUES (2, 4);
INSERT INTO section_file_junction (section_id, file_id)
    VALUES (2, 4);
INSERT INTO section_file_junction (section_id, file_id)
    VALUES (3, 4);
INSERT INTO section_file_junction (section_id, file_id)
    VALUES (3, 4);