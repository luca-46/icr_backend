package it.icr.vendors.responses.anagraph;

import it.icr.vendors.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AnagraphResponse {

    private VendorDto vendor;

    private List<SubcategoryDto> subcategories;

    private CategoryDto category;

    private List<AvailabilityZoneDto> zones;

    private List<OfficeDto> offices;

    private ContactDto contact;
}
