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
 * The service to display start page
 */
@WebServlet(urlPatterns = "")
public class WelcomeServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(WelcomeServlet.class);

    /**
     * Processing GET request to '/'
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Load welcome page: home.html");
        req.getRequestDispatcher("/WEB-INF/view/html/home.html")
                .forward(req, resp);
    }
}
