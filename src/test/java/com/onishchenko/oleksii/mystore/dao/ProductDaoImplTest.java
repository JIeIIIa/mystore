package com.onishchenko.oleksii.mystore.dao;

import com.onishchenko.oleksii.mystore.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductDaoImplTest {

    private ProductDaoImpl instance;

    @BeforeEach
    void setUp() {
        instance = new ProductDaoImpl();
        instance.updateProducts(asList(
                new Product("123-456-789", "awesomeProduct", 7654321),
                new Product("222-222-222", "toy", 22),
                new Product("333-333-333", "car", 333)
        ));
    }

    @DisplayName(value = "Optional<Product> findByVendor(String)")
    @Nested
    class FindByVendor {
        @Test
        void success() {
            //Given
            Product expected = new Product("123-456-789", "awesomeProduct", 7654321);

            //When
            Optional<Product> result = instance.findByVendor("123-456-789");

            //Then
            assertThat(result).isPresent()
                    .get()
                    .isEqualTo(expected);
        }

        @Test
        void notFound() {
            //When
            Optional<Product> result = instance.findByVendor("absent-vendor-code");

            //Then
            assertThat(result).isNotPresent();
        }

        @Test
        void whenVendorCodeIsNull() {
            //When
            assertThrows(IllegalArgumentException.class, () -> instance.findByVendor(null));
        }

        @Test
        void productsListContainsSeveralProductsWithTheSameVendorCode() {
            //Given
            instance.updateProducts(asList(
                    new Product("123-456-789", "awesomeProduct", 7654321),
                    new Product("123-456-789", "toy", 22),
                    new Product("123-456-789", "car", 333)
            ));
            Product expected = new Product("123-456-789", "awesomeProduct", 7654321);

            //When
            Optional<Product> result = instance.findByVendor("123-456-789");

            //Then
            assertThat(result).isPresent()
                    .get()
                    .isEqualTo(expected);
        }
    }

    @Test
    void findAll() {
        //Given
        Product[] expected = {
                new Product("123-456-789", "awesomeProduct", 7654321),
                new Product("222-222-222", "toy", 22),
                new Product("333-333-333", "car", 333)};

        //When
        List<Product> result = instance.findAll();

        //Then
        assertThat(result).containsExactly(expected);
        assertThrows(UnsupportedOperationException.class, () -> result.add(new Product("", "", 0)));
        assertThrows(UnsupportedOperationException.class, () -> result.remove(0));
    }

    @DisplayName(value = "void updateProducts(List<Product>)")
    @Test
    void updateProducts() {
        //Given
        Product[] expected = {
                new Product("11111", "first", 11111),
                new Product("22222", "second", 22222),
                new Product("33333", "third", 33333)};

        //When
        instance.updateProducts(asList(
                new Product("11111", "first", 11111),
                new Product("22222", "second", 22222),
                new Product("33333", "third", 33333)));
        List<Product> result = instance.findAll();

        //Then
        assertThat(result).containsExactly(expected);
    }
}