package it.icr.vendors.services.impl;

import io.vavr.control.Either;
import io.vavr.control.Try;
import it.icr.vendors.entities.Document;
import it.icr.vendors.entities.ICRDocument;
import it.icr.vendors.entities.Vendor;
import it.icr.vendors.repositories.DocTypeRepository;
import it.icr.vendors.repositories.DocumentRepository;
import it.icr.vendors.repositories.VendorRepository;
import it.icr.vendors.services.DocumentService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@PropertySource({
        "classpath:uploads.properties"
})
@Service
public class DocumentServiceImpl implements DocumentService {

    @Value("${documents.upload-dir}")
    private String uploadsPath;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private DocTypeRepository docTypeRepository;

    @Autowired
    private DocumentRepository documentRepository;

    BiFunction<List<MultipartFile>, List<Integer>, HashMap<MultipartFile, Integer>> mapFileToType = (files, doc_ids) -> {
        HashMap<MultipartFile, Integer> map = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            map.put(files.get(i), doc_ids.get(i));
        }
        return map;
    };

    BiFunction<String, String ,Try<Path>> generatePath = ( vendor_id, hash ) ->
            Try.of(() -> {
                Path path = Paths.get(uploadsPath + "/" + vendor_id).toAbsolutePath().normalize();

                Files.createDirectories(path);
                return path.resolve(hash);
            });

//    BiFunction<HashMap<MultipartFile, Integer>, String, Try<Vendor>> saveDocuments = (map, vendor_id) -> {
//        List<Document> documents = new ArrayList<>();
//        map.forEach((file, doc_id) -> {
//            Document doc = new Document();
//            doc.setFilename(file.getOriginalFilename());
//            doc.setFilePath(uploadsPath + "/" + vendor_id + "/" + file.getOriginalFilename());
//            doc.setIcrDocument(docTypeRepository.findById(doc_id).get());
//
//            documents.add(doc);
//        });
//
//
//
//        return Try.of(() -> {
//            List<Document> _documents = (List<Document>) documentRepository.saveAll(documents);
//            Vendor vendor = vendorRepository.findById(vendor_id).get();
//            vendor.getDocuments().addAll(_documents);
//            return vendorRepository.save(vendor);
//        });
//    };

    Function<Date, Date> add6Months = (date) -> {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 6);
        return calendar.getTime();
    };

    Function<Date, Date> add3Months = (date) -> {
        return new Date(date.getTime() + 3L * 30 * 24 * 60 * 60 * 1000);
    };

    private Try<Vendor> saveDocuments(HashMap<MultipartFile, Integer> map, String vendor_id, List<Date> expireDates) {
        List<Document> documents = new ArrayList<>();
        final int[] i = {0};
        map.forEach((file, doc_id) -> {
            Document doc = new Document();
            ICRDocument docType = docTypeRepository.findById(doc_id).get();

            Date expireDate = expireDates.get(i[0]);

            if (docType.getId().equals(1)) {
                expireDate = add6Months.apply(expireDate);
            }

            if (docType.getId().equals(2)) {
                expireDate = add3Months.apply(expireDate);
            }

            doc.setExpireDate(expireDate);
            doc.setFilename(file.getOriginalFilename());
            doc.setFilePath(uploadsPath + "/" + vendor_id + "/" + file.getOriginalFilename());
            doc.setIcrDocument(docType);

            documents.add(doc);
            i[0]++;
        });

        return Try.of(() -> {
            List<Document> _documents = (List<Document>) documentRepository.saveAll(documents);
            Vendor vendor = vendorRepository.findById(vendor_id).get();
            vendor.getDocuments().addAll(_documents);
            return vendorRepository.save(vendor);
        });
    }

    @Override
    public Either<Error, Vendor> saveDocuments(List<MultipartFile> files, List<Integer> doc_ids, String vendor_id, List<Date> expireDates) {
        HashMap<MultipartFile, Integer> map = mapFileToType.apply(files, doc_ids);

        map.forEach((file, doc_id) -> {
            String hash = file.getOriginalFilename();
            Try<Path> path = generatePath.apply(vendor_id, hash);
            path.onSuccess(p -> Try.run(() -> file.transferTo(p)));
        });

        return saveDocuments(map, vendor_id, expireDates)
                .fold(
                        throwable -> Either.left(new Error(throwable.getMessage())),
                        Either::right
                );
    }

    @Override
    public Try<List<ICRDocument>> getDocumentTypes() {

        return Try.of(() -> (List<ICRDocument>) docTypeRepository.findAll());
    }

    @Override
    public List<Document> getVendorDocuments(String vendorId) {
        Optional<Vendor> vendor = vendorRepository.findById(vendorId);


        return vendor.get().getDocuments();

    }

}
