package com.alten.dev.productsapp.controller;

import com.alten.dev.productsapp.dto.ProductDTO;
import com.alten.dev.productsapp.dto.Views;
import com.alten.dev.productsapp.entities.Product;
import com.alten.dev.productsapp.mapper.ProductMapper;
import com.alten.dev.productsapp.services.ProductService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProducsController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProducsController(ProductService productService, ProductMapper productMapper){
        this.productService = productService;
        this.productMapper = productMapper;
    }

    //Ajouter un produit
    @Operation(summary = "Créer un produit")
    @PostMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product){
        Product productEntity = this.productMapper.toEntity(product);
        return new ResponseEntity<>(this.productMapper.toDto(this.productService.saveProduct(productEntity)),
                HttpStatus.CREATED);
    }
    //Afficher tous les produits
    @Operation(summary = "Afficher tous les produits")
    @GetMapping
    @JsonView(Views.Internal.class)
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        List<ProductDTO>productsDTO = this.productService.getAllProducts().stream().map(productMapper::toDto).toList();
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
    @PatchMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) {
        Product p = this.productMapper.toEntity(product);
        ProductDTO productDTO = this.productMapper.toDto(this.productService.updateProduct(id, p));
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    //Supprimer un produit
    @Operation(summary = "Supprimer un produit")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
