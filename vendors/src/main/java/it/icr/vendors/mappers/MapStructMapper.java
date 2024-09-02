package it.icr.vendors.mappers;

import it.icr.vendors.dto.*;
import it.icr.vendors.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {

    @Mapping(source = "state.name", target = "state")
    VendorDto vendorToVendorDto(Vendor vendor);

    @Mapping(source = "state", target = "state.name")

    Vendor vendorDtoToVendor(VendorDto vendorDto);
    AvailabilityZone availabilityZoneDtoToAvailabilityZone(AvailabilityZoneDto availabilityZoneDto);

    AvailabilityZoneDto availabilityZoneToAvailabilityZoneDto(AvailabilityZone availabilityZone);

    List<AvailabilityZoneDto> listAvailabilityZoneToAvailabilityZoneDto(List<AvailabilityZone> availabilityZone);

    Office officeDtoToOffice(OfficeDto officeDto);

    OfficeDto officeToOfficeDto(Office office);

    List<OfficeDto> listOfficeToOfficeDto(List<Office> office);

    Contact contactDtoToContact(ContactDto contactDto);

    ContactDto contactToContactDto(Contact contact);

    List<SubcategoryDto> subcategoryToSubcategoryDto(List<SubCategory> subcategory);

    CategoryDto categoryToCategoryDto(Category category);

    List<CategoryDto> listCategoryToCategoryDto(List<Category> category);


}
