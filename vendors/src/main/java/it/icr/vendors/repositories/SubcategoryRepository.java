package it.icr.vendors.repositories;

import it.icr.vendors.entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoryRepository extends JpaRepository<SubCategory, Integer> {
}
