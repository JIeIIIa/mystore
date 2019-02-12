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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BasketServletTest {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private BasketServlet instance;
    private ServletContext servletContext;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        instance = new BasketServlet();

        servletContext = mock(ServletContext.class);
        dispatcher = mock(RequestDispatcher.class);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(any())).thenReturn(dispatcher);

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
        String items = "item1,item2";
        ProductService productService = mock(ProductService.class);
        final List<ValidatedProductDto> products = asList(
                new ValidatedProductDto("item1", null),
                new ValidatedProductDto("item2", null)
        );
        final List<ValidatedProductDto> expectedProducts = asList(
                new ValidatedProductDto("item1", null),
                new ValidatedProductDto("item2", null)
        );
        instance.setProductService(productService);
        when(request.getParameter("items")).thenReturn(items);
        when(productService.validatedProducts(any())).thenReturn(products);

        //When
        instance.doGet(request, response);

        //Then
        verify(productService, times(1)).validatedProducts("item1", "item2");
        verify(request, times(1)).setAttribute(eq("products"), eq(expectedProducts));
        verify(request, times(1)).setAttribute("checkboxVisibility", false);
        verify(servletContext, times(1)).getRequestDispatcher("/WEB-INF/view/jsp/basket.jsp");
        verifyNoMoreInteractions(response, productService);
    }

    @Test
    void whenParameterIsNull() throws ServletException, IOException {
        //Given
        ProductService productService = mock(ProductService.class);
        instance.setProductService(productService);
        when(request.getParameter("items")).thenReturn(null);

        //When
        instance.doGet(request, response);

        //Then
        verify(request, times(1)).setAttribute("checkboxVisibility", false);
        verify(servletContext, times(1)).getRequestDispatcher("/WEB-INF/view/jsp/basket.jsp");
        verifyNoMoreInteractions(response, productService);
    }
}