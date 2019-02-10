package com.onishchenko.oleksii.mystore.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void equals() {
        EqualsVerifier.forClass(Product.class)
                .usingGetClass()
                .verify();
    }

    @Test
    void toStringTest() {
        //Given
        Product product = new Product("qwerty", "product", 12345);
        String expected = "Product{vendorCode='qwerty', name='product', price=123.45}";

        //When
        String result = product.toString();

        //Then
        assertThat(result).isEqualTo(expected);
    }
}