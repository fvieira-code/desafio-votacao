CREATE TABLE tb_cliente (
    id_cliente INTEGER AUTO_INCREMENT NOT NULL,
    razao_social_cliente VARCHAR(250) NOT NULL,
    nome_fantasia_cliente VARCHAR(250) NOT NULL,
    cnpj_cliente VARCHAR(15) NOT NULL,
    endereco_cliente VARCHAR(500),
    CONSTRAINT tb_cliente_pk PRIMARY KEY (id_cliente)
);
