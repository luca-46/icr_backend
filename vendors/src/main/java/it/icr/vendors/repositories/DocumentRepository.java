package it.icr.vendors.repositories;

import it.icr.vendors.entities.Document;
import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends CrudRepository<Document, Integer>{
}
