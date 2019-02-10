package com.onishchenko.oleksii.mystore.service;

import com.onishchenko.oleksii.mystore.entity.Product;

import java.util.List;

/**
 * The {@link ProductService} interface provides methods to manage products and purchases.
 *
 * @see ProductServiceImpl
 */
public interface ProductService {
    /**
     * Find all available products
     *
     * @return a list of available products
     */
    List<Product> findAll();

    /**
     * Validate vendor codes by available products
     *
     * @param vendorCodes a list of vendor codes for validation
     */
    boolean validateAll(String... vendorCodes);

    /**
     * Make a purchase based on vendor codes
     *
     * @param vendorCodes vendor codes of products that user wants to buy
     * @return <code>true</code> if and only if vendor codes is not an empty
     * list, all vendor codes is present in available products
     * <code>false</code> otherwise.
     */
    boolean buy(String... vendorCodes);

    /**
     * Loads products from a file with the filename and
     * update available products list.
     *
     * @param filename the filename to load data
     */
    void loadProducts(String filename);
}
