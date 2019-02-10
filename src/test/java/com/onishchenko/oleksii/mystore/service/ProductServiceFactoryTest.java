package com.onishchenko.oleksii.mystore.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceFactoryTest {
    @Test
    void getInstance() {
        //Given

        //When
        ProductService productService = ProductServiceFactory.getInstance("path");

        //Then
        assertThat(productService).isNotNull()
                .isInstanceOf(ProductServiceImpl.class);
    }
}