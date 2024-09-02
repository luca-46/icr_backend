package it.icr.vendors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OfficeDto {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("state")
    private String state;

    @JsonProperty("province")
    private String province;

    @JsonProperty("county")
    private String county;

    @JsonProperty("address")
    private String address;

    @JsonProperty("address2")
    private String address2;

    @JsonProperty("cap")
    private String cap;

    @JsonProperty("contacts")
    private List<ContactDto> contacts;

}
