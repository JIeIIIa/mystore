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
 * Servlet to display errors
 * */
@WebServlet(urlPatterns = "/shop/failure")
public class FailureServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(FailureServlet.class);

    /**
     * Processing GET request to '/shop/failure'
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Load failure.html");
        req.getRequestDispatcher("/WEB-INF/view/html/failure.html")
                .forward(req, resp);
    }
}
