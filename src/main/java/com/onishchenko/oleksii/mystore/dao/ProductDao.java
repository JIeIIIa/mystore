package com.onishchenko.oleksii.mystore.dao;

import com.onishchenko.oleksii.mystore.entity.Product;

import java.util.List;
import java.util.Optional;

/**
 * The Product Data Access Object is the interface providing access to products
 */
public interface ProductDao {
    /**
     * Find the first product by the vendor code
     *
     * @param vendorCode the vendor code
     * @return an {@code Optional} describing the first product for
     * which the vendor code is equals to the specified parameter,
     * or an empty {@code Optional} if the product is not found
     * @throws IllegalArgumentException if the vendor code is null
     */
    Optional<Product> findByVendor(String vendorCode);

    /**
     * Find all products
     *
     * @return The list of products
     */
    List<Product> findAll();

    /**
     * Replaces the list of products with a new one.
     *
     * @param products A new list of products.
     */
    void updateProducts(List<Product> products);
}
