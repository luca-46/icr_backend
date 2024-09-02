package it.icr.vendors.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "availability_zones")
public class AvailabilityZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "state")
    private String state;

    @Column(name = "region")
    private String region;

    @Column(name = "province")
    private String province;

    @Column(name = "county")
    private String county;

}