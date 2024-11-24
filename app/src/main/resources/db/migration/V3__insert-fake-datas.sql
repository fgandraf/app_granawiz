INSERT INTO tbl_groups VALUES (1, 'Pessoal', 1);
INSERT INTO tbl_groups VALUES (2, 'Empresa', 2);

INSERT INTO tbl_bank_accounts VALUES(1, 'CHECKING','NuBank', 'Conta do NuBank', 2, 'nubank.svg', 2000.0, 1, 2000.0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(2, 'SAVINGS','Inter', 'Conta do Banco Inter', 1, 'inter.svg', 4000.0, 1, 4000.0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(3, 'CHECKING','Banco do Brasil', 'Conta do Banco do Brasil', 3, 'bb.svg', 6000.0, 1, 6000.0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(4, 'CHECKING','Caixa Econômica', 'Conta Corporativa da Caixa Econômica Federal', 2, 'cef.svg', -1000, 2, 0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(5, 'CREDIT_CARD','Mastercard', 'Cartão Mastercard da Caixa Econômica Federal', 1, 'mastercard.svg', -400, 2, 0, 0, 3500,25,5);

INSERT INTO tbl_parties VALUES (1, 'Banco do Brasil', 'PAYER');
INSERT INTO tbl_parties VALUES (2, 'Caixa Econômica Federal', 'PAYER');
INSERT INTO tbl_parties VALUES (3, 'Marisa Viviani Parra', 'PAYER');
INSERT INTO tbl_parties VALUES (4, 'Pedro Burneiko de Godoy', 'PAYER');
INSERT INTO tbl_parties VALUES (5, 'Promore - SEESP', 'PAYER');
INSERT INTO tbl_parties VALUES (6, 'Amazon', 'RECEIVER');
INSERT INTO tbl_parties VALUES (7, 'Auto Posto West', 'RECEIVER');
INSERT INTO tbl_parties VALUES (8, 'Bar do Môa', 'RECEIVER');
INSERT INTO tbl_parties VALUES (9, 'Casa da Alface', 'RECEIVER');
INSERT INTO tbl_parties VALUES (10, 'Castelinho da Pamonha', 'RECEIVER');
INSERT INTO tbl_parties VALUES (11, 'Drogaria Nissei', 'RECEIVER');
INSERT INTO tbl_parties VALUES (12, 'Panificadora Pão e Companhia', 'RECEIVER');
INSERT INTO tbl_parties VALUES (13, 'Restaurante do Vale', 'RECEIVER');
INSERT INTO tbl_parties VALUES (14, 'Rotisserie na Brasa', 'RECEIVER');
INSERT INTO tbl_parties VALUES (15, 'João Santos', 'RECEIVER');
INSERT INTO tbl_parties VALUES (16, 'Uber', 'RECEIVER');

INSERT INTO tbl_party_names VALUES (1, 11, 'NISSEI DROG');
INSERT INTO tbl_party_names VALUES (2, 11, 'DROGARIA NISSEI');
INSERT INTO tbl_party_names VALUES (3, 11, 'NISSEI*100-DROG');
INSERT INTO tbl_party_names VALUES (4, 2, 'PAG FORN CEF');
INSERT INTO tbl_party_names VALUES (5, 2, 'PAGAMENTO FORNECEDOR GOV');
INSERT INTO tbl_party_names VALUES (6, 1, 'BB FORN');
INSERT INTO tbl_party_names VALUES (7, 1, 'BB FORNECEDOR');

INSERT INTO tbl_transactions VALUES (1,9,1,1, 3,'2024-11-20 15:03:32','Marmita Fitness',-23.50, 'EXPENSE');
INSERT INTO tbl_transactions VALUES (2,9,1,1, 1,'2024-11-19 12:03:32','Doce de Leite Ninho',-16.00, 'EXPENSE');
INSERT INTO tbl_transactions VALUES (3,3,1,12, null,'2024-11-08 12:03:32','Regularização da Construção',650.00, 'GAIN');
INSERT INTO tbl_transactions VALUES (4,10,1,1, 1,'2024-11-04 12:03:32','Salgados e café',-26.90, 'EXPENSE');

INSERT INTO tbl_transaction_tag VALUES (1, 5);
INSERT INTO tbl_transaction_tag VALUES (2, 1);
INSERT INTO tbl_transaction_tag VALUES (2, 4);
INSERT INTO tbl_transaction_tag VALUES (3, 3);
INSERT INTO tbl_transaction_tag VALUES (4, 7);
INSERT INTO tbl_transaction_tag VALUES (4, 5);


















INSERT INTO tbl_transactions VALUES (5,9,1,1, 3,'2024-10-20 15:03:32','Marmita Fitness',-26.12, 'EXPENSE');
INSERT INTO tbl_transactions VALUES (6,9,1,1, 1,'2024-10-19 12:03:32','Doce de Leite Ninho', -17.50, 'EXPENSE');
INSERT INTO tbl_transactions VALUES (7,3,1,12, null,'2024-09-08 12:03:32','Regularização da Construção', 350.00, 'GAIN');
INSERT INTO tbl_transactions VALUES (8,10,1,1, 1,'2024-09-08 12:03:32','Salgados e café', -8.40, 'EXPENSE');

INSERT INTO tbl_transactions VALUES (9,9,1,1, 3,'2024-09-20 15:03:32','Marmita Fitness',-23.50, 'EXPENSE');
INSERT INTO tbl_transactions VALUES (10,9,1,1, 1,'2024-08-19 12:03:32','Paçoca', -8.00, 'EXPENSE');
INSERT INTO tbl_transactions VALUES (11,3,1,12, null,'2024-07-08 12:03:32','Regularização da Construção', 200.00, 'GAIN');
INSERT INTO tbl_transactions VALUES (12,10,1,1, 1,'2024-07-08 12:03:32','Salgados e café', -12.30, 'EXPENSE');



INSERT INTO tbl_transaction_tag VALUES (5, 5);
INSERT INTO tbl_transaction_tag VALUES (6, 1);
INSERT INTO tbl_transaction_tag VALUES (6, 4);
INSERT INTO tbl_transaction_tag VALUES (7, 3);
INSERT INTO tbl_transaction_tag VALUES (8, 7);
INSERT INTO tbl_transaction_tag VALUES (8, 9);


INSERT INTO tbl_transaction_tag VALUES (9, 5);
INSERT INTO tbl_transaction_tag VALUES (10, 1);
INSERT INTO tbl_transaction_tag VALUES (10, 4);
INSERT INTO tbl_transaction_tag VALUES (11, 3);
INSERT INTO tbl_transaction_tag VALUES (12, 7);
INSERT INTO tbl_transaction_tag VALUES (12, 9);