package it.icr.vendors.repositories;

import it.icr.vendors.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
