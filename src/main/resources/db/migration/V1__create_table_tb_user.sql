CREATE TABLE tb_user (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(250) NOT NULL,
	last_name VARCHAR(250) NOT NULL,
	email VARCHAR(250) NOT NULL,
	password VARCHAR(250) NOT NULL,
	role VARCHAR(250)
);

INSERT INTO tb_user (
    first_name,
    last_name,
    email,
    password,
    role
) VALUES (
    'Admin',
    'Admin',
    'admin@zukk.com.br',
    '$2a$10$3sdN/7.Nsp/hGVwovlx3XOxgaRfEBluQzLXGQuGm/8pO7zHQNLe.G',
    'ADMIN'
);
