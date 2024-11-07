CREATE TABLE TBL_GROUPS (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    position INTEGER
);


CREATE TABLE TBL_BANK_ACCOUNTS (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
    closing_date TEXT,
    due_date TEXT,
    portfolio TEXT,

    FOREIGN KEY (group_id) REFERENCES TBL_GROUPS(id)
);