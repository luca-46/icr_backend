package it.icr.vendors.repositories;

import it.icr.vendors.entities.Vendor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VendorRepository extends CrudRepository<Vendor, String>{
    Optional<Vendor> findByEmail(String email);
}
