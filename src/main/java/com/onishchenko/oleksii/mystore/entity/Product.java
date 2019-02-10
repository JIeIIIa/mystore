package com.onishchenko.oleksii.mystore.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * The entity class to encapsulate product information
 */
public class Product implements Serializable {

    private static final long serialVersionUID = -6961523554631370433L;

    /**
     * The vendor code of the product
     */
    private final String vendorCode;

    /**
     * The product's name
     */
    private final String name;

    /**
     * The product price in cents.
     * This is because floating points on computers are inherently inaccurate for decimal values(0.1+0.2 != 0.3)
     */
    private final int price;

    public Product(String vendorCode, String name, int price) {
        this.vendorCode = vendorCode;
        this.name = name;
        this.price = price;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    /**
     * Convert the price to dollars
     *
     * @return A string that represents the price in dollars.
     */
    public String getPriceAsString() {
        return String.valueOf(price / 100.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price &&
                Objects.equals(vendorCode, product.vendorCode) &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorCode, name, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "vendorCode='" + vendorCode + '\'' +
                ", name='" + name + '\'' +
                ", price=" + getPriceAsString() +
                '}';
    }
}
