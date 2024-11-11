package com.alten.dev.productsapp.repositories;

import com.alten.dev.productsapp.entities.Product;
import com.alten.dev.productsapp.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setQuantity(5);
    }
    @Test
    void testSaveProductHappy(){
        when(this.productRepository.save(any(Product.class))).thenReturn(product);
        this.productService.saveProduct(product);
        verify(this.productRepository).save(product);
    }
    @Test
    void testSaveProductBad(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            this.productService.saveProduct(null);
        });
        assertEquals("Product cannot be null", exception.getMessage());
    }
    @Test
    void testUpdateProductHappy(){
        Product updated = Product.builder().name("P2").quantity(1).price(20).build();
        when(this.productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(this.productRepository.save(any(Product.class))).thenReturn(updated);
        Product p = this.productService.updateProduct(1L, updated);
        assertEquals("P2", p.getName());
        assertEquals(1, p.getQuantity());
        assertEquals(20.0, p.getPrice());
        verify(this.productRepository).save(any(Product.class));

    }
    @Test
     void testUpdateProductBad(){
        when(this.productRepository.findById(anyLong())).thenReturn(Optional.empty());
        Product p  = this.productService.updateProduct(1L, product);
        assertNull(p);
        verify(this.productRepository, times(0)).save(any(Product.class));
    }
    @Test
    void testGetProductByIdHappy(){
        when(this.productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product p = this.productService.getProduct(1L);
        assertEquals("Test Product", p.getName());
        assertEquals(1L, p.getId());
        verify(this.productRepository).findById(1L);
    }
    @Test
    void testGetProductByIdBad(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            this.productService.getProduct(5L);
        });
        assertEquals("Product not found with ID: 5", exception.getMessage());
    }

    @Test
    void testGetAllProductsHappy(){
        Product p2 = Product.builder().id(2L).name("Test Product 2").build();

        when(this.productRepository.findAll()).thenReturn(List.of(product, p2));

        List<Product> products = this.productService.getAllProducts();

        assertEquals(2, products.size());
        verify(this.productRepository).findAll();
    }
    @Test
    void testGetAllProductsBad(){
        when(this.productRepository.findAll()).thenReturn(new ArrayList<>());

        List<Product> products = this.productService.getAllProducts();

        assertEquals(0, products.size());

        verify(this.productRepository).findAll();
    }

    @Test
    void testDeleteProductHappy(){
        when(this.productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        this.productService.deleteProduct(9L);
        verify(this.productRepository, times(1)).delete(any(Product.class));
    }
    @Test
    void testDeleteProductBad(){
        when(this.productRepository.findById(anyLong())).thenReturn(Optional.empty());
        this.productService.deleteProduct(6L);
        verify(this.productRepository, times(0)).delete(any(Product.class));
    }
}
