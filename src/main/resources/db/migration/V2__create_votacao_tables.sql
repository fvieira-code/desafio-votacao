CREATE TABLE IF NOT EXISTS pauta (
  id BIGINT NOT NULL AUTO_INCREMENT,
  titulo VARCHAR(200) NOT NULL,
  descricao TEXT NULL,
  criada_em DATETIME NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sessao_votacao (
  id BIGINT NOT NULL AUTO_INCREMENT,
  pauta_id BIGINT NOT NULL,
  aberta_em DATETIME NOT NULL,
  encerra_em DATETIME NOT NULL,
  status VARCHAR(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_sessao_pauta
    FOREIGN KEY (pauta_id) REFERENCES pauta(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  INDEX idx_sessao_pauta_status (pauta_id, status),
  INDEX idx_sessao_pauta_encerra (pauta_id, encerra_em)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS voto (
  id BIGINT NOT NULL AUTO_INCREMENT,
  pauta_id BIGINT NOT NULL,
  associado_id VARCHAR(80) NOT NULL,
  valor VARCHAR(10) NOT NULL,
  criado_em DATETIME NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_voto_pauta
    FOREIGN KEY (pauta_id) REFERENCES pauta(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT uk_voto_pauta_associado
    UNIQUE (pauta_id, associado_id),
  INDEX idx_voto_pauta (pauta_id),
  INDEX idx_voto_pauta_valor (pauta_id, valor)
) ENGINE=InnoDB;
