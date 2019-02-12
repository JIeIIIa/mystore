package com.onishchenko.oleksii.mystore.service;

import com.onishchenko.oleksii.mystore.dto.ValidatedProductDto;

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
    List<ValidatedProductDto> findAll();

    /**
     * Validate vendor codes by available products
     *
     * @param vendorCodes a list of vendor codes for validation
     * @return <code>true</code> if and only if all vendor code
     * are valid; <code>false</code> otherwise.
     */
    boolean validateAll(String... vendorCodes);

    /**
     * Validate vendor codes by available products
     *
     * @param vendorCodes a list of vendor codes for validation
     * @return list of {@link ValidatedProductDto} objects. Each element
     * of this list encapsulates a vendor code and product with the vendor code
     * if the vendor code presents in the product list$
     * encapsulates a vendor code and <code>null</code> otherwise
     */
    List<ValidatedProductDto> validatedProducts(String... vendorCodes);

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
