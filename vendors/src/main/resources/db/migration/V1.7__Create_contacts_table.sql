CREATE TABLE contacts (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          email VARCHAR(255),
                          phone VARCHAR(255) NULL,
                          office_id INT,
                          FOREIGN KEY (office_id) REFERENCES offices(id)
);