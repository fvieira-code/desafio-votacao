CREATE TABLE tb_consultor (
	id_consultor INTEGER AUTO_INCREMENT NOT NULL,
	nome_consultor VARCHAR(250) NOT NULL,
	cpf_consultor VARCHAR(15) NOT NULL,
	rg_consultor VARCHAR(15) NOT NULL,
	endereco_consultor VARCHAR(500),
	CONSTRAINT tb_consultor_pk PRIMARY KEY (id_consultor)
);