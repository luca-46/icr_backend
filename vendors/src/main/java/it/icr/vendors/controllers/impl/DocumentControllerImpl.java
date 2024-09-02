package it.icr.vendors.controllers.impl;

import io.vavr.control.Either;
import io.vavr.control.Try;
import it.icr.vendors.controllers.DocumentController;
import it.icr.vendors.entities.Document;
import it.icr.vendors.entities.ICRDocument;
import it.icr.vendors.entities.Vendor;
import it.icr.vendors.responses.ResponseHandler;
import it.icr.vendors.services.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class DocumentControllerImpl implements DocumentController {

    @Autowired
    private DocumentService documentService;

    public ResponseEntity<Object> saveDocuments(
            @RequestParam(name = "vendor_id", required=false) String vendor_id,
            @RequestParam(name = "files[]", required=true) MultipartFile[] files,
            @RequestParam(name = "ids", required=false) List<Integer> doc_ids,
            @RequestParam("dates") @DateTimeFormat(pattern = "dd-MM-yyyy") List<Date> expireDates
    ){

        Either<Error, Vendor> result = documentService.saveDocuments(Arrays.stream(files).toList(), doc_ids, vendor_id, expireDates);

        return result.fold(
                error -> ResponseHandler.generateResponse(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null),
                vendor -> ResponseHandler.generateResponse("Documents saved successfully", HttpStatus.OK, vendor)
        );
    }

    @Override
    public ResponseEntity<Object> getDocumentTypes() {
        Try<List<ICRDocument>> result = documentService.getDocumentTypes();

        return result.isSuccess() ?
                ResponseHandler.generateResponse("Document types retrieved successfully", HttpStatus.OK, result.get()) :
                ResponseHandler.generateResponse("Error retrieving document types", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @Override
    public ResponseEntity<Object> getVendorDocuments(Authentication authentication) {
        Vendor vendor = (Vendor) authentication.getPrincipal();

        return ResponseHandler.generateResponse("Vendor documents retrieved successfully", HttpStatus.OK, vendor.getDocuments());
    }

    @Override
    public ResponseEntity<List<Document>> getVendorDocumentsByVendorId(String vendorId) throws Exception {
        return ResponseEntity.ok(documentService.getVendorDocuments(vendorId));
    }
}
