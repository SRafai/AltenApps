package com.alten.dev.productsapp.services.impl;

import com.alten.dev.productsapp.dto.ProductDTO;
import com.alten.dev.productsapp.entities.Product;
import com.alten.dev.productsapp.repositories.ProductRepository;
import com.alten.dev.productsapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    @Autowired
    ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Product saveProduct(Product product) {
        if(product == null){
            throw new IllegalArgumentException("Product cannot be null");
        }
        return this.productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        Optional<Product> optionalOldProduct = productRepository.findById(productId);
        if (optionalOldProduct.isPresent()) {
            Product updatedProduct = optionalOldProduct.get();
            updatedProduct.setName(product.getName());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setQuantity(product.getQuantity());
            return productRepository.save(updatedProduct);
        }
        return null;
    }

    @Override
    public Product getProduct(Long productId) {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public void deleteProduct(Long productId) {
        Optional<Product> product = this.productRepository.findById(productId);
        product.ifPresent(this.productRepository::delete);
    }
    @Override
    public Page<Product> getPaginatedProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public long countProducts() {
        return productRepository.countAllProducts();
    }
}
