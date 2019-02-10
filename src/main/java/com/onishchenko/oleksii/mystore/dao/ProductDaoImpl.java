package com.onishchenko.oleksii.mystore.dao;

import com.onishchenko.oleksii.mystore.entity.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Objects.isNull;


/**
 * The Product Data Access Object Implementation is the implementation of The Product Data Access Object
 * This class providing access to products that are stored in the list.
 * The ProductDaoImpl class provides thread-safe data access.
 *
 * @see com.onishchenko.oleksii.mystore.dao.ProductDao
 */
public class ProductDaoImpl implements ProductDao {

    private static final Logger log = LogManager.getLogger(ProductDaoImpl.class);

    /**
     * The list of stored products
     */
    private List<Product> products = new CopyOnWriteArrayList<>();

    /**
     * Find the first product by the vendor code
     *
     * @param vendorCode the vendor code
     * @return an {@code Optional} describing the first product for
     * which the vendor code is equals to the specified parameter,
     * or an empty {@code Optional} if the product is not found
     * @throws IllegalArgumentException if the vendor code is null
     */
    @Override
    public Optional<Product> findByVendor(String vendorCode) {
        if (isNull(vendorCode)) {
            throw new IllegalArgumentException("Illegal vendor: null");
        }
        return products.stream().filter(p -> vendorCode.equals(p.getVendorCode()))
                .findFirst();
    }

    /**
     * Find all products
     *
     * @return The unmodifiable list of products
     */
    @Override
    public List<Product> findAll() {
        return Collections.unmodifiableList(products);
    }

    /**
     * Replaces the list of products with a new one.
     * Before replacing the list is validated.
     * If the list contains duplicate vendor codes, a warning is displayed
     *
     * @param products A new list of products.
     */
    @Override
    public void updateProducts(List<Product> products) {
        this.products.clear();
        verify(products);
        this.products.addAll(products);
        log.info("The products list was updated.");
    }

    /**
     * Validate the list of products: all vendor codes in the list must be unique
     * If the list contains duplicate vendor codes, a warning is displayed
     *
     * @param products A list of products.
     */
    private void verify(List<Product> products) {
        if (products.size() != uniqueVendorCodesCount(products)) {
            log.warn("The products list contains elements with the same vendor code.");
        }
    }

    /**
     * The number of unique vendor codes
     *
     * @param products A list of products.
     * @return the number of unique vendor codes
     */
    private long uniqueVendorCodesCount(List<Product> products) {
        return products.stream()
                .map(Product::getVendorCode)
                .distinct()
                .count();
    }

}

