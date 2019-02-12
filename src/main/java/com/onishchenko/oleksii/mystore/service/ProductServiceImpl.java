package com.onishchenko.oleksii.mystore.service;

import com.onishchenko.oleksii.mystore.dao.ProductDao;
import com.onishchenko.oleksii.mystore.dto.ValidatedProductDto;
import com.onishchenko.oleksii.mystore.entity.Product;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

/**
 * The {@link ProductServiceImpl} class is the implementation of
 * the {@link ProductService} interface provides methods to manage products and purchases.
 *
 * @see ProductService
 */
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LogManager.getLogger(ProductServiceImpl.class);

    /**
     * A Product Data Access Object
     */
    private ProductDao productDao;

    /**
     * A directory path to save all orders
     */
    private String directoryPathToSaveOrders;

    /**
     * Constructs a new {@link ProductServiceImpl} with the specified a Product Data Access Object
     * and a directory path to save all orders.
     *
     * @param productDao                a Product Data Access Object
     * @param directoryPathToSaveOrders A directory path to save all orders
     */
    public ProductServiceImpl(ProductDao productDao, String directoryPathToSaveOrders) {
        log.info("Create instance of {}", ProductServiceImpl.class.getName());
        this.productDao = productDao;
        this.directoryPathToSaveOrders = directoryPathToSaveOrders;
    }

    /**
     * Update the directory path to save orders to the specified <code>directoryPathToSaveOrders</>
     *
     * @param directoryPathToSaveOrders new directory path to save orders
     */
    public void setDirectoryPathToSaveOrders(String directoryPathToSaveOrders) {
        this.directoryPathToSaveOrders = directoryPathToSaveOrders;
    }

    /**
     * Find all available products
     *
     * @return a list of available products as a Data Transfer Objects list
     */
    @Override
    public List<ValidatedProductDto> findAll() {
        return productDao.findAll()
                .stream()
                .map(p -> new ValidatedProductDto(p.getVendorCode(), p))
                .collect(toList());
    }

    /**
     * Validate vendor codes by available products
     *
     * @param vendorCodes a list of vendor codes for validation
     * @return <code>true</code> if and only if all vendor code
     * are valid; <code>false</code> otherwise.
     * @throws IllegalArgumentException if a list of vendor codes is empty
     */
    @Override
    public boolean validateAll(String... vendorCodes) {
        if (vendorCodes.length == 0) {
            throw new IllegalArgumentException("Illegal argument vendorCodes: The vendor codes must contain at least one value");
        }

        return vendorCodes.length == mapVendorCodesToProducts(vendorCodes).size();
    }

    /**
     * Validate vendor codes by available products
     *
     * @param vendorCodes a list of vendor codes for validation
     * @return list of {@link ValidatedProductDto} objects. Each element
     * of this list encapsulates a vendor code and product with the vendor code
     * if the vendor code presents in the product list$
     * encapsulates a vendor code and <code>null</code> otherwise
     */
    @Override
    public List<ValidatedProductDto> validatedProducts(String... vendorCodes) {
        return Arrays.stream(vendorCodes)
                .map(vc -> new ValidatedProductDto(
                        vc,
                        productDao.findByVendor(vc).orElse(null))
                )
                .collect(toList());
    }

    /**
     * Map vendor codes to available products
     *
     * @param vendorCodes vendor codes for mapping
     * @return a list of products with the same vendor codes
     */
    private List<Product> mapVendorCodesToProducts(String... vendorCodes) {
        return Arrays.stream(vendorCodes)
                .map(productDao::findByVendor)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    /**
     * Make a purchase based on vendor codes
     *
     * @param vendorCodes vendor codes of products that user wants to buy
     * @return <code>true</code> if and only if vendor codes is not an empty
     * list, all vendor codes is present in available products list and
     * a file with purchase information is saved successfully
     * <code>false</code> otherwise.
     */
    @Override
    public boolean buy(String... vendorCodes) {
        if (vendorCodes.length == 0) {
            log.warn("The vendor codes must contain at least one value");
            return false;
        }

        List<Product> products = mapVendorCodesToProducts(vendorCodes);
        if (vendorCodes.length != products.size()) {
            log.warn("{} vendor codes were not found", vendorCodes.length - products.size());
            return false;
        }
        return saveOrder(products);
    }

    /**
     * Saves a list of products to a csv-file on a disk
     *
     * @param products a list of products for saving
     * @return <code>true</code> if and only if a csv-file is created
     * and all products are successfully saved
     * <code>false</code> otherwise.
     */
    boolean saveOrder(List<Product> products) {
        File file = Paths.get(directoryPathToSaveOrders, generateFilename()).toFile();

        try (CSVWriter writer = createCSVWriter(file)) {
            for (Product product : products) {
                writer.writeNext(new String[]{product.getVendorCode(), product.getName(), product.getPriceAsString()});
            }
        } catch (IOException e) {
            log.error("{} was not created", file.getAbsolutePath());
            return false;
        }
        return true;
    }

    /**
     * Generate a filename to save a purchase.
     * The filename is created from a template: order-TIMESTAMP.csv,
     * where <tt>TIMESTAMP</tt> is the current server time
     *
     * @return generated filename
     */
    String generateFilename() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return "order-" + timestamp.getTime() + ".csv";
    }

    /**
     * Creates an instance of {@link CSVWriter} to save data to the file
     *
     * @param file the file to save data
     * @return an instance of {@link CSVWriter} to save data to the file
     * @throws IOException if some IO errors is occurred
     */
    CSVWriter createCSVWriter(File file) throws IOException {
        return new CSVWriter(new FileWriter(file));
    }

    /**
     * Loads products from a file with the filename and
     * update products list in the Product Data Access Object.
     * List is updated if and only if no IO error is occurred.
     *
     * @param filename the filename to load data
     */
    @Override
    public void loadProducts(String filename) {
        List<Product> list = new LinkedList<>();
        try (CSVReader reader = createCSVReader(filename)) {
            reader.forEach(line -> parseAndAddProduct(line, list));
        } catch (IOException e) {
            log.error("Error reading file {}. Products list was not updated", filename);
            return;
        }
        productDao.updateProducts(list);
    }

    /**
     * Creates an instance of {@link CSVReader} to load data from the filename.
     *
     * @param filename the filename to load data
     * @return an instance of {@link CSVReader} to load data from the filename
     * @throws FileNotFoundException if the file does not found on the disk
     */
    CSVReader createCSVReader(String filename) throws FileNotFoundException {
        return new CSVReader(new FileReader(filename));
    }

    /**
     * Parses a line (validates line arguments) and adds the product to the list.
     * A product is added to the list if and only if line arguments are
     * successfully validated.
     *
     * @param line a line arguments to parse and validate
     * @param list a list of products to add parsed data
     */
    private void parseAndAddProduct(String[] line, List<Product> list) {
        if (line.length != 3) {
            log.error("Unexpected parameters count: {}", Arrays.toString(line));
        } else if (isNull(line[0]) || "".equals(line[0])) {
            log.error("Vendor code should be non empty: {}", Arrays.toString(line));
        } else if (isNull(line[1]) || "".equals(line[1])) {
            log.error("Title should be non empty: {}", Arrays.toString(line));
        } else if (!isDouble(line[2])) {
            log.error("Price should be double: {}", Arrays.toString(line));
        } else {
            list.add(new Product(line[0], line[1], (int) (Double.parseDouble(line[2]) * 100)));
        }
    }

    /**/
    private boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}