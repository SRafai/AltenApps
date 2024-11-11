package com.alten.dev.productsapp.dto;

import com.alten.dev.productsapp.enums.InventoryStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import java.util.Date;

@Data
public class ProductDTO {
    @JsonView(Views.Internal.class)
    @JsonProperty
    private Long id;
    @JsonView(Views.Public.class)
    @JsonProperty
    private String code;
    @JsonView(Views.Public.class)
    @JsonProperty
    private String name;
    @JsonView(Views.Public.class)
    @JsonProperty
    private String description;
    @JsonView(Views.Public.class)
    @JsonProperty
    private String category;
    @JsonView(Views.Public.class)
    @JsonProperty
    private double price;
    @JsonView(Views.Public.class)
    @JsonProperty
    private int quantity;
    @JsonView(Views.Public.class)
    @JsonProperty
    private InventoryStatusEnum inventoryStatus;
    @JsonView(Views.Public.class)
    @JsonProperty
    private double rating;
    @JsonView(Views.Public.class)
    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date createdAt;
    @JsonView(Views.Public.class)
    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date updatedAt;
}
