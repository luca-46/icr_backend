ALTER TABLE vendors
    ADD COLUMN tech_referent VARCHAR(255),
ADD COLUMN admin_referent VARCHAR(255),
ADD COLUMN phone VARCHAR(255),
ADD COLUMN pec VARCHAR(255) UNIQUE,
ADD COLUMN category_id INT,
ADD COLUMN state INT;

ALTER TABLE vendors
    ADD FOREIGN KEY (category_id) REFERENCES categories(id),
ADD FOREIGN KEY (state) REFERENCES icr_state(id);