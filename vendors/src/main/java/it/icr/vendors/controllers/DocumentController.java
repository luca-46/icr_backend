package it.icr.vendors.controllers;

import it.icr.vendors.entities.Document;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@RequestMapping("/documents")
public interface DocumentController {
    @PostMapping("/save")
    ResponseEntity<Object> saveDocuments(
            @RequestParam("vendor_id") String vendor_id,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("ids") List<Integer> doc_ids,
            @RequestParam("dates") @DateTimeFormat(pattern = "dd-MM-yyyy") List<Date> expireDates
    );

    @GetMapping("/types")
    ResponseEntity<Object> getDocumentTypes();

    @GetMapping("/vendor")
    ResponseEntity<Object> getVendorDocuments(Authentication authentication);

    @GetMapping("/download/{vendorId}/{filename:.+}")
    default ResponseEntity<Resource> downloadFile(@PathVariable String vendorId, @PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(vendorId).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/vendor/{vendorId}")
    ResponseEntity<List<Document>> getVendorDocumentsByVendorId(@PathVariable String vendorId) throws Exception;
}
