package it.icr.vendors.repositories;

import it.icr.vendors.entities.ICRDocument;
import org.springframework.data.repository.CrudRepository;

public interface DocTypeRepository extends CrudRepository<ICRDocument, Integer> {
}
