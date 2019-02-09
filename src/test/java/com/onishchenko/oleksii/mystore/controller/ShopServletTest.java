package com.onishchenko.oleksii.mystore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

class ShopServletTest {
    private ShopServlet instance;

    private HttpServletRequest req;

    private HttpServletResponse resp;

    @BeforeEach
    void setUp() {
        instance = new ShopServlet();
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
    }

    @Test
    void success() throws ServletException, IOException {
        //Given
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        String resourcePath = "/WEB-INF/view/html/shop.html";
        when(req.getRequestDispatcher(resourcePath))
                .thenReturn(dispatcher);

        //When
        instance.doGet(req, resp);

        //Then
        verify(req, times(1)).getRequestDispatcher(resourcePath);
        verify(dispatcher, times(1)).forward(req, resp);
        verifyNoMoreInteractions(req, resp, dispatcher);
    }
}