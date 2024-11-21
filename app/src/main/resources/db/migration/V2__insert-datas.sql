INSERT INTO tbl_groups VALUES (1, 'Pessoal', 1);
INSERT INTO tbl_groups VALUES (2, 'Empresa', 2);

INSERT INTO tbl_bank_accounts VALUES(1, 'CHECKING','NuBank', 'Conta do NuBank', 2, 'nubank.svg', 2000.0, 1, 2000.0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(2, 'SAVINGS','Inter', 'Conta do Banco Inter', 1, 'inter.svg', 4000.0, 1, 4000.0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(3, 'CHECKING','Banco do Brasil', 'Conta do Banco do Brasil', 3, 'bb.svg', 6000.0, 1, 6000.0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(4, 'CHECKING','Caixa Econômica', 'Conta Corporativa da Caixa Econômica Federal', 2, 'cef.svg', -1000, 2, 0, 0, null,null,null);
INSERT INTO tbl_bank_accounts VALUES(5, 'CREDIT_CARD','Mastercard', 'Cartão Mastercard da Caixa Econômica Federal', 1, 'mastercard.svg', -400, 2, 0, 0, 3500,25,5);

INSERT INTO tbl_tags VALUES (1, 'Emergência');
INSERT INTO tbl_tags VALUES (2, 'Black Friday');
INSERT INTO tbl_tags VALUES (3, 'Parcelado');
INSERT INTO tbl_tags VALUES (4, 'Impulso');
INSERT INTO tbl_tags VALUES (5, 'Planejado');
INSERT INTO tbl_tags VALUES (6, 'Cashback');
INSERT INTO tbl_tags VALUES (7, 'Viagem');
INSERT INTO tbl_tags VALUES (8, 'Reforma Cozinha');
INSERT INTO tbl_tags VALUES (9, 'Evento Summit');

INSERT INTO tbl_categories VALUES (1, 'EXPENSE', 'Alimentação', 'pizza.svg');
INSERT INTO tbl_categories VALUES (2, 'EXPENSE', 'Compra', 'shopping-cart.svg');
INSERT INTO tbl_categories VALUES (3, 'EXPENSE', 'Educação', 'graduation-cap.svg');
INSERT INTO tbl_categories VALUES (4, 'EXPENSE', 'Lazer', 'mask-happy.svg');
INSERT INTO tbl_categories VALUES (5, 'EXPENSE', 'Moradia', 'house-line.svg');
INSERT INTO tbl_categories VALUES (6, 'EXPENSE', 'Outro', 'question-mark.svg');
INSERT INTO tbl_categories VALUES (7, 'EXPENSE', 'Saúde', 'first-aid.svg');
INSERT INTO tbl_categories VALUES (8, 'EXPENSE', 'Taxa e Imposto', 'scales.svg');
INSERT INTO tbl_categories VALUES (9, 'EXPENSE', 'Transporte', 'car-profile.svg');
INSERT INTO tbl_categories VALUES (10, 'EXPENSE', 'Vestimenta', 't-shirt.svg');

INSERT INTO tbl_categories VALUES (11, 'INCOME', 'Salário', 'money.svg');
INSERT INTO tbl_categories VALUES (12, 'INCOME', 'Prestação de Serviço', 'call-bell.svg');
INSERT INTO tbl_categories VALUES (13, 'INCOME', 'Rendimento', 'chart-line-up.svg');
INSERT INTO tbl_categories VALUES (14, 'INCOME', 'Empréstimo', 'bank.svg');
INSERT INTO tbl_categories VALUES (15, 'INCOME', 'Venda', 'handshake.svg');

INSERT INTO tbl_subcategories VALUES (1, 1, 'Conveniência');
INSERT INTO tbl_subcategories VALUES (2, 1, 'Lanchonete');
INSERT INTO tbl_subcategories VALUES (3, 1, 'Restaurante');
INSERT INTO tbl_subcategories VALUES (4, 1, 'Outros');
INSERT INTO tbl_subcategories VALUES (5, 3, 'Livros');
INSERT INTO tbl_subcategories VALUES (6, 3, 'Mensalidade');
INSERT INTO tbl_subcategories VALUES (7, 9, 'Combustível');
INSERT INTO tbl_subcategories VALUES (8, 9, 'Translado');
INSERT INTO tbl_subcategories VALUES (9, 7, 'Consulta');
INSERT INTO tbl_subcategories VALUES (10, 7, 'Medicamento');
INSERT INTO tbl_subcategories VALUES (11, 12, 'Contrato');
INSERT INTO tbl_subcategories VALUES (12, 12, 'Particular');
INSERT INTO tbl_subcategories VALUES (13, 14, 'Bancário');
INSERT INTO tbl_subcategories VALUES (14, 14, 'Pessoal');

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
INSERT INTO tbl_transactions VALUES (4,10,1,1, 1,'2024-11-04 12:03:32','Salgados e café',26.90, 'EXPENSE');

INSERT INTO tbl_transaction_tag VALUES (1, 5);
INSERT INTO tbl_transaction_tag VALUES (2, 1);
INSERT INTO tbl_transaction_tag VALUES (2, 4);
INSERT INTO tbl_transaction_tag VALUES (3, 3);
INSERT INTO tbl_transaction_tag VALUES (4, 7);
INSERT INTO tbl_transaction_tag VALUES (4, 9);
