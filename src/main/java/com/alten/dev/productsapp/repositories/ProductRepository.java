package com.alten.dev.productsapp.repositories;

import com.alten.dev.productsapp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT COUNT(p) FROM Product p")
    long countAllProducts();
}
