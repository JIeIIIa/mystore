package com.onishchenko.oleksii.mystore.controller;

import com.onishchenko.oleksii.mystore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BuyServiceServletTest {

    private static final String ITEMS = "items";

    private HttpServletRequest request;

    private HttpServletResponse response;

    private BuyServiceServlet instance;

    private static Stream<String> invalidParameters() {
        return Stream.of(null, "");
    }

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        instance = new BuyServiceServlet();
    }

    @ParameterizedTest
    @MethodSource(value = "invalidParameters")
    void invalidParameter(String items) throws ServletException, IOException {
        //Given
        when(request.getParameter(ITEMS)).thenReturn(items);

        //When
        instance.doPost(request, response);

        //Then
        verify(response, times(1)).setStatus(400);
        verifyNoMoreInteractions(response);
    }


    @Test
    void init() throws ServletException {
        //When
        instance.init();

        //Then
        assertThat(instance.getProductService()).isNotNull();
    }

    @Nested
    class Buy {
        private ProductService productService;
        private String items;
        private String[] expectedItems;

        @BeforeEach
        void setUp() {
            items = "item1,item2,item3";
            expectedItems = new String[]{"item1", "item2", "item3"};
            productService = mock(ProductService.class);
            instance.setProductService(productService);
            when(request.getParameter(ITEMS)).thenReturn(items);
        }

        @Test
        void success() throws ServletException, IOException {
            //Given
            when(productService.buy(any())).thenReturn(true);

            //When
            instance.doPost(request, response);

            //Then
            verify(response, times(1)).setStatus(201);
            verify(productService, times(1)).buy(expectedItems);
            verifyNoMoreInteractions(response, productService);
        }

        @Test
        void failure() throws ServletException, IOException {
            //Given
            when(productService.buy(any())).thenReturn(false);

            //When
            instance.doPost(request, response);

            //Then
            verify(response, times(1)).setStatus(400);
            verify(productService, times(1)).buy(expectedItems);
            verifyNoMoreInteractions(response, productService);

        }
    }
}