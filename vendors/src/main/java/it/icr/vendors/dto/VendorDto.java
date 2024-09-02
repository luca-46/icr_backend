package it.icr.vendors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class VendorDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("vat_number")
    private String vatNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("tech_referent")
    private String techReferent;

    @JsonProperty("admin_referent")
    private String adminReferent;

    @JsonProperty("pec")
    private String pec;

    @JsonProperty("is_first_access")
    private Boolean isFirstAccess;

    @JsonProperty("state")
    private String state;
}
