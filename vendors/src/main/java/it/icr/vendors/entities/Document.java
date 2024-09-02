package it.icr.vendors.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at",  insertable = false)
    private Timestamp updatedAt;

    @Column(name = "expireDate")
    private Date expireDate;

    @Column(name = "is_expired")
    private Boolean isExpired;

    @ManyToOne
    @JoinColumn(name = "type")
    private ICRDocument icrDocument;

}