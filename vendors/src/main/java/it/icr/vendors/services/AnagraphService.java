package it.icr.vendors.services;

import io.vavr.control.Either;
import it.icr.vendors.dto.ChangeVendorStateDto;
import it.icr.vendors.dto.VendorDto;
import it.icr.vendors.entities.*;
import it.icr.vendors.requests.anagraph.GeneralAnagraphRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.function.Function;

public interface AnagraphService {

    Either<Error, Vendor> saveAnagraph(GeneralAnagraphRequest request) ;

    Either<Error, List<Category>> getAvailableCategories() ;

    Either<Error, Category> getVendorCategory(Vendor vendor);

    Either<Error, List<AvailabilityZone>> getVendorZones(Vendor vendor);

    Either<Error, List<Office>> getVendorOffices(Vendor vendor);

    ResponseEntity<List<VendorDto>> getAllVendors();

    ResponseEntity<List<State>> getStates();

    ResponseEntity<ChangeVendorStateDto> changeVendorState(String state, String vendorId);

}
