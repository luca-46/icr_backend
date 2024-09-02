CREATE TABLE offices (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         state VARCHAR(255),
                         province VARCHAR(255),
                         county VARCHAR(255),
                         address VARCHAR(255),
                         address2 VARCHAR(255) NULL,
                         cap VARCHAR(255),
                         vendor_id VARCHAR(255),
                         FOREIGN KEY (vendor_id) REFERENCES vendors(id)
);