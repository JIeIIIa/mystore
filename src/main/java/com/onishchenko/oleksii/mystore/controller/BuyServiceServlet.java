package com.onishchenko.oleksii.mystore.controller;

import com.onishchenko.oleksii.mystore.service.ProductService;
import com.onishchenko.oleksii.mystore.service.ProductServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.isNull;

/**
 * Purchase processing servlet
 */
@WebServlet(urlPatterns = "/shop/buyService")
public class BuyServiceServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(BuyServiceServlet.class);

    /**
     * The service to manage products and purchases
     */
    private ProductService productService;

    ProductService getProductService() {
        return productService;
    }

    void setProductService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Set properties, do be used by subsequent doGet() calls
     */
    @Override
    public void init() throws ServletException {
        log.info("Initialize {}", BuyServiceServlet.class);
        productService = ProductServiceFactory.getInstance();
    }

    /**
     * Processing POST request to '/shop/buyService'
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Buying...");
        String items = req.getParameter("items");
        if (isNull(items) || "".equals(items)) {
            log.error("Parameter 'items' is null or empty");
            resp.setStatus(400);
            return;
        }
        boolean validationResult = productService.buy(items.split(","));
        if (validationResult) {
            resp.setStatus(201);
            log.debug("Purchase was success");
        } else {
            resp.setStatus(400);
            log.debug("Purchase was failure");
        }
    }
}
