package com.alten.dev.productsapp.controllers;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.jupiter.api.Assertions.*;

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
        Mockito.when(productService.getAllProducts()).thenReturn(productList);
        Mockito.when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        ResponseEntity<List<ProductDTO>> response = productsController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
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

        Mockito.when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(product);
        Mockito.when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(product);
        Mockito.when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productsController.updateProduct(1L, productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("P123", response.getBody().getCode());
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() {
        Mockito.doNothing().when(productService).deleteProduct(anyLong());

        ResponseEntity<Void> response = productsController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
