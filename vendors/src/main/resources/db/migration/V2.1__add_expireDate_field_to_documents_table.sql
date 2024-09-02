ALTER TABLE documents
    ADD COLUMN expire_date DATE,
    ADD COLUMN is_expired BOOLEAN DEFAULT FALSE;