package com.onishchenko.oleksii.mystore.dto;

import com.onishchenko.oleksii.mystore.entity.Product;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatedProductDtoTest {

    @Test
    void equals() {
        EqualsVerifier.forClass(ValidatedProductDto.class)
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void withProduct() {
        //When
        ValidatedProductDto instance =
                new ValidatedProductDto("code", new Product("code", "name", 123));


        //Then
        assertThat(instance.isValid()).isTrue();
        assertThat(instance.getVendorCode()).isEqualTo("code");
        assertThat(instance.getProduct()).isEqualTo(
                new Product("code", "name", 123)
        );
    }

    @Test
    void noProduct() {
        //When
        ValidatedProductDto instance = new ValidatedProductDto("code", null);

        //Then
        assertThat(instance.isValid()).isFalse();
        assertThat(instance.getVendorCode()).isEqualTo("code");
        assertThat(instance.getProduct()).isNull();
    }
}