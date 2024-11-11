package com.alten.dev.productsapp.services;

import com.alten.dev.productsapp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);

    Product updateProduct(Long productId, Product product);

    Product getProduct(Long productId);

    List<Product> getAllProducts();

    void deleteProduct(Long productId);
    Page<Product> getPaginatedProducts(Pageable pageable);
    long countProducts();
}
