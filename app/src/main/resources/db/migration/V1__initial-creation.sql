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
    icon_svg TEXT,

    FOREIGN KEY (group_id) REFERENCES tbl_groups(group_id)
);

CREATE TABLE tbl_tags (
    tag_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE tbl_categories (
    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_type TEXT NOT NULL,
    name TEXT NOT NULL,
    icon TEXT
);

CREATE TABLE tbl_subcategories (
    subcategory_id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_id INTEGER,
    name TEXT NOT NULL,

    FOREIGN KEY (category_id) REFERENCES tbl_categories(category_id)
);

CREATE TABLE tbl_parties (
    party_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    type TEXT CHECK(type IN ('PAYER', 'RECEIVER', 'BOTH')) NOT NULL
);

CREATE TABLE tbl_party_names (
    party_name_id INTEGER PRIMARY KEY AUTOINCREMENT,
    party_id INTEGER,
    name TEXT NOT NULL UNIQUE,

    FOREIGN KEY (party_id) REFERENCES tbl_parties(party_id)
);

CREATE TABLE tbl_transactions (
    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
    party_id INTEGER,
    account_id INTEGER,
    category_id INTEGER,
    subcategory_id INTEGER,
    date DATETIME,
    description TEXT,
    balance REAL NOT NULL,
    type TEXT CHECK(type IN ('GAIN', 'EXPENSE', 'NEUTRAL')) NOT NULL,
    schedule_id INTEGER,
    original_due_date DATETIME,
    installment TEXT DEFAULT '1/1',

    FOREIGN KEY (party_id) REFERENCES tbl_parties(party_id),
    FOREIGN KEY (account_id) REFERENCES tbl_bank_accounts(account_id),
    FOREIGN KEY (category_id) REFERENCES tbl_categories(category_id),
    FOREIGN KEY (subcategory_id) REFERENCES tbl_subcategories(subcategory_id)
    FOREIGN KEY (schedule_id) REFERENCES tbl_schedules(schedule_id)
);

CREATE TABLE tbl_transaction_tag (
    transaction_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    PRIMARY KEY (transaction_id, tag_id),
    FOREIGN KEY (transaction_id) REFERENCES tbl_transactions(transaction_id),
    FOREIGN KEY (tag_id) REFERENCES tbl_tags(tag_id)
);

CREATE TABLE tbl_user_preferences (
    preference_id INTEGER PRIMARY KEY,
    is_light_theme INTEGER NOT NULL DEFAULT 1,
    currency_symbol TEXT NOT NULL DEFAULT 'Brazilian Real (R$)',
    currency_format TEXT NOT NULL DEFAULT 'dot-comma',
    language TEXT NOT NULL DEFAULT 'en-US',
    title_bar_style TEXT NOT NULL DEFAULT 'default'
);

CREATE TABLE tbl_schedules (
    schedule_id INTEGER PRIMARY KEY AUTOINCREMENT,
    party_id INTEGER,
    account_id INTEGER,
    category_id INTEGER,
    subcategory_id INTEGER,
    start_date DATETIME NOT NULL,
    description TEXT,
    balance REAL NOT NULL,
    type TEXT CHECK(type IN ('GAIN', 'EXPENSE', 'NEUTRAL')) NOT NULL,
    frequency TEXT CHECK(frequency IN ('DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY')) NOT NULL,
    interval_value INTEGER NOT NULL DEFAULT 1,
    day_of_month INTEGER,
    end_date DATETIME,
    installments INTEGER,

    FOREIGN KEY (party_id) REFERENCES tbl_parties(party_id),
    FOREIGN KEY (account_id) REFERENCES tbl_bank_accounts(account_id),
    FOREIGN KEY (category_id) REFERENCES tbl_categories(category_id),
    FOREIGN KEY (subcategory_id) REFERENCES tbl_subcategories(subcategory_id)
);

CREATE TABLE tbl_schedule_tag (
    schedule_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    PRIMARY KEY (schedule_id, tag_id),
    FOREIGN KEY (schedule_id) REFERENCES tbl_schedules(schedule_id),
    FOREIGN KEY (tag_id) REFERENCES tbl_tags(tag_id)
);