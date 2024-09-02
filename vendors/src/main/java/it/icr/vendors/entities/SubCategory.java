package it.icr.vendors.entities;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "subcategories")
@Entity
public class SubCategory {

    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
}
