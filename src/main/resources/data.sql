INSERT INTO roles (authority) VALUES ('ADMIN');
INSERT INTO roles (authority) VALUES ('USER');

INSERT INTO users (username, password) VALUES ('admin', '{bcrypt}$2a$12$PuA9tpe9GL8VAYCcpvyYgOP/CF2je8n.aeevlXWdSfxDrUd4U6n8e');
INSERT INTO users (username, password) VALUES ('user', '{bcrypt}$2a$12$aIaN4uBIVKYrl1l1j0u1Gu2BqA6pXGdPTFPlINh5ty7cJCodtmHDa');

INSERT INTO user_role_junction (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role_junction (user_id, role_id) VALUES (2, 2);
