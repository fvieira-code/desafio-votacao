CREATE TABLE tb_ponto (
	id_ponto INTEGER AUTO_INCREMENT NOT NULL,
	atividade VARCHAR(1000) NOT NULL,
	data_ponto DATE NOT NULL,
	dia_ponto VARCHAR(25) NOT NULL,
	inicio_ponto TIME,
	final_ponto TIME,
	total_hora_ponto TIME,
	status__ponto VARCHAR(25) NOT NULL,
	ticket_ponto VARCHAR(25) NOT NULL,
	id_consultor INTEGER NOT NULL,
	id_cliente INTEGER NOT NULL,
	PRIMARY KEY (id_ponto),
	CONSTRAINT fk_ponto_consultor FOREIGN KEY (id_consultor)
		REFERENCES tb_consultor(id_consultor)
		ON DELETE CASCADE,
	CONSTRAINT fk_ponto_cliente FOREIGN KEY (id_cliente)
		REFERENCES tb_cliente(id_cliente)
		ON DELETE CASCADE
);
