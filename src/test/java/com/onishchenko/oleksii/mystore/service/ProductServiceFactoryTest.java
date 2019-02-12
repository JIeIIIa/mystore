package com.onishchenko.oleksii.mystore.service;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceFactoryTest {
    @Test
    void getInstance() {
        //When
        ProductService productService = ProductServiceFactory.getInstance("path");

        //Then
        assertThat(productService).isNotNull()
                .isInstanceOf(ProductServiceImpl.class);
    }

    @Test
    void defaultGetInstance() {
        //When
        ProductService productService = ProductServiceFactory.getInstance();

        //Then
        assertThat(productService).isNotNull()
                .isInstanceOf(ProductServiceImpl.class);
    }

    @Test
    void defaultDataPath() {
        //Given
        Path expected = Paths.get("/data");

        //When
        Path dataPath = ProductServiceFactory.getDataPath();

        //Then
        assertThat(dataPath).isEqualTo(expected);
    }

    @Test
    void dataPath() {
        //Given
        System.setProperty("catalina.home", "/somePath");
        String expected = "/somePath/data";

        //When
        Path dataPath = ProductServiceFactory.getDataPath();

        //Then
        assertThat(dataPath).isEqualTo(Paths.get(expected));
    }
}