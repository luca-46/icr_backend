package it.icr.vendors.controllers.impl;

import it.icr.vendors.controllers.AnagraphController;
import it.icr.vendors.dto.CategoryDto;
import it.icr.vendors.dto.ChangeVendorStateDto;
import it.icr.vendors.dto.VendorDto;
import it.icr.vendors.entities.State;
import it.icr.vendors.entities.Vendor;
import it.icr.vendors.mappers.MapStructMapper;
import it.icr.vendors.requests.anagraph.GeneralAnagraphRequest;
import it.icr.vendors.responses.ResponseHandler;
import it.icr.vendors.responses.anagraph.AnagraphResponse;
import it.icr.vendors.services.AnagraphService;
import it.icr.vendors.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AnagraphControllerImpl implements AnagraphController {

    @Autowired
    private AnagraphService anagraphService;

    @Autowired
    private MapStructMapper mapper;

    @Autowired
    private DocumentService documentService;

    @Override
    public ResponseEntity<Object> getVendor(Authentication authentication) {
        return ResponseHandler.generateResponse("Vendor retrieved successfully", HttpStatus.OK, mapper.vendorToVendorDto((Vendor) authentication.getPrincipal()));
    }

    public ResponseEntity<Object> saveAnagraph(@RequestBody GeneralAnagraphRequest request) {

        return anagraphService.saveAnagraph(request)
                .fold(
                  error -> ResponseHandler.generateResponse(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null),
                  success -> ResponseHandler.generateResponse("Anagraph saved successfully", HttpStatus.OK,
                                new AnagraphResponse(
                                   mapper.vendorToVendorDto(success),
                                        mapper.subcategoryToSubcategoryDto(success.getSubcategories()),
                                        mapper.categoryToCategoryDto(success.getSubcategories().get(0).getCategory()),
                                        mapper.listAvailabilityZoneToAvailabilityZoneDto(success.getZones()),
                                       mapper.listOfficeToOfficeDto(success.getOffices()),
                                        null
                                        //mapper.contactToContactDto(success.getContact().get(0))
                                )
                          )
                );

    }

    @Override
    public ResponseEntity<Object> getAvailableCategories() {
        return anagraphService.getAvailableCategories()
                .fold(
                        error -> ResponseHandler.generateResponse(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null),
                        success -> ResponseHandler.generateResponse("Categories retrieved successfully", HttpStatus.OK, mapper.listCategoryToCategoryDto(success))
                );
    }

    @Override
    public ResponseEntity<Object> getVendorCategory(Authentication authentication) {



        return anagraphService.getVendorCategory((Vendor) authentication.getPrincipal())
                .fold(
                        error -> ResponseHandler.generateResponse(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null),
                        success -> ResponseHandler.generateResponse("Category retrieved successfully", HttpStatus.OK, mapper.categoryToCategoryDto(success))
                );

    }

    @Override
    public ResponseEntity<Object> getVendorZones(Authentication authentication) {
        return anagraphService.getVendorZones((Vendor) authentication.getPrincipal())
                .fold(
                        error -> ResponseHandler.generateResponse(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null),
                        success -> ResponseHandler.generateResponse("Zones retrieved successfully", HttpStatus.OK, mapper.listAvailabilityZoneToAvailabilityZoneDto(success))
                );
    }

    @Override
    public ResponseEntity<Object> getVendorOffices(Authentication authentication) {
        return anagraphService.getVendorOffices((Vendor) authentication.getPrincipal())
                .fold(
                        error -> ResponseHandler.generateResponse(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null),
                        success -> ResponseHandler.generateResponse("Offices retrieved successfully", HttpStatus.OK, mapper.listOfficeToOfficeDto(success))
                );
    }

    @Override
    public ResponseEntity<List<VendorDto>> getAllVendors(){
        return anagraphService.getAllVendors();
    }

    @Override
    public ResponseEntity<List<State>> getStates() throws Exception {
        try{

        return anagraphService.getStates();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ChangeVendorStateDto> changeVendorState(String state, String vendorId) throws Exception{
        try{
            return anagraphService.changeVendorState(state,vendorId);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
