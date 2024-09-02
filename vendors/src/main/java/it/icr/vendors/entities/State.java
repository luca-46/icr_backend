package it.icr.vendors.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "icr_state")
public class State {

    @Id
    private Integer id;

    @Column
    private String name;
}