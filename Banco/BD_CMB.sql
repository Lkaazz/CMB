#LUCAS DA SILVA CARVALHO = CP3020363
#LEONARDO SOUZA FARIA DE MORAES = CP3015777
#MATHEUS DOS ANJOS DE OLIVEIRA - CP3020762

DROP SCHEMA IF EXISTS cmb;
CREATE SCHEMA IF NOT EXISTS cmb;
USE cmb;

-- Definição de Tabelas

-- Tabela de Passageiros
CREATE TABLE passageiros (
  passageiro_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  nascimento DATE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  senha VARCHAR(32) NOT NULL
);

-- Tabela de Linhas de Ônibus
CREATE TABLE linhas_onibus (
  linha_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  numero VARCHAR(20) NOT NULL,
  descricao VARCHAR(255) NOT NULL
);

-- Tabela de Tipos de Problemas
CREATE TABLE problemas_tipos (
  tipo_problema_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  descricao VARCHAR(255) NOT NULL
);

-- Tabela de Problemas Reportados
CREATE TABLE problemas_reportados (
  problema_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  descricao VARCHAR(255) NOT NULL,
  tipo_problema_id INT NOT NULL,
  linha_id INT NOT NULL,
  passageiro_id INT NOT NULL,
  data_hora DATETIME NOT NULL,
  FOREIGN KEY (tipo_problema_id) REFERENCES problemas_tipos(tipo_problema_id),
  FOREIGN KEY (linha_id) REFERENCES linhas_onibus(linha_id),
  FOREIGN KEY (passageiro_id) REFERENCES passageiros(passageiro_id)
);

-- Inserção de Dados na Tabela de Passageiros
INSERT INTO passageiros (nome, email, senha, nascimento) VALUES
('Leonardo', 'leonardo@exemplo.com', '123', '2023-01-01'),
('Lucas', 'lucas@exemplo.com', '123', '2023-01-02'),
('Ana', 'ana@exemplo.com', '123', '2023-02-15'),
('Matheus', 'matheus@exemplo.com', '123', '2023-03-20');

-- Inserção de Dados na Tabela de Tipos de Problemas
INSERT INTO problemas_tipos (descricao) VALUES
('Atraso'),
('Lotação'),
('Segurança'),
('Acidente');

-- Inserção de Dados na Tabela de Linhas de Ônibus
INSERT INTO linhas_onibus (numero, descricao) VALUES
('225', 'RESIDENCIAL SÍRIUS / CORREDOR CENTRAL'),
('226', 'RESIDENCIAL SÍRIUS / TERMINAL SATÉLITE ÍRIS'),
('210', 'TERMINAL CAMPO GRANDE / TERMINAL BARÃO GERALDO'),
('213', 'TERMINAL ITAJAÍ / RODOVIÁRIA'),
('227', 'JARDIM FLORENCE II / TERMINAL SATÉLITE ÍRIS'),
('BRT20', 'TERMINAL CAMPO GRANDE / CORREDOR CENTRAL');

-- Inserção de Dados na Tabela de Problemas Reportados
INSERT INTO problemas_reportados (descricao, tipo_problema_id, linha_id, passageiro_id, data_hora) VALUES
('Ônibus quebrou no meio do trajeto', 4, 3, 3, '2023-06-05 10:30:00'),
('Atraso recorrente nos últimos dias', 1, 1, 2, '2023-06-06 08:45:00'),
('Veículo lotado mesmo em horários fora de pico', 2, 2, 4, '2023-06-07 12:00:00'),
('Problema de segurança identificado no terminal', 3, 6, 1, '2023-06-08 14:00:00'),
('Alteração no itinerário sem aviso prévio', 1, 4, 3, '2023-06-09 16:30:00');


-- Criação de VIEWS, PROCEDURES, TRIGGERS, FUNCTIONS E CURSORES

-- View Problemas Detalhados
CREATE VIEW vw_problemas_detalhados AS
SELECT pr.problema_id, pr.descricao AS problema_descricao, pr.data_hora,
       pt.descricao AS tipo_problema, lo.numero AS numero_linha, lo.descricao AS descricao_linha,
       p.nome AS passageiro_nome, p.email AS passageiro_email
