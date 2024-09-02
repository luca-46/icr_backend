package it.icr.vendors.services.impl;

import io.vavr.control.Either;
import io.vavr.control.Try;
import it.icr.vendors.dto.*;
import it.icr.vendors.dto.sections.VendorAnagraphDto;
import it.icr.vendors.entities.*;
import it.icr.vendors.enums.VendorStateEnum;
import it.icr.vendors.mappers.MapStructMapper;
import it.icr.vendors.repositories.*;
import it.icr.vendors.requests.anagraph.GeneralAnagraphRequest;
import it.icr.vendors.services.AnagraphService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AnagraphServiceImpl implements AnagraphService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private OfficeRespoitory officeRespoitory;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AvailabilityZoneRepository zoneRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MapStructMapper mapper;

     Function<String, Vendor> getSavingVendor = (vendorId) -> vendorRepository.findById(vendorId)
            .orElseThrow(() -> new RuntimeException("Vendor not found"));

     Function<List<Integer>, Try<List<SubCategory>>> getVendorSubcategories = (subcategoriesIds) ->
            Try.of(() -> subcategoriesIds.stream()
                    .map(subcategoryRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));

     Function<List<Integer>, Try<List<AvailabilityZone>>> getVendorZones = (zonesIds) ->
            Try.of(() -> zonesIds.stream()
                    .map(zoneRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));

     Function<List<AvailabilityZone>, Try<List<AvailabilityZone>>> saveNewZones = (toBeSavedZones) ->
            Try.of(() -> toBeSavedZones.stream()
                    .map(zoneRepository::save)
                    .collect(Collectors.toList()));


    Function<Vendor, Try<Vendor>> prepareAnagraphic = (vendor) ->
            Try.of(() -> {
                Vendor savedVendor = getSavingVendor.apply(vendor.getId());
                vendor.setPassword(savedVendor.getPassword());
                vendor.setState(savedVendor.getState());
                return vendor;
            });

    BiFunction<List<AvailabilityZone>, Vendor, Try<Vendor>> prepareAvailabilityZones =
            (zones, vendor) -> Try.of(() -> {

                List<AvailabilityZone> toBeUpdatedZones = Optional.ofNullable(zones)
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(x -> x.getId() != null)
                        .toList();

                List<AvailabilityZone> savedZones = saveNewZones.apply(
                        Optional.ofNullable(zones)
                                .orElse(Collections.emptyList())
                                .stream()
                                .filter(y -> y.getId() == null)
                                .toList()
                ).getOrElseThrow(throwable -> new RuntimeException("Error saving new zones", throwable));

                List<AvailabilityZone> _zones = new java.util.ArrayList<>(toBeUpdatedZones);

                _zones.addAll(savedZones);


                vendor.setZones(_zones);

                return vendor;
            });

    BiFunction<List<SubcategoryDto>, Vendor, Try<Vendor>> prepareSubcategories = (subcategories, vendor) ->
            Try.of(() -> {
                List<SubCategory> _subcategories = getVendorSubcategories.apply(
                            subcategories.stream()
                                    .map(SubcategoryDto::getId)
                                    .toList()
                        ).getOrElseThrow(throwable -> new RuntimeException("Error getting subcategories", throwable));

                vendor.setSubcategories(_subcategories);

                return vendor;
            });

    BiFunction<List<Office>, Vendor, Try<Vendor>> prepareOffices = (offices, vendor) ->
            Try.of(() -> {
                List<Office> _offices = Optional.ofNullable(offices)
                        .orElse(Collections.emptyList())
                        .stream()
                        .peek(office -> office.setVendor_id(vendor.getId()))
                        .peek(office -> office.getContacts().stream().peek(contact -> contact.setOffice_id(office)).toList())
                        .toList();

                vendor.setOffices(
                        _offices
                );
                return vendor;
            });

    BiFunction<Vendor, List<Integer> ,Try<Vendor>> deleteVendorOffices = (vendor, sent_offices) ->
            Try.of(() -> {
                officeRespoitory.deleteAll(
                        Optional.ofNullable(vendor.getOffices())
                                .orElse(Collections.emptyList())
                                .stream()
                                .filter(office -> !sent_offices.contains(office.getId()))
                                .toList()
                );
                return vendor;
            });

    BiFunction<Vendor, List<Integer> ,Try<Vendor>> deleteVendorZones = (vendor, sent_zones) ->
            Try.of(() -> {
                zoneRepository.deleteAll(
                        Optional.ofNullable(vendor.getZones())
                                .orElse(Collections.emptyList())
                                .stream()
                                .filter(zone -> !sent_zones.contains(zone.getId()))
                                .toList()
                );
                return vendor;
            });

    Function<VendorAnagraphDto, Try<Vendor>> prepareVendorForSaving = (vendorAnagraphDto) ->
            Try.of(() ->
                {
                    Try<Vendor> vendor = prepareAnagraphic.apply(
                            mapper.vendorDtoToVendor(vendorAnagraphDto.getVendor())
                    ).onFailure(throwable -> {
                        throw new RuntimeException("Error preparing vendor for saving", throwable);
                    });

                    vendor = vendor.map(v -> {
                       return prepareAvailabilityZones.apply(
                               Optional.ofNullable(vendorAnagraphDto.getZones())
                                       .orElse(Collections.emptyList())
                                       .stream()
                                       .map(mapper::availabilityZoneDtoToAvailabilityZone)
                                       .toList(),
                               v
                       ).getOrElseThrow(throwable -> new RuntimeException("Error preparing availability zones", throwable));
                    });

                    vendor = vendor.map(v -> {
                        v.setCategory(
                                categoryRepository.findById(vendorAnagraphDto.getCategory().getId())
                                        .orElseThrow(() -> new RuntimeException("Category not found"))
                        );
                        return v;
                    });

                    vendor = vendor.map(v -> prepareSubcategories.apply(vendorAnagraphDto.getSubcategories(), v)
                            .getOrElseThrow(throwable -> new RuntimeException("Error preparing availability Subcategories", throwable)));

                    vendor = vendor.map(v -> prepareOffices.apply(
                            Optional.ofNullable(vendorAnagraphDto.getOffices())
                                    .orElse(Collections.emptyList())
                                    .stream()
                                    .map(mapper::officeDtoToOffice)
                                    .toList(),
                            v
                    ).getOrElseThrow(throwable -> new RuntimeException("Error preparing availability offices", throwable)));

                    deleteVendorOffices.apply(getSavingVendor.apply(vendorAnagraphDto.getVendor().getId()),
                            Optional.ofNullable(vendorAnagraphDto.getOffices())
                                    .orElse(Collections.emptyList())
                                    .stream().map(OfficeDto::getId).toList());

                    deleteVendorZones.apply(getSavingVendor.apply(vendorAnagraphDto.getVendor().getId()),
                            Optional.ofNullable(vendorAnagraphDto.getZones())
                                    .orElse(Collections.emptyList())
                                    .stream().map(AvailabilityZoneDto::getId).toList());

                    vendor = vendor.map(v -> {
                        Optional.ofNullable(vendorAnagraphDto.getVendor().getState())
                                .filter(state -> state.equals(VendorStateEnum.PENDING_REGISTRATION.name()))
                                .ifPresentOrElse(
                                        state ->
                                                v.setState(stateRepository.findByName(VendorStateEnum.DRAFT.name())
                                                    .orElseThrow(() -> new RuntimeException("State not found"))),
                                        () -> v.setState(stateRepository.findByName(vendorAnagraphDto.getVendor().getState())
                                                .orElseThrow(() -> new RuntimeException("State not found")))
                                );
                        return v;
                    });

                    vendor.map(v -> {
                        v.setDocuments(
                                getSavingVendor.apply(vendorAnagraphDto.getVendor().getId()).getDocuments()
                        );
                        return v;
                    });

                    return vendor.get();
                }
            );

    @Transactional
    public Either<Error, Vendor> saveAnagraph(GeneralAnagraphRequest request) {
      return prepareVendorForSaving.apply(request.getVendorAnagraph())
                    .map(vendorRepository::save)
                    .toEither()
                    .mapLeft(v -> v)
                    .mapLeft(v -> new Error("Error saving vendor", new RuntimeException("Error saving vendor")));
    }

    @Override
    public Either<Error, List<Category>> getAvailableCategories() {
        return Try.of(() -> (List<Category>) categoryRepository.findAll())
                .toEither()
                .mapLeft(e -> new Error("Error retrieving categories", e));
    }

    @Override
    public Either<Error, Category> getVendorCategory(Vendor vendor) {
        return Try.of(() -> vendorRepository.findById(vendor.getId()))
                .toEither()
                .mapLeft(e -> new Error("Error retrieving vendor", e))
                .map(v -> v.get().getCategory());
    }

    @Override
    public Either<Error, List<AvailabilityZone>> getVendorZones(Vendor vendor) {
        return Try.of(() -> vendorRepository.findById(vendor.getId()))
                .toEither()
                .mapLeft(e -> new Error("Error retrieving vendor", e))
                .map(v -> v.get().getZones());
    }

    @Override
    public Either<Error, List<Office>> getVendorOffices(Vendor vendor) {
        return Try.of(() -> vendorRepository.findById(vendor.getId()))
                .toEither()
                .mapLeft(e -> new Error("Error retrieving vendor", e))
                .map(v -> v.get().getOffices());
    }

    @Override
    public ResponseEntity<List<VendorDto>> getAllVendors() {
        List<VendorDto> vendorDtos = new ArrayList<>();
        List<Vendor> vendors = (List<Vendor>) vendorRepository.findAll();
        vendors.forEach(v -> vendorDtos.add(mapper.vendorToVendorDto(v)));
        return ResponseEntity.ok(vendorDtos);
    }

    @Override
    public ResponseEntity<List<State>> getStates(){
        return ResponseEntity.ok((List<State>) stateRepository.findAll());
    }

    @Override
    public ResponseEntity<ChangeVendorStateDto> changeVendorState(String state, String vendorId){
        Optional<Vendor> vendor = vendorRepository.findById(vendorId);
        Optional<State> stateToChange = stateRepository.findByName(state);
        if(vendor.isPresent() && stateToChange.isPresent()){
            vendor.get().setState(stateToChange.get());
            vendorRepository.save(vendor.get());

            ChangeVendorStateDto dto = new ChangeVendorStateDto();
            dto.setVendorId(vendorId);
            dto.setState(state);
            dto.setMsg("State changed");

            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
