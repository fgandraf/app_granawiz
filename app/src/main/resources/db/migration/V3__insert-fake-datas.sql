INSERT INTO tbl_parties VALUES (1, 'Banco do Brasil', 'PAYER');
    INSERT INTO tbl_party_names VALUES (6, 1, 'BB FORN');
    INSERT INTO tbl_party_names VALUES (7, 1, 'BB FORNECEDOR');
INSERT INTO tbl_parties VALUES (2, 'Caixa Econômica Federal', 'PAYER');
    INSERT INTO tbl_party_names VALUES (4, 2, 'PAG FORN CEF');
    INSERT INTO tbl_party_names VALUES (5, 2, 'PAGAMENTO FORNECEDOR GOV');
INSERT INTO tbl_parties VALUES (3, 'Ana Paula da Costa', 'PAYER');
INSERT INTO tbl_parties VALUES (4, 'Pedro Silva', 'PAYER');
INSERT INTO tbl_parties VALUES (5, 'Prefeitura Municipal', 'PAYER');
INSERT INTO tbl_parties VALUES (6, 'Amazon', 'RECEIVER');
INSERT INTO tbl_parties VALUES (7, 'Auto Posto West', 'RECEIVER');
INSERT INTO tbl_parties VALUES (8, 'Bar do Chefe', 'RECEIVER');
INSERT INTO tbl_parties VALUES (9, 'Restaurante da Vó', 'RECEIVER');
INSERT INTO tbl_parties VALUES (10, 'Casa da Alface', 'RECEIVER');
INSERT INTO tbl_parties VALUES (11, 'Castelinho da Pamonha', 'RECEIVER');
INSERT INTO tbl_parties VALUES (12, 'Drogaria Nissei', 'RECEIVER');
    INSERT INTO tbl_party_names VALUES (1, 12, 'NISSEI DROG');
    INSERT INTO tbl_party_names VALUES (2, 12, 'DROGARIA NISSEI');
    INSERT INTO tbl_party_names VALUES (3, 12, 'NISSEI*100-DROG');
INSERT INTO tbl_parties VALUES (13, 'Panificadora do Bairro', 'RECEIVER');
INSERT INTO tbl_parties VALUES (14, 'Restaurante do Vale', 'RECEIVER');
INSERT INTO tbl_parties VALUES (15, 'Rotisserie na Brasa', 'RECEIVER');
INSERT INTO tbl_parties VALUES (16, 'João Santos', 'RECEIVER');
INSERT INTO tbl_parties VALUES (17, 'Uber', 'RECEIVER');
INSERT INTO tbl_parties VALUES (18, 'Auto Posto Seven', 'RECEIVER');
INSERT INTO tbl_parties VALUES (19, '99 Freelas', 'PAYER');
INSERT INTO tbl_parties VALUES (20, 'Mecânica Irmãos', 'RECEIVER');

INSERT INTO tbl_groups VALUES (1, 'Pessoal', 1);
    INSERT INTO tbl_bank_accounts VALUES(1, 'CHECKING','NuBank', 'Conta Corrente do NuBank', 2, 'nubank.svg', 1045.48, 1, 0.0, 0, null,null,null);
        INSERT INTO tbl_transactions VALUES (1,9,1,1, 3,'2024-12-20 15:03:23','Prato feito',-23.50, 'EXPENSE');
        INSERT INTO tbl_transactions VALUES (2,10,1,1, 1,'2024-12-19 12:03:31','Frutas e Legumes',-16.00, 'EXPENSE');
        INSERT INTO tbl_transactions VALUES (3,1,1,12, 11,'2024-12-08 18:22:56','Laudo de Avalização Imobiliário',650.00, 'GAIN');
            INSERT INTO tbl_transaction_tag VALUES (3, 5);
        INSERT INTO tbl_transactions VALUES (4,11,1,1, 1,'2024-12-04 21:43:12','Salgados e café',-26.90, 'EXPENSE');
        INSERT INTO tbl_transactions VALUES (5,13,1,1, 4,'2024-12-20 07:53:01','Almoço',-26.12, 'EXPENSE');
        INSERT INTO tbl_transactions VALUES (6,8,1,4, null,'2024-12-19 09:37:10','Happy Hour da Empresa', -57.50, 'EXPENSE');
        INSERT INTO tbl_transactions VALUES (7,12,1,7, 10,'2024-12-18 09:37:10','Medicamentos', -232.50, 'EXPENSE');
        INSERT INTO tbl_transactions VALUES (8,3,1,12, 12,'2024-11-08 23:04:21','Regularização da Construção', 950.00, 'GAIN');
            INSERT INTO tbl_transaction_tag VALUES (8, 5);
        INSERT INTO tbl_transactions VALUES (9,7,1,1, 1,'2024-11-08 17:14:31','Salgados e café', -8.40, 'EXPENSE');
            INSERT INTO tbl_transaction_tag VALUES (9, 7);
            INSERT INTO tbl_transaction_tag VALUES (9, 9);
        INSERT INTO tbl_transactions VALUES (10,9,1,1, 3,'2024-11-20 14:10:38','Almoço',-23.50, 'EXPENSE');
        INSERT INTO tbl_transactions VALUES (11,18,1,9, 7,'2024-11-06 13:29:41','Gasolina', -182.00, 'EXPENSE');
            INSERT INTO tbl_transaction_tag VALUES (11, 7);
        INSERT INTO tbl_transactions VALUES (12,6,1,2, 15,'2024-11-24 13:29:41','Monitor Asus', -299.90, 'EXPENSE');
            INSERT INTO tbl_transaction_tag VALUES (12, 2);
            INSERT INTO tbl_transaction_tag VALUES (12, 5);
        INSERT INTO tbl_transactions VALUES (13,6,1,3, 5,'2024-11-24 13:29:41','Livro Código Limpo', -99.90, 'EXPENSE');
            INSERT INTO tbl_transaction_tag VALUES (13, 2);
        INSERT INTO tbl_transactions VALUES (14,7,1,9, 7,'2024-11-19 13:29:41','Gasolina', -196.00, 'EXPENSE');
        INSERT INTO tbl_transactions VALUES (15,4,1,12, 12,'2024-10-08 19:49:48','Parecer Técnico - Parcela 1/2', 200.00, 'GAIN');
            INSERT INTO tbl_transaction_tag VALUES (15, 3);
            INSERT INTO tbl_transaction_tag VALUES (15, 5);
        INSERT INTO tbl_transactions VALUES (16,15,1,1, 1,'2024-10-09 22:16:56','Frango Assado', -42.30, 'EXPENSE');
        INSERT INTO tbl_transactions VALUES (17,19,1,12, 12,'2024-11-21 23:04:21','Projeto AVCB', 800.00, 'GAIN');
        INSERT INTO tbl_transactions VALUES (18,20,1,9, 16,'2024-11-08 19:49:48','Troca da Correia do Alternador', -320.00, 'EXPENSE');
            INSERT INTO tbl_transaction_tag VALUES (18, 1);
    INSERT INTO tbl_bank_accounts VALUES(2, 'SAVINGS','Banco do Brasil', 'Conta Poupança do Banco do Brasil', 3, 'bb.svg', 0.0, 1, 0.0, 0, null,null,null);
INSERT INTO tbl_groups VALUES (2, 'Empresa', 2);
    INSERT INTO tbl_bank_accounts VALUES(3, 'CHECKING','Caixa Econômica', 'Conta Corrente Corporativa da Caixa Econômica Federal', 2, 'cef.svg', 0.0, 2, 0, 0, null,null,null);















