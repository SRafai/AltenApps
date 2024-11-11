package com.alten.dev.productsapp.controllers;

import com.alten.dev.productsapp.dto.ProductDTO;
import com.alten.dev.productsapp.dto.Views;
import com.alten.dev.productsapp.entities.Product;
import com.alten.dev.productsapp.mapper.ProductMapper;
import com.alten.dev.productsapp.services.ProductService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.query.sqm.ParsingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class ProductsController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductsController(ProductService productService, ProductMapper productMapper){
        this.productService = productService;
        this.productMapper = productMapper;
    }

    //Ajouter un produit
    @Operation(summary = "Créer un produit")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.Public.class)
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product){
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    //Afficher tous les produits
    // Get paginated products
    @Operation(summary = "Afficher tous les produits avec pagination")
    @GetMapping
    @JsonView(Views.Internal.class)
    public ResponseEntity<List<ProductDTO>> getAllProductsP(
            @RequestParam(defaultValue = "0") int _start,
            @RequestParam(defaultValue = "10") int _limit) {

        // Create Pageable object from query parameters
        Pageable pageable = PageRequest.of(_start / _limit, _limit);

        List<ProductDTO> productsDTO = this.productService
                .getPaginatedProducts(pageable)  // Assuming the service method is updated for pagination
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(productsDTO, HttpStatus.OK);
    }

    //Afficher un produit
    @Operation(summary = "Chercher un produit")
    @GetMapping("/{id}")
    @JsonView(Views.Internal.class)
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id){
        ProductDTO productDTO = this.productMapper.toDto(this.productService.getProduct(id));
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    //Mettre à jour un produit
    @Operation(summary = "Modifier un produit")
    @PatchMapping(path = "/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) {
        if (product != null && product.getId() != null) {
            if (product.getUpdatedAt() == null) {
                product.setUpdatedAt(new Date());  // Set updatedAt to the current date if it's null
            }
            // If createdAt is null, consider setting it or ignoring it in update (assuming it shouldn't change)
            if (product.getCreatedAt() == null) {
                product.setCreatedAt(new Date());
            }
            try {
                Product p = this.productMapper.toEntity(product);
                if (p != null) {
                    Product updatedProduct = this.productService.updateProduct(id, p);
                    if (updatedProduct != null) {
                        ProductDTO productDTO = this.productMapper.toDto(updatedProduct);
                        return new ResponseEntity<>(productDTO, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Invalid entity
                }
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Catch any parsing errors here
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



    //Supprimer un produit
    @Operation(summary = "Supprimer un produit")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
