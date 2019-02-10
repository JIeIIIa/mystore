package com.onishchenko.oleksii.mystore.dao;

/**
 * This class provides a factory for creating an Product Data Access Object.
 */
public class ProductDaoFactory {

    /**
     * Returns a singleton {@link ProductDaoImpl} instance
     *
     * @return a singleton {@link ProductDaoImpl} instance
     */
    public static ProductDao singleton() {
        return SingletonHelper.INSTANCE;
    }

    /**
     * When the {@link ProductDaoFactory} class is loaded,
     * SingletonHelper class is not loaded into memory and only
     * when someone calls {@link ProductDaoFactory#singleton()}, this class gets
     * loaded and creates the {@link ProductDaoImpl} class instance.
     * */
    private static class SingletonHelper {
        /**
         * Contains the instance of the {@link ProductDaoImpl} class
         * */
        private static final ProductDao INSTANCE = new ProductDaoImpl();
    }
}