FROM problemas_reportados pr
JOIN problemas_tipos pt ON pr.tipo_problema_id = pt.tipo_problema_id
JOIN linhas_onibus lo ON pr.linha_id = lo.linha_id
JOIN passageiros p ON pr.passageiro_id = p.passageiro_id;

-- View Linhas Mais Problemáticas
CREATE VIEW vw_linhas_mais_problematicas AS
SELECT lo.numero AS numero_linha, COUNT(pr.problema_id) AS quantidade_problemas
FROM linhas_onibus lo
LEFT JOIN problemas_reportados pr ON lo.linha_id = pr.linha_id
GROUP BY lo.numero;

-- Procedure Adicionar Problema Reportado
DELIMITER //
CREATE PROCEDURE sp_adicionar_problema_reportado(
    IN p_descricao VARCHAR(255),
    IN p_tipo_problema_id INT,
    IN p_linha_id INT,
    IN p_passageiro_id INT,
    IN p_data_hora DATETIME
)
BEGIN
    INSERT INTO problemas_reportados (descricao, tipo_problema_id, linha_id, passageiro_id, data_hora)
    VALUES (p_descricao, p_tipo_problema_id, p_linha_id, p_passageiro_id, p_data_hora);
END //
DELIMITER ;

-- Procedure Atualizar Senha do Passageiro
DELIMITER //
CREATE PROCEDURE sp_atualizar_senha_passageiro(
    IN p_passageiro_id INT,
    IN p_nova_senha VARCHAR(32)
)
BEGIN
    UPDATE passageiros
    SET senha = p_nova_senha
    WHERE passageiro_id = p_passageiro_id;
END //
DELIMITER ;

-- Procedure para Listar Problemas de um Passageiro
DELIMITER //
CREATE PROCEDURE sp_listar_problemas_por_passageiro(IN p_passageiro_id INT)
BEGIN
    -- Selecionar os detalhes dos problemas do passageiro
    SELECT CONCAT('Problema ID: ', pr.problema_id, ', Descrição: ', pr.descricao) AS Detalhes_Problema
    FROM problemas_reportados pr
    WHERE pr.passageiro_id = p_passageiro_id;
END //
DELIMITER ;

-- Função Contar Problemas por Tipo
DELIMITER //
CREATE FUNCTION fn_contar_problemas_por_tipo(
    p_tipo_problema_id INT
) RETURNS VARCHAR(255) DETERMINISTIC
BEGIN
    DECLARE quantidade INT;
    DECLARE descricao_problema VARCHAR(255);

    -- Obter a quantidade de problemas
    SELECT COUNT(*) INTO quantidade
    FROM problemas_reportados
    WHERE tipo_problema_id = p_tipo_problema_id;

    -- Obter a descrição do tipo de problema
    SELECT descricao INTO descricao_problema
    FROM problemas_tipos
    WHERE tipo_problema_id = p_tipo_problema_id;

    -- Retornar a quantidade e a descrição
    RETURN CONCAT('Quantidade de problemas do tipo "', descricao_problema, '": ', quantidade);
END //
DELIMITER ;

-- Função Buscar Linha Mais Reportada por Tipo de Problema
DELIMITER //
CREATE FUNCTION fn_buscar_linha_mais_reportada(p_tipo_problema_id INT) RETURNS VARCHAR(20) DETERMINISTIC
BEGIN
    DECLARE numero_linha VARCHAR(20);

    -- Utilizar subconsulta para encontrar a linha mais reportada com base no tipo de problema
    SELECT lo.numero INTO numero_linha
    FROM linhas_onibus lo
    JOIN problemas_reportados pr ON lo.linha_id = pr.linha_id
    WHERE pr.tipo_problema_id = p_tipo_problema_id
    GROUP BY lo.numero
    ORDER BY COUNT(pr.problema_id) DESC
    LIMIT 1;

    RETURN numero_linha;
END //
DELIMITER ;

-- Trigger Antes de Atualização de Senha
DELIMITER //
CREATE TRIGGER before_update_senha
BEFORE UPDATE ON passageiros
FOR EACH ROW
BEGIN
    -- Validar se a nova senha tem pelo menos 6 caracteres
    IF LENGTH(NEW.senha) < 6 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'A nova senha deve ter pelo menos 6 caracteres';
    END IF;
END;
DELIMITER ;

-- Função Detalhes de Problemas por Passageiro
DELIMITER //
CREATE FUNCTION fn_detalhes_problemas_por_passageiro(p_passageiro_id INT) RETURNS VARCHAR(255) DETERMINISTIC
BEGIN
    DECLARE detalhes_problema VARCHAR(255);
    DECLARE done INT DEFAULT FALSE;
    DECLARE cur_problemas CURSOR FOR
        SELECT CONCAT('Problema ID: ', pr.problema_id, ', Descrição: ', pr.descricao) AS Detalhes_Problema
        FROM problemas_reportados pr
        WHERE pr.passageiro_id = p_passageiro_id;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur_problemas;

    SET detalhes_problema = '';

    read_loop: LOOP
        FETCH cur_problemas INTO detalhes_problema;
        IF done THEN
            LEAVE read_loop;
        END IF;
    END LOOP;

    CLOSE cur_problemas;

    RETURN detalhes_problema;
END //
DELIMITER ;

-- Consultas e Testes

-- Consulta para Linhas sem Problemas
SELECT lo.numero AS linhas_sem_problemas_reportados
FROM linhas_onibus lo
LEFT JOIN problemas_reportados pr ON lo.linha_id = pr.linha_id
WHERE pr.problema_id IS NULL;

-- Consultar quantidade de problemas reportados
SELECT COUNT(*) AS quantidade_problemas
FROM problemas_reportados;

-- Teste View Problemas Detalhados
SELECT * FROM vw_problemas_detalhados;

-- Teste View Linhas Mais Problemáticas
SELECT * FROM vw_linhas_mais_problematicas;

-- Teste Procedure Adicionar Problema
CALL sp_adicionar_problema_reportado('Novo problema', 3, 2, 1, '2023-06-02 10:00:00');

-- Verificar o novo problema na tabela
SELECT * FROM problemas_reportados;

-- Teste Procedure Atualizar Senha do Passageiro
CALL sp_atualizar_senha_passageiro(1, 'nova_senha123');

-- Verificar se a senha foi atualizada
SELECT senha FROM passageiros WHERE passageiro_id = 1;

-- Teste Function Contar Problemas por Tipo
SELECT fn_contar_problemas_por_tipo(1) AS quantidade_problemas;

-- Teste Function Buscar Linha Mais Reportada por Tipo de Problema
SELECT fn_buscar_linha_mais_reportada(2) AS linha_mais_reportada;

-- Inserir problema para acionar o trigger after_insert_problema
INSERT INTO problemas_reportados (descricao, tipo_problema_id, linha_id, passageiro_id, data_hora)
VALUES ('Novo problema', 1, 1, 2, '2023-06-02 11:00:00');

-- Inserir problema para o passageiro 2
INSERT INTO problemas_reportados (descricao, tipo_problema_id, linha_id, passageiro_id, data_hora)
VALUES ('Problema do Passageiro 2', 1, 1, 2, '2023-06-03 14:00:00');

-- Inserir problema para o passageiro 1
INSERT INTO problemas_reportados (descricao, tipo_problema_id, linha_id, passageiro_id, data_hora)
VALUES ('Novo problema do Passageiro 1', 1, 1, 1, '2023-06-04 15:00:00');

-- Chamar Procedure para Listar Problemas de um Passageiro
CALL sp_listar_problemas_por_passageiro(1);

-- Chamar função e armazenar detalhes dos problemas do passageiro com ID 1
SELECT fn_detalhes_problemas_por_passageiro(1) AS detalhes_problema_passageiro;
