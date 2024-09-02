package it.icr.vendors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.icr.vendors.entities.Vendor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorRegistrationDto {
    private Vendor vendor;
    private String tempPassword;
}
