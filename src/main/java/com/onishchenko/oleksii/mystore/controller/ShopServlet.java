package com.onishchenko.oleksii.mystore.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/shop")
public class ShopServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(ShopServlet.class);

    /**
     * Processing GET request to '/shop'
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Load shop.html");
        req.getRequestDispatcher("/WEB-INF/view/html/shop.html")
                .forward(req, resp);
    }
}
