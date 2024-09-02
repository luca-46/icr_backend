package it.icr.vendors.services;

import io.vavr.control.Either;
import io.vavr.control.Try;
import it.icr.vendors.entities.Document;
import it.icr.vendors.entities.ICRDocument;
import it.icr.vendors.entities.Vendor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface DocumentService {
    Either<Error, Vendor> saveDocuments(List<MultipartFile> files, List<Integer> doc_ids, String vendor_id, List<Date> expireDates);

    Try<List<ICRDocument>> getDocumentTypes();

    List<Document> getVendorDocuments(String vendorId);
}
