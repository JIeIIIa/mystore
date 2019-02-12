package com.onishchenko.oleksii.mystore.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The service to display information about success purchases
 */
@WebServlet(urlPatterns = "/shop/success")
public class SuccessServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(SuccessServlet.class);

    /**
     * Processing GET request to 'shop/success'
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Load success.html");
        req.getRequestDispatcher("/WEB-INF/view/html/success.html")
                .forward(req, resp);
    }
}
