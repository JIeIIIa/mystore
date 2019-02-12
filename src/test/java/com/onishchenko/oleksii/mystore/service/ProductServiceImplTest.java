package com.onishchenko.oleksii.mystore.service;

import com.onishchenko.oleksii.mystore.dao.ProductDao;
import com.onishchenko.oleksii.mystore.dto.ValidatedProductDto;
import com.onishchenko.oleksii.mystore.entity.Product;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {
    private static final String PATH_TO_SAVE_ORDERS = "D:/Temp/mystore";

    private ProductDao productDao;

    private ProductServiceImpl instance;

    @BeforeEach
    void setUp() {
        productDao = mock(ProductDao.class);
        instance = new ProductServiceImpl(productDao, PATH_TO_SAVE_ORDERS);
    }

    @DisplayName(value = "boolean validateAll(String...)")
    @Nested
    class ValidateAll {
        @Test
        void parameterIsEmptyArray() {
            //When
            assertThrows(IllegalArgumentException.class, () -> instance.validateAll());
        }

        @Test
        void success() {
            //Given
            for (int i = 1; i < 10; i++) {
                when(productDao.findByVendor("vc-" + i)).thenReturn(
                        Optional.of(new Product("vc-" + i, "name_" + i, i * i * i))
                );
            }

            //When
            boolean result = instance.validateAll("vc-4", "vc-1", "vc-7");

            //Then
            assertThat(result).isTrue();
        }


        @Test
        void vendorCodeNotPresent() {
            //Given
            when(productDao.findAll()).thenReturn(
                    IntStream.range(1, 10)
                            .boxed()
                            .map(i -> new Product("vc-" + i, "name_" + i, i * i * i))
                            .collect(toList()));

            //When
            boolean result = instance.validateAll("vc-4", "vc-111", "vc-10");

            //Then
            assertThat(result).isFalse();
        }
    }

    @DisplayName(value = "boolean validateAll(String...)")
    @Nested
    class ValidatedProducts {
        @Test
        void parameterIsEmptyArray() {
            //When
            List<ValidatedProductDto> result = instance.validatedProducts();

            //Then
            assertThat(result).isEmpty();
        }

        @Test
        void success() {
            //Given
            for (int i = 1; i < 10; i++) {
                when(productDao.findByVendor("vc-" + i)).thenReturn(
                        Optional.of(new Product("vc-" + i, "name_" + i, i * i * i))
                );
            }
            ValidatedProductDto[] expected = Stream.of(4, 1, 7)
                    .map(i -> new ValidatedProductDto(
                            "vc-" + i,
                            new Product("vc-" + i, "name_" + i, i * i * i))
                    ).toArray(ValidatedProductDto[]::new);

            //When
            List<ValidatedProductDto> result = instance.validatedProducts("vc-4", "vc-1", "vc-7");

            //Then
            assertThat(result).hasSize(3)
                    .containsOnlyOnce(expected);
        }


        @Test
        void vendorCodeNotPresent() {
            //Given
            when(productDao.findByVendor("vc-4"))
                    .thenReturn(Optional.of(new Product("vc-4", "name_4", 64)));
            when(productDao.findByVendor("vc-10"))
                    .thenReturn(Optional.of(new Product("vc-10", "name_10", 1000)));
            ValidatedProductDto[] expected = {
                    new ValidatedProductDto("vc-4", new Product("vc-4", "name_4", 64)),
                    new ValidatedProductDto("vc-111", null),
                    new ValidatedProductDto("vc-10", new Product("vc-10", "name_10", 1000))
            };

            //When
            List<ValidatedProductDto> result = instance.validatedProducts("vc-4", "vc-111", "vc-10");

            //Then
            assertThat(result).hasSize(3)
                    .containsOnlyOnce(expected);
        }
    }


    @Test
    void findAll() {
        //Given
        when(productDao.findAll()).thenReturn(
                IntStream.range(1, 10)
                        .boxed()
                        .map(i -> new Product("vc-" + i, "name_" + i, i * i * i))
                        .collect(toList()));

        //When
        List<ValidatedProductDto> result = instance.findAll();

        //Then
        assertThat(result).containsExactly(
                IntStream.range(1, 10)
                        .boxed()
                        .map(this::createValidatedProductDto)
                        .toArray(ValidatedProductDto[]::new)
        );
        verify(productDao, times(1)).findAll();
        verifyNoMoreInteractions(productDao);
    }

    private ValidatedProductDto createValidatedProductDto(Integer i) {
        return new ValidatedProductDto(
                "vc-" + i,
                new Product("vc-" + i, "name_" + i, i * i * i));
    }

    @DisplayName(value = "void saveOrder(List<Product>)")
    @Nested
    class Buy {
        @Test
        void parameterIsNull() {
            //When
            boolean result = instance.buy((String) null);

            //Then
            assertThat(result).isFalse();
        }

        @Test
        void parameterIsEmptyArray() {
            //When
            boolean result = instance.buy();

            //Then
            assertThat(result).isFalse();
            verifyNoMoreInteractions(productDao);
        }

        @Test
        void vendorCodeNotFound() {
            //Given    
            String[] vendorCodes = {"1-1-1", "2-2-2"};
            when(productDao.findByVendor("1-1-1"))
                    .thenReturn(Optional.of(new Product("1-1-1", "first", 12345)));
            when(productDao.findByVendor("2-2-2")).thenReturn(Optional.empty());

            //When
            boolean result = instance.buy(vendorCodes);

            //Then
            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"true", "false"})
        void buy(String saveResult) {
            //Given
            ProductServiceImpl spy = spy(instance);
            boolean expected = Boolean.getBoolean(saveResult);
            String[] vendorCodes = {"1-1-1", "2-2-2"};
            when(productDao.findByVendor("1-1-1"))
                    .thenReturn(Optional.of(new Product("1-1-1", "first", 12345)));
            when(productDao.findByVendor("2-2-2"))
                    .thenReturn(Optional.of(new Product("2-2-2", "second", 7654321)));
            doReturn(expected).when(spy).saveOrder(any());

            //When
            boolean result = instance.buy(vendorCodes);

            //Then
            assertThat(result).isEqualTo(expected);
        }
    }

    @DisplayName(value = "void saveOrder(List<Product>)")
    @Nested
    class SaveOrder {
        @Test
        void success(@TempDir Path tempDir) {
            //Given
            ProductServiceImpl spy = spy(instance);
            spy.setDirectoryPathToSaveOrders(tempDir.toString());
            List<Product> products = asList(
                    new Product("111-111-111", "first", 12345),
                    new Product("222-222-222", "second", 22222),
                    new Product("333-333-333", "third", 54321)
            );

            String filename = "order-success.csv";
            doReturn(filename).when(spy).generateFilename();
            Path expectedFile = tempDir.resolve(filename);

            //When
            boolean result = spy.saveOrder(products);

            //Then
            assertThat(result).isTrue();
            assertThat(Files.exists(expectedFile)).isTrue();
            assertThat(expectedFile).hasContent(
                    "\"111-111-111\",\"first\",\"123.45\"\n" +
                            "\"222-222-222\",\"second\",\"222.22\"\n" +
                            "\"333-333-333\",\"third\",\"543.21\""
            );
            verifyNoMoreInteractions(productDao);
        }

        @Test
        void throwIOException() throws IOException {
            //Given
            ProductServiceImpl spy = spy(instance);
            List<Product> products = singletonList(
                    new Product("111-111-111", "first", 12345)
            );

            String filename = "IOException.csv";

            doReturn(filename).when(spy).generateFilename();
            doThrow(IOException.class).when(spy).createCSVWriter(any(File.class));

            //When
            boolean result = spy.saveOrder(products);

            //Then
            assertThat(result).isFalse();
            verifyNoMoreInteractions(productDao);
        }
    }

    @Test
    void generateFilename() {
        //When
        String result = instance.generateFilename();

        //Then
        assertThat(result).containsPattern("order-\\d+.csv");
    }

    @DisplayName(value = "void loadProducts(String)")
    @Nested
    class LoadProducts {
        @Test
        void throwIOException() throws IOException {
            //Given
            ProductServiceImpl spy = spy(instance);
            doThrow(FileNotFoundException.class).when(spy).createCSVReader(any(String.class));

            //When
            spy.loadProducts("filename.csv");

            //Then
            verifyNoMoreInteractions(productDao);
        }


        @Test
        void success() throws IOException {
            //Given
            ProductServiceImpl spy = spy(instance);
            CSVReader reader = mock(CSVReader.class);
            doReturn(reader).when(spy).createCSVReader(any(String.class));
            doCallRealMethod().when(reader).forEach(any());
            when(reader.iterator()).thenReturn(asList(
                    new String[]{"name_0", "12345.4"},
                    new String[]{null, "name_1", "12345.4"},
                    new String[]{"", "name_2", "22222.22"},
                    new String[]{"vc-3", null, "333.33"},
                    new String[]{"vc-4", "", "4444.44"},
                    new String[]{"vc-5", "name_5", "value"},
                    new String[]{"vc-6", "name_6", "8765"},
                    new String[]{"vc-7", "name_7", "987.65"},
                    new String[]{"vc-8", "name_8", null}
                    ).iterator()
            );
            Product[] expected = {
                    new Product("vc-6", "name_6", 876500),
                    new Product("vc-7", "name_7", 98765)
            };

            //When
            spy.loadProducts("filename.csv");

            //Then
            @SuppressWarnings("unchecked")
            Class<List<Product>> listClass = (Class<List<Product>>) (Class) List.class;
            ArgumentCaptor<List<Product>> argument = ArgumentCaptor.forClass(listClass);
            verify(productDao).updateProducts(argument.capture());
            assertThat(argument.getValue()).containsExactly(expected);
            verifyNoMoreInteractions(productDao);
        }

        @Test
        void readRealData(@TempDir Path tempDir) throws IOException {
            //Given
            String filename = "products.csv";
            Path filePath = tempDir.resolve(filename);
            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath.toFile()))) {
                writer.writeNext(new String[]{"vc-6", "name_6", "8765"});
                writer.writeNext(new String[]{"vc-7", "name_7", "987.65"});
            }

            Product[] expected = {
                    new Product("vc-6", "name_6", 876500),
                    new Product("vc-7", "name_7", 98765)
            };

            //When
            instance.loadProducts(filePath.toString());

            //Then
            @SuppressWarnings("unchecked")
            Class<List<Product>> listClass = (Class<List<Product>>) (Class) List.class;
            ArgumentCaptor<List<Product>> argument = ArgumentCaptor.forClass(listClass);
            verify(productDao).updateProducts(argument.capture());
            assertThat(argument.getValue()).containsExactly(expected);
            verifyNoMoreInteractions(productDao);
        }
    }
}