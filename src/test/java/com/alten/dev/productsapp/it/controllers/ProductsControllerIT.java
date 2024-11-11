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
    public void testCreateProduct() throws Exception {
        when(productMapper.toEntity(productDTO)).thenReturn(productEntity);
        when(productService.saveProduct(productEntity)).thenReturn(productEntity);
        when(productMapper.toDto(productEntity)).thenReturn(productDTO);

        String body = "{\"code\":\"P001\",\"name\":\"Test Product\",\"description\":\"Product description\",\"category\":\"Category\",\"price\":100.0,\"quantity\":10,\"inventoryStatus\":\""+InventoryStatusEnum.INSTOCK+"\",\"rating\":4.5}";
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("P001"))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(100.0));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(productEntity));
        when(productMapper.toDto(productEntity)).thenReturn(productDTO);

        mockMvc.perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("P001"))
                .andExpect(jsonPath("$[0].name").value("Test Product"));
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
    public void testUpdateProduct() throws Exception {
        productDTO.setId(1L);
        productDTO.setCreatedAt(new Date()); // Set createdAt to the current date
        productDTO.setUpdatedAt(new Date()); // Set updatedAt to the current date

        when(productMapper.toEntity(productDTO)).thenReturn(productEntity);
        when(productService.updateProduct(1L, productEntity)).thenReturn(productEntity);
        when(productMapper.toDto(productEntity)).thenReturn(productDTO);

        String body = "{\"id\":1,\"code\":\"P001\",\"name\":\"Updated Product\",\"description\":\"Updated description\",\"category\":\"Category\",\"price\":120.0,\"quantity\":15,\"inventoryStatus\":\"INSTOCK\",\"rating\":4.8}";

        mockMvc.perform(patch(URL+"/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(120.0));
    }


    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete(URL+"/1"))
                .andExpect(status().isNoContent());
    }
}
