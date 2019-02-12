package com.onishchenko.oleksii.mystore.dto;

import com.onishchenko.oleksii.mystore.entity.Product;

import java.util.Objects;

import static java.util.Objects.isNull;

public class ValidatedProductDto {
    private String vendorCode;

    private Product product;

    public ValidatedProductDto(String vendorCode, Product product) {
        this.vendorCode = vendorCode;
        this.product = product;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public Product getProduct() {
        return product;
    }

    public boolean isValid() {
        return !isNull(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidatedProductDto that = (ValidatedProductDto) o;
        return Objects.equals(vendorCode, that.vendorCode) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorCode, product);
    }

    @Override
    public String toString() {
        return "ValidatedProductDto{" +
                "vendorCode='" + vendorCode + '\'' +
                ", product=" + product +
                '}';
    }
}
