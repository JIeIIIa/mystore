package com.onishchenko.oleksii.mystore.controller;

import com.onishchenko.oleksii.mystore.dto.ValidatedProductDto;
import com.onishchenko.oleksii.mystore.service.ProductService;
import com.onishchenko.oleksii.mystore.service.ProductServiceFactory;
import com.onishchenko.oleksii.mystore.utils.ProductUpdaterScheduledExecutorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Servlet to display all products
 */
@WebServlet(urlPatterns = "/shop/items", loadOnStartup = 0)
public class ItemsServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(ItemsServlet.class);

    /**
     * The period to update the product list
     */
    private static final int PRODUCT_UPDATE_PERIOD = 5;

    /**
     * The time duration for PRODUCT_UPDATE_PERIOD
     */
    private static final TimeUnit PRODUCT_UPDATE_TIME_UNIT = TimeUnit.MINUTES;

    /**
     * The filename with information about all products
     */
    private static final String PRODUCTS_CSV = "products.csv";

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
        log.info("Initialize {}", ItemsServlet.class);

        productService = ProductServiceFactory.getInstance();
        Path path = ProductServiceFactory.getDataPath().resolve(PRODUCTS_CSV);
        log.debug("Path to the file with products data was resolved: {}", path.toString());

        ProductUpdaterScheduledExecutorService executorService =
                new ProductUpdaterScheduledExecutorService(productService, path.toString());
        executorService.scheduleAtFixedRate(PRODUCT_UPDATE_PERIOD, PRODUCT_UPDATE_TIME_UNIT);
        log.debug("Scheduled task to update products every {} {} was created", PRODUCT_UPDATE_PERIOD, PRODUCT_UPDATE_TIME_UNIT);
    }

    /**
     * Processing GET request to '/shop/items'
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Load all products");
        List<ValidatedProductDto> products = productService.findAll();
        req.setAttribute("products", products);
        req.setAttribute("checkboxVisibility", true);

        req.getServletContext().getRequestDispatcher("/WEB-INF/view/jsp/products.jsp")
                .forward(req, resp);
    }
}
