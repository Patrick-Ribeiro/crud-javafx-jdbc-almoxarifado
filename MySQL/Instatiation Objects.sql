USE almox;

INSERT INTO user_groups (description) VALUES
('Almoxarife'),
('Comprador'), 
('Gerente'),
('Suporte');

INSERT INTO product_categories (description) VALUES
('Bandejas'),
('Bobina/Filme'),
('E.P.I.'),
('Embalagens Rotisseria'),
('Etiqueta de balança'),
('Lanches e refeições'),
('Material de escritório'),
('Material de limpeza'),
('Material de uso e consumo'),
('Rapid bag/Speed roll'),
('Sacolas');

INSERT INTO expenses (debit, description, type) VALUES
(331201, 'Uniformes e Equipamentos de Proteção Individual', 'CONSUMPTION'),
(331410, 'Gás', 'CONSUMPTION'),
(332501, 'Impressos', 'CONSUMPTION'),
(332502, 'Material de Escritório', 'CONSUMPTION'),
(332503, 'Material de Limpeza', 'CONSUMPTION'),
(332504, 'Suprimentos de Informática', 'CONSUMPTION'),
(332505, 'Despesas com Duplicação e Encardenação', 'CONSUMPTION'),
(332507, 'Material de Uso e Consumo', 'CONSUMPTION'),
(332706, 'Lanches e Refeições', 'CONSUMPTION'),
(332601, 'Sacolas', 'SELLING'),
(332602, 'Embalagens Açougue', 'SELLING'),
(332603, 'Embalagens Rotisseria', 'SELLING'),
(332604, 'Bobina Resinite/Filme', 'SELLING'),
(332605, 'Rapid Bag / Spreed Roll', 'SELLING'),
(332606, 'Bandejas', 'SELLING'),
(332607, 'Etiquetas para Balança', 'SELLING'),
(332608, 'Bobina Termoscripty', 'SELLING'),
(332609, 'Suprimentos para o PDV/bobinas', 'SELLING'),
(332612, 'Sacolas Hortifruti', 'SELLING');

INSERT INTO departaments (name) VALUES
('Açougue'),
('Cartão'),
('Contabilidade'),
('Depósito'),
('Fatiados'),
('Financeiro'),
('Frente de caixa'),
('Hortifruti'),
('Marketing'),
('Mercearia'),
('Padaria'),
('R.H.');