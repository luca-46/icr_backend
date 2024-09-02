package it.icr.vendors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeVendorStateDto {
    @JsonProperty("vendorId")
    private String vendorId;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("state")
    private String state;
}
