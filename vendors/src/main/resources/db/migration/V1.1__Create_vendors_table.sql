CREATE TABLE vendors (
                         id VARCHAR(36) PRIMARY KEY,
                         company_name VARCHAR(255),
                         vat_number VARCHAR(255),
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255),
                         is_first_access BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);