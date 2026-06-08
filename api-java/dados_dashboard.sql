CREATE TABLE IF NOT EXISTS transacoes (
    mes VARCHAR(20), 
    regiao VARCHAR(50), 
    produto VARCHAR(100),
    status VARCHAR(50),
    valor DOUBLE
);

DELETE FROM transacoes;

INSERT INTO transacoes VALUES 
('Junho', 'Sul', 'Licença Corporativa', 'Ativo', 15000.0),
('Junho', 'Sul', 'Consultoria', 'Ativo', 4500.0),
('Junho', 'Sudeste', 'Licença Corporativa', 'Ativo', 28000.0),
('Julho', 'Sul', 'Licença Corporativa', 'Cancelado', 5000.0),
('Julho', 'Nordeste', 'Consultoria', 'Ativo', 8000.0),
('Julho', 'Sudeste', 'Licença Corporativa', 'Ativo', 35000.0),
('Agosto', 'Norte', 'Consultoria', 'Em Negociação', 6000.0),
('Agosto', 'Sudeste', 'Licença Corporativa', 'Ativo', 41000.0),
('Julho', 'Norte', 'Licença Corporativa', 'Ativo', 18000.0);