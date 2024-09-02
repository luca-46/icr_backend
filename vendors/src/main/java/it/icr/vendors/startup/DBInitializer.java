package it.icr.vendors.startup;

import it.icr.vendors.entities.Category;
import it.icr.vendors.entities.ICRDocument;
import it.icr.vendors.entities.State;
import it.icr.vendors.entities.Vendor;
import it.icr.vendors.repositories.CategoryRepository;
import it.icr.vendors.repositories.DocTypeRepository;
import it.icr.vendors.repositories.StateRepository;
import it.icr.vendors.repositories.VendorRepository;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@PropertySource({
        "classpath:application.properties",
        "classpath:flyway.properties"

})
@Component
public class DBInitializer implements CommandLineRunner {

    @Value("${flyway.url}")
    private String datasource_url;

    @Value("${flyway.user}")
    private String datasource_user;

    @Value("${flyway.password}")
    private String datasource_password;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private DocTypeRepository docTypeRepository;

    @Override
    public void run(String... args) throws Exception {

        Flyway flyway = Flyway.configure().dataSource(datasource_url, datasource_user, datasource_password).load();
        flyway.migrate();


        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(List.of(
                new Category(1, "SubAppaltatore", null),
                new Category(2, "Fornitore", null),
                new Category(3, "Servizi", null)
            ));
        }

        if (stateRepository.count() == 0) {
            stateRepository.saveAll(List.of(
                new State(1, "PENDING_REGISTRATION"),
                new State(2, "DRAFT"),
                new State(3, "PENDING_APPROVAL")
            ));
        }


        if (vendorRepository.count() == 0) {
            vendorRepository.save(new Vendor(
                    "a",
                    null,
                    null,
                    "vendor1@icr.com",
                    "$2a$10$sQAvg8.9oduxmD/OvEb04uVj1jm62qDSYUEtGk98.L0.6akuL2KRW",
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    new State(1, "PENDING_REGISTRATION")

            ));
        }

        if (docTypeRepository.count() == 0) {
            docTypeRepository.saveAll(List.of(
                new ICRDocument(1, "Camera di commercio"),
                new ICRDocument(2, "DURC"),
                new ICRDocument(3, "SOA")
            ));
        }
    }

}
