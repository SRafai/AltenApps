package com.alten.dev.productsapp.it.controllers;

import com.alten.dev.productsapp.controllers.ProductsController;
import com.alten.dev.productsapp.dto.ProductDTO;
import com.alten.dev.productsapp.entities.Product;
import com.alten.dev.productsapp.enums.InventoryStatusEnum;
import com.alten.dev.productsapp.mapper.ProductMapper;
import com.alten.dev.productsapp.services.ProductService;
import com.alten.dev.productsapp.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {ProductsControllerIT.class, ProductsController.class, ProductDTO.class})
@ImportAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@EnableWebMvc
public class ProductsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    private ProductDTO productDTO;
    private Product productEntity;
    private final String URL = "/api/products";

    @BeforeEach
    public void setUp() {
        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setCode("P001");
        productDTO.setName("Test Product");
        productDTO.setDescription("Product description");
        productDTO.setCategory("Category");
        productDTO.setPrice(100.0);
        productDTO.setQuantity(10);
        productDTO.setInventoryStatus(InventoryStatusEnum.INSTOCK);
        productDTO.setRating(4.5);

        productEntity = new Product();
        productEntity.setId(1L);
        productEntity.setCode("P001");
        productEntity.setName("Test Product");
        productEntity.setDescription("Product description");
        productEntity.setCategory("Category");
        productEntity.setPrice(100.0);
        productEntity.setQuantity(10);
        productEntity.setInventoryStatus(InventoryStatusEnum.INSTOCK);
        productEntity.setRating(4.5);
    }

    @Test
    public void testCountProducts() throws Exception {
        when(productService.countProducts()).thenReturn(1L);

        mockMvc.perform(get(URL+"/count"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProduct() throws Exception {
        when(productService.getProduct(1L)).thenReturn(productEntity);
        when(productMapper.toDto(productEntity)).thenReturn(productDTO);

        mockMvc.perform(get(URL+"/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("P001"))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }


    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete(URL+"/1"))
                .andExpect(status().isNoContent());
    }
}
