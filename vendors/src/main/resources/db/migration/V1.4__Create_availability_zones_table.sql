CREATE TABLE availability_zones (
                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                    state VARCHAR(255),
                                    region VARCHAR(255) NULL,
                                    province VARCHAR(255) NULL,
                                    county VARCHAR(255) NULL
);