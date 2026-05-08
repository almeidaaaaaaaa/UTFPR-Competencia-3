-- ============================================================
--  Script de criação do banco de dados: meninas_digitais
--  Execute no MySQL antes de rodar a aplicação.
-- ============================================================

CREATE DATABASE IF NOT EXISTS meninas_digitais
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE meninas_digitais;

-- ── Tabela: usuario ──────────────────────────────────────────
CREATE TABLE IF NOT EXISTS usuario (
    id    INT          NOT NULL AUTO_INCREMENT,
    nome  VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,          -- idealmente um hash BCrypt
    tipo  ENUM('COMUM','GESTOR','ADMINISTRADOR') NOT NULL DEFAULT 'COMUM',
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ── Tabela: projeto_extensao ─────────────────────────────────
CREATE TABLE IF NOT EXISTS projeto_extensao (
    id              INT          NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(150) NOT NULL,
    descricao       TEXT,
    id_coordenador  INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_proj_gestor FOREIGN KEY (id_coordenador) REFERENCES usuario(id)
) ENGINE=InnoDB;

-- ── Tabela: sugestao ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS sugestao (
    id             INT          NOT NULL AUTO_INCREMENT,
    titulo         VARCHAR(200) NOT NULL,
    descricao      TEXT         NOT NULL,
    status         ENUM('PENDENTE','EM_ANALISE','APROVADA','RECUSADA') NOT NULL DEFAULT 'PENDENTE',
    data_envio     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_proponente  INT          NOT NULL,
    id_projeto     INT          NOT NULL,
    justificativa  TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_sug_proponente FOREIGN KEY (id_proponente) REFERENCES usuario(id),
    CONSTRAINT fk_sug_projeto    FOREIGN KEY (id_projeto)    REFERENCES projeto_extensao(id)
) ENGINE=InnoDB;

-- ── Dados de teste ────────────────────────────────────────────
-- Senha "admin123" (em produção use hash BCrypt)
INSERT INTO usuario (nome, email, senha, tipo) VALUES
    ('Admin Sistema',   'admin@utfpr.edu.br',   'admin123',   'ADMINISTRADOR'),
    ('Gestor Projeto',  'gestor@utfpr.edu.br',  'gestor123',  'GESTOR'),
    ('Usuario Comum',   'usuario@utfpr.edu.br', 'user123',    'COMUM');

INSERT INTO projeto_extensao (nome, descricao, id_coordenador) VALUES
    ('Meninas Digitais', 'Projeto de extensão para inclusão feminina na tecnologia.', 2);

INSERT INTO sugestao (titulo, descricao, id_proponente, id_projeto) VALUES
    ('Workshop de Python', 'Realizar um workshop introdutório de Python para iniciantes.', 3, 1),
    ('Hackathon Feminino',  'Organizar um hackathon voltado para mulheres na tecnologia.',  3, 1);
