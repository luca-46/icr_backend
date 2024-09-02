CREATE TABLE vendor_has_document (
                                     vendor_id VARCHAR(255),
                                     document_id INT,
                                     PRIMARY KEY (vendor_id, document_id),
                                     FOREIGN KEY (vendor_id) REFERENCES vendors(id),
                                     FOREIGN KEY (document_id) REFERENCES documents(id)
);