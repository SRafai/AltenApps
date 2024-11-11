package com.alten.dev.productsapp.ut.controllers;

import com.alten.dev.productsapp.controllers.ProductsController;
import com.alten.dev.productsapp.dto.ProductDTO;
import com.alten.dev.productsapp.entities.Product;
import com.alten.dev.productsapp.enums.InventoryStatusEnum;
import com.alten.dev.productsapp.mapper.ProductMapper;
import com.alten.dev.productsapp.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class ProductsControllerTest {

    @InjectMocks
    private ProductsController productsController;

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    private ProductDTO productDTO;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productDTO = new ProductDTO();
        productDTO.setCode("P123");
        productDTO.setName("Product 123");
        productDTO.setDescription("A sample product");
        productDTO.setCategory("Category 1");
        productDTO.setPrice(99.99);
        productDTO.setQuantity(10);
        productDTO.setInventoryStatus(InventoryStatusEnum.INSTOCK);
        productDTO.setRating(4.5);

        product = new Product();
        product.setCode("P123");
        product.setName("Product 123");
        product.setDescription("A sample product");
        product.setCategory("Category 1");
        product.setPrice(99.99);
        product.setQuantity(10);
        product.setInventoryStatus(InventoryStatusEnum.INSTOCK);
        product.setRating(4.5);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {

        Mockito.when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(product);
        Mockito.when(productService.saveProduct(any(Product.class))).thenReturn(product);
        Mockito.when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productsController.createProduct(productDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("P123", response.getBody().getCode());
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() {

        List<Product> productList = Arrays.asList(product);

        // Create a Page with one product
        Page<Product> productPage = new PageImpl<>(productList, PageRequest.of(0, 10), 1);

        // Mocking productService and productMapper
        Mockito.when(productService.getPaginatedProducts(PageRequest.of(0, 10)))
                .thenReturn(productPage);
        Mockito.when(productMapper.toDto(any(Product.class)))
                .thenReturn(productDTO);

        // When
        ResponseEntity<List<ProductDTO>> response = productsController.getAllProductsP(0, 10);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody()); // Ensure response body is not null
        assertEquals(1, response.getBody().size()); // Check if the size is 1 (one product)
        assertEquals("P123", response.getBody().get(0).getCode());
    }

    @Test
    void getProduct_ShouldReturnProductById() {

        Mockito.when(productService.getProduct(anyLong())).thenReturn(product);
        Mockito.when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productsController.getProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("P123", response.getBody().getCode());
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() {
        // Given: Initialize product and productDTO with necessary values
        Product product = new Product();
        product.setId(1L); // Make sure to set an ID
        product.setCode("P123");
        product.setCreatedAt(new Date()); // Assuming you want to mock this
        product.setUpdatedAt(new Date());

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L); // Ensure the DTO has an ID
        productDTO.setCode("P123");

        // Mocking the service and mapper methods
        Mockito.when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(product);
        Mockito.when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(product);
        Mockito.when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        // When: Call the updateProduct method in the controller
        ResponseEntity<ProductDTO> response = productsController.updateProduct(1L, productDTO);

        // Then: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("P123", response.getBody().getCode()); // Assert that the product code is correct
    }


    @Test
    void deleteProduct_ShouldReturnNoContent() {
        Mockito.doNothing().when(productService).deleteProduct(anyLong());

        ResponseEntity<Void> response = productsController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
