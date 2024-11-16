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

INSERT INTO tbl_tags VALUES (1, 'Emergência');
INSERT INTO tbl_tags VALUES (2, 'Black Friday');
INSERT INTO tbl_tags VALUES (3, 'Parcelado');
INSERT INTO tbl_tags VALUES (4, 'Impulso');
INSERT INTO tbl_tags VALUES (5, 'Planejado');
INSERT INTO tbl_tags VALUES (6, 'Cashback');
INSERT INTO tbl_tags VALUES (7, 'Viagem');
INSERT INTO tbl_tags VALUES (8, 'Reforma Cozinha');
INSERT INTO tbl_tags VALUES (9, 'Evento Summit');




INSERT INTO tbl_associated_receivers_names VALUES (1, 6, 'NISSEI DROG');
INSERT INTO tbl_associated_receivers_names VALUES (2, 6, 'DROGARIA NISSEI');
INSERT INTO tbl_associated_receivers_names VALUES (3, 6, 'NISSEI*100-DROG');