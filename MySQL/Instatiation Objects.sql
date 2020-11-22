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
(331201, 'Uniformes e Equipamentos de Proteção Individual', 'C'),
(331410, 'Gás', 'C'),
(332501, 'Impressos', 'C'),
(332502, 'Material de Escritório', 'C'),
(332503, 'Material de Limpeza', 'C'),
(332504, 'Suprimentos de Informática', 'C'),
(332505, 'Despesas com Duplicação e Encardenação', 'C'),
(332507, 'Material de Uso e Consumo', 'C'),
(332706, 'Lanches e Refeições', 'C'),
(332601, 'Sacolas', 'S'),
(332602, 'Embalagens Açougue', 'S'),
(332603, 'Embalagens Rotisseria', 'S'),
(332604, 'Bobina Resinite/Filme', 'S'),
(332605, 'Rapid Bag / Spreed Roll', 'S'),
(332606, 'Bandejas', 'S'),
(332607, 'Etiquetas para Balança', 'S'),
(332608, 'Bobina Termoscripty', 'S'),
(332609, 'Suprimentos para o PDV/bobinas', 'S'),
(332612, 'Sacolas Hortifruti', 'S');

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