package com.onishchenko.oleksii.mystore.utils;

import com.onishchenko.oleksii.mystore.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

/**
 * Service to manage scheduled tasks
 */
public class ProductUpdaterScheduledExecutorService {
    private static final Logger log = LogManager.getLogger(ProductUpdaterScheduledExecutorService.class);

    /**
     * The service to manage products and purchases
     */
    private ProductService productService;

    /**
     * The filename with information about all products
     */
    private String filename;

    public ProductUpdaterScheduledExecutorService(ProductService productService, String filename) {
        log.info("Create instance of {}", ProductUpdaterScheduledExecutorService.class);
        this.productService = productService;
        this.filename = filename;
    }

    /**
     * Create and run scheduled task to update products list
     * at fixed rate
     *
     * @param period the period to update the product list
     * @param unit   the time duration for period to update
     * @throws IllegalArgumentException if <code>period</code> < 0
     *                                  or <code>unit</code> is null
     */
    public void scheduleAtFixedRate(long period, TimeUnit unit) {
        if (period < 0) {
            throw new IllegalArgumentException("Illegal argument period: " + period);
        }
        if (isNull(unit)) {
            throw new IllegalArgumentException("Illegal argument unit: null");
        }
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(
                () -> productService.loadProducts(filename), 0, period, unit);
    }
}
