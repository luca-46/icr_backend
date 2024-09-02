CREATE TABLE vendor_has_availability_zone (
                                              vendor_id VARCHAR(255),
                                              zone_id INT,
                                              PRIMARY KEY (vendor_id, zone_id),
                                              FOREIGN KEY (vendor_id) REFERENCES vendors(id),
                                              FOREIGN KEY (zone_id) REFERENCES availability_zones(id)
);