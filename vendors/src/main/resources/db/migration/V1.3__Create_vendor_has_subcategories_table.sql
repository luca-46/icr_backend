CREATE TABLE vendor_has_subcategories (
                                          vendor_id VARCHAR(36),
                                          subcategory_id INT,
                                          PRIMARY KEY (vendor_id, subcategory_id),
                                          FOREIGN KEY (vendor_id) REFERENCES vendors(id),
                                          FOREIGN KEY (subcategory_id) REFERENCES subcategories(id)
);