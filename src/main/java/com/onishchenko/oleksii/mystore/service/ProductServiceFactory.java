package com.onishchenko.oleksii.mystore.service;


import com.onishchenko.oleksii.mystore.dao.ProductDaoFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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

    /**
     * Returns path to 'data' directory in the Tomcat directory or default path
     *
     * @return path to 'data' directory in the Tomcat directory if the 'catalina.home'
     * system property is set; <code>Paths.get("/data")</code> otherwise
     * */
    public static Path getDataPath() {
        Path tomcatPath = Paths.get(
                Optional.ofNullable(System.getProperty("catalina.home")).orElse("/")
        );
        return tomcatPath.resolve("data");
    }

    /**
     * Returns a {@link ProductServiceImpl} instance with default settings
     *
     * @return a {@link ProductServiceImpl} instance with default settings
     */
    public static ProductService getInstance() {
        return new ProductServiceImpl(ProductDaoFactory.singleton(), getDataPath().toString());
    }
}
