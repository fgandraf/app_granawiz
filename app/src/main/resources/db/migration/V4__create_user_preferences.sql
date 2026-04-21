CREATE TABLE tbl_user_preferences (
    preference_id INTEGER PRIMARY KEY,
    is_light_theme INTEGER NOT NULL DEFAULT 1
);

INSERT INTO tbl_user_preferences (preference_id, is_light_theme) VALUES (1, 1);
