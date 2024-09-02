package it.icr.vendors.repositories;

import it.icr.vendors.entities.Contact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Integer>{
}
