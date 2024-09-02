package it.icr.vendors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SubcategoryDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;
}
