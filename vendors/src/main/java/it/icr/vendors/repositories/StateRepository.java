package it.icr.vendors.repositories;

import it.icr.vendors.entities.State;
import it.icr.vendors.entities.Vendor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StateRepository extends CrudRepository<State, Integer> {
    @Query("SELECT s FROM State s WHERE s.name = :name")
    Optional<State> findByName(@Param("name") String name);
}
