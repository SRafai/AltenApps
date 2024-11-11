package com.alten.dev.productsapp.mapper;

import com.alten.dev.productsapp.entities.Product;
import com.alten.dev.productsapp.dto.ProductDTO;
import com.alten.dev.productsapp.enums.InventoryStatusEnum;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProductMapper {

    public ProductDTO toDto(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setCode(product.getCode());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setInventoryStatus(product.getInventoryStatus());
        dto.setRating(product.getRating());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null; // or throw a custom exception
        }

        Product product = new Product();
        product.setId(dto.getId());  // Ensure that ID is correctly set (it might be null in DTO)
        product.setCode(dto.getCode() != null ? dto.getCode() : "");  // Set defaults for required fields
        product.setName(dto.getName() != null ? dto.getName() : "");
        product.setDescription(dto.getDescription() != null ? dto.getDescription() : "");
        product.setCategory(dto.getCategory() != null ? dto.getCategory() : "");
        product.setPrice(dto.getPrice() != 0 ? dto.getPrice() : 0.0);  // Set default price if missing
        product.setQuantity(dto.getQuantity() != 0 ? dto.getQuantity() : 0); // Set default quantity
        product.setInventoryStatus(dto.getInventoryStatus() != null ? dto.getInventoryStatus() : InventoryStatusEnum.LOWSTOCK); // Assuming a default Enum
        product.setRating(dto.getRating() != 0 ? dto.getRating() : 0.0);  // Default rating
        product.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : new Date());
        product.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : new Date());
        return product;
    }
}
