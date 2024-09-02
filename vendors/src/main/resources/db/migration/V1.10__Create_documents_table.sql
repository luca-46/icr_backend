CREATE TABLE documents (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           filename VARCHAR(255),
                           file_path VARCHAR(255),
                           type INT,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (type) REFERENCES icr_documents(id)
);