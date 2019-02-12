package com.onishchenko.oleksii.mystore.controller;

import com.onishchenko.oleksii.mystore.dto.ValidatedProductDto;
import com.onishchenko.oleksii.mystore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ItemsServletTest {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ItemsServlet instance;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        instance = new ItemsServlet();
    }

    @Test
    void init() throws ServletException {
        //When
        instance.init();

        //Then
        assertThat(instance.getProductService()).isNotNull();
    }

    @Test
    void success() throws ServletException, IOException {
        //Given
        ProductService productService = mock(ProductService.class);
        final List<ValidatedProductDto> products = asList(
                new ValidatedProductDto("1", null),
                new ValidatedProductDto("2", null)
        );
        final List<ValidatedProductDto> expectedProducts = asList(
                new ValidatedProductDto("1", null),
                new ValidatedProductDto("2", null)
        );
        instance.setProductService(productService);
        when(productService.findAll()).thenReturn(products);

        ServletContext servletContext = mock(ServletContext.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(any())).thenReturn(dispatcher);

        //When
        instance.doGet(request, response);

        //Then
        verify(request, times(1)).setAttribute(eq("products"), eq(expectedProducts));
        verify(request, times(1)).setAttribute("checkboxVisibility", true);
        verify(productService, times(1)).findAll();
        verify(servletContext, times(1)).getRequestDispatcher("/WEB-INF/view/jsp/products.jsp");
        verifyNoMoreInteractions(response, productService);
    }
}