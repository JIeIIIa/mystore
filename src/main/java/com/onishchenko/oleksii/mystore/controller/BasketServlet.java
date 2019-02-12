package com.onishchenko.oleksii.mystore.controller;

import com.onishchenko.oleksii.mystore.dto.ValidatedProductDto;
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
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * Products validation servlet
 */
@WebServlet(urlPatterns = "/shop/basket")
public class BasketServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(BasketServlet.class);

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
        log.info("Initialize {}", BasketServlet.class);
        productService = ProductServiceFactory.getInstance();
    }

    /**
     * Processing GET request to '/shop/basket'
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String items = req.getParameter("items");
        if (nonNull(items)) {
            log.debug("Validating products...");
            List<ValidatedProductDto> products = productService.validatedProducts(items.split(","));
            req.setAttribute("products", products);
        } else {
            log.debug("Parameter 'items' is null");
        }
        req.setAttribute("checkboxVisibility", false);

        req.getServletContext().getRequestDispatcher("/WEB-INF/view/jsp/basket.jsp")
                .forward(req, resp);
    }
}
