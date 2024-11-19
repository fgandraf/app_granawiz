INSERT INTO tbl_groups VALUES (1, 'Pessoal', 1);
INSERT INTO tbl_groups VALUES (2, 'Empresa', 2);

INSERT INTO tbl_bank_accounts VALUES(1, 'CHECKING','NuBank', 'Conta do NuBank', 2, 'nubank.svg', 2000.0, 1, 2000.0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(2, 'SAVINGS','Inter', 'Conta do Banco Inter', 1, 'inter.svg', 4000.0, 1, 4000.0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(3, 'CHECKING','Banco do Brasil', 'Conta do Banco do Brasil', 3, 'bb.svg', 6000.0, 1, 6000.0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(4, 'CHECKING','Caixa Econômica', 'Conta Corporativa da Caixa Econômica Federal', 2, 'cef.svg', -1000, 2, 0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(5, 'CREDIT_CARD','Mastercard', 'Cartão Mastercard da Caixa Econômica Federal', 1, 'mastercard.svg', -400, 2, 0, 0, 3500,25,5);

INSERT INTO tbl_receivers VALUES (1, 'Amazon');
INSERT INTO tbl_receivers VALUES (2, 'Auto Posto West');
INSERT INTO tbl_receivers VALUES (3, 'Bar do Môa');
INSERT INTO tbl_receivers VALUES (4, 'Casa da Alface');
INSERT INTO tbl_receivers VALUES (5, 'Castelinho da Pamonha');
INSERT INTO tbl_receivers VALUES (6, 'Drogaria Nissei');
INSERT INTO tbl_receivers VALUES (7, 'Panificadora Pão e Companhia');
INSERT INTO tbl_receivers VALUES (8, 'Restaurante do Vale');
INSERT INTO tbl_receivers VALUES (9, 'Rotisserie na Brasa');
INSERT INTO tbl_receivers VALUES (10, 'João Santos');
INSERT INTO tbl_receivers VALUES (11, 'Uber');

INSERT INTO tbl_receiver_names VALUES (1, 6, 'NISSEI DROG');
INSERT INTO tbl_receiver_names VALUES (2, 6, 'DROGARIA NISSEI');
INSERT INTO tbl_receiver_names VALUES (3, 6, 'NISSEI*100-DROG');

INSERT INTO tbl_tags VALUES (1, 'Emergência');
INSERT INTO tbl_tags VALUES (2, 'Black Friday');
INSERT INTO tbl_tags VALUES (3, 'Parcelado');
INSERT INTO tbl_tags VALUES (4, 'Impulso');
INSERT INTO tbl_tags VALUES (5, 'Planejado');
INSERT INTO tbl_tags VALUES (6, 'Cashback');
INSERT INTO tbl_tags VALUES (7, 'Viagem');
INSERT INTO tbl_tags VALUES (8, 'Reforma Cozinha');
INSERT INTO tbl_tags VALUES (9, 'Evento Summit');

INSERT INTO tbl_payers VALUES (1, 'Banco do Brasil');
INSERT INTO tbl_payers VALUES (2, 'Caixa Econômica Federal');
INSERT INTO tbl_payers VALUES (3, 'Marisa Viviani Parra');
INSERT INTO tbl_payers VALUES (4, 'Pedro Burneiko de Godoy');
INSERT INTO tbl_payers VALUES (5, 'Promore - SEESP');

INSERT INTO tbl_payer_names VALUES (1, 2, 'PAG FORN CEF');
INSERT INTO tbl_payer_names VALUES (2, 2, 'PAGAMENTO FORNECEDOR GOV');
INSERT INTO tbl_payer_names VALUES (3, 1, 'BB FORN');
INSERT INTO tbl_payer_names VALUES (4, 1, 'BB FORNECEDOR');

INSERT INTO tbl_categories VALUES (1, 'EXPENSE', 'Alimentação', 'food.svg');
INSERT INTO tbl_categories VALUES (2, 'EXPENSE', 'Compras', 'shop.svg');
INSERT INTO tbl_categories VALUES (3, 'EXPENSE', 'Educação', 'education.svg');
INSERT INTO tbl_categories VALUES (4, 'EXPENSE', 'Lazer', 'fun.svg');
INSERT INTO tbl_categories VALUES (5, 'EXPENSE', 'Moradia', 'house.svg');
INSERT INTO tbl_categories VALUES (6, 'EXPENSE', 'Outros', 'question.svg');
INSERT INTO tbl_categories VALUES (7, 'EXPENSE', 'Saúde', 'medicine.svg');
INSERT INTO tbl_categories VALUES (8, 'EXPENSE', 'Taxas e Impostos', 'weight');
INSERT INTO tbl_categories VALUES (9, 'EXPENSE', 'Transporte', 'car.svg');
INSERT INTO tbl_categories VALUES (10, 'EXPENSE', 'Vestimentas', 'clothes.svg');

INSERT INTO tbl_categories VALUES (11, 'INCOME', 'Salário', 'question.svg');
INSERT INTO tbl_categories VALUES (12, 'INCOME', 'Prestação de Serviços', 'question.svg');
INSERT INTO tbl_categories VALUES (13, 'INCOME', 'Rendimentos', 'question.svg');
INSERT INTO tbl_categories VALUES (14, 'INCOME', 'Empréstimo', 'question.svg');
INSERT INTO tbl_categories VALUES (15, 'INCOME', 'Venda', 'question.svg');


INSERT INTO tbl_sub_categories VALUES (1, 1, 'Conveniência');
INSERT INTO tbl_sub_categories VALUES (2, 1, 'Lanchonete');
INSERT INTO tbl_sub_categories VALUES (3, 1, 'Restaurante');
INSERT INTO tbl_sub_categories VALUES (4, 1, 'Outros');
INSERT INTO tbl_sub_categories VALUES (5, 3, 'Livros');
INSERT INTO tbl_sub_categories VALUES (6, 3, 'Mensalidade');
INSERT INTO tbl_sub_categories VALUES (7, 9, 'Combustível');
INSERT INTO tbl_sub_categories VALUES (8, 9, 'Translado');
INSERT INTO tbl_sub_categories VALUES (9, 7, 'Consulta');
INSERT INTO tbl_sub_categories VALUES (10, 7, 'Medicamento');
INSERT INTO tbl_sub_categories VALUES (11, 12, 'Contrato');
INSERT INTO tbl_sub_categories VALUES (12, 12, 'Particular');
INSERT INTO tbl_sub_categories VALUES (13, 14, 'Bancário');
INSERT INTO tbl_sub_categories VALUES (14, 14, 'Pessoal');