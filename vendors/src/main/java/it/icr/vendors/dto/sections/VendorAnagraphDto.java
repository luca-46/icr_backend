package it.icr.vendors.dto.sections;

import it.icr.vendors.dto.*;
import lombok.Data;

import java.util.List;

@Data
public class VendorAnagraphDto {

    private VendorDto vendor;

    private CategoryDto category;

    private List<SubcategoryDto> subcategories;

    private List<AvailabilityZoneDto> zones;

    private List<OfficeDto> offices;

//    private ContactDto contact;

}
