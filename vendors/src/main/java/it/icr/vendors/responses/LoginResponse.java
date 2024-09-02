package it.icr.vendors.responses;

import it.icr.vendors.dto.VendorDto;
import lombok.Data;

@Data
public class LoginResponse {

    private VendorDto vendor;
    private String token;

}
