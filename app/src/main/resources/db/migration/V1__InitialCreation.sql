CREATE TABLE tbl_groups (
    group_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    position INTEGER
);


CREATE TABLE tbl_bank_accounts (
    account_id INTEGER PRIMARY KEY AUTOINCREMENT,
    account_type TEXT NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    position INTEGER,
    icon TEXT,
    balance REAL NOT NULL,
    group_id INTEGER,
    open_balance REAL,
    overdraft_limit REAL,
    credit_limit REAL,
    closing_day INTEGER(2),
    due_day INTEGER(2),

    FOREIGN KEY (group_id) REFERENCES tbl_groups(group_id)
);