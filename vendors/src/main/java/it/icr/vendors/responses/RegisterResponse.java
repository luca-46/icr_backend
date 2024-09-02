package it.icr.vendors.responses;

import it.icr.vendors.dto.VendorDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterResponse {

    private String tempPassword;
    private String message;
}
