package com.onishchenko.oleksii.mystore.service;


import com.onishchenko.oleksii.mystore.dao.ProductDaoFactory;

/**
 * This class provides a factory for creating an Product Service instance.
 */
public class ProductServiceFactory {
    /**
     * Returns a {@link ProductServiceImpl} instance
     *
     * @param directoryPathToSaveOrders a directory path to save all orders
     * @return a {@link ProductServiceImpl} instance
     */
    public static ProductService getInstance(String directoryPathToSaveOrders) {
        return new ProductServiceImpl(ProductDaoFactory.singleton(), directoryPathToSaveOrders);
    }
}
