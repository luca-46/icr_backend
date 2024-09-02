package it.icr.vendors.requests.anagraph;

import it.icr.vendors.dto.sections.VendorAnagraphDto;
import lombok.Data;

@Data
public class GeneralAnagraphRequest {

    private VendorAnagraphDto vendorAnagraph;

    private String vendorId;

}
