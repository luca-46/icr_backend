package it.icr.vendors.controllers;

import it.icr.vendors.dto.ChangeVendorStateDto;
import it.icr.vendors.dto.VendorDto;
import it.icr.vendors.entities.State;
import it.icr.vendors.entities.Vendor;
import it.icr.vendors.requests.anagraph.GeneralAnagraphRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/anagraph")
public interface AnagraphController {
    @GetMapping
    ResponseEntity<Object> getVendor(Authentication authentication);

    @PostMapping
    ResponseEntity<Object> saveAnagraph(@RequestBody GeneralAnagraphRequest request);

    @GetMapping("/categories")
    ResponseEntity<Object> getAvailableCategories();

    @GetMapping("/vendor/categories")
    ResponseEntity<Object> getVendorCategory(Authentication authentication);

    @GetMapping("/vendor/zones")
    ResponseEntity<Object> getVendorZones(Authentication authentication);

    @GetMapping("/vendor/offices")
    ResponseEntity<Object> getVendorOffices(Authentication authentication);

    @GetMapping("/all-vendors")
    ResponseEntity<List<VendorDto>> getAllVendors();

    @GetMapping("/states")
    ResponseEntity<List<State>> getStates() throws Exception;

    @PutMapping("/vendor/state/{vendorId}")
    ResponseEntity<ChangeVendorStateDto> changeVendorState(@RequestBody String state, @PathVariable String vendorId) throws Exception;

}
