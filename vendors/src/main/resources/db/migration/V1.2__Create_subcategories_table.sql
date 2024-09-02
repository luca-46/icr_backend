CREATE TABLE subcategories (
                               id INT PRIMARY KEY,
                               name VARCHAR(255),
                               category_id INT,
                               FOREIGN KEY (category_id) REFERENCES categories(id)
);