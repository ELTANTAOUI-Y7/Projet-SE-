package com.mycompany.phone_shoop.entities;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProductTest {

    @Test
    public void getPriceAfterApplyingDiscountCalculatesReducedPrice() {
        Product product = new Product();
        product.setpPrice(1000);
        product.setpDiscount(25);

        int result = product.getPriceAfterApplyingDiscount();

        assertEquals(750, result);
    }

    @Test
    public void getPriceAfterApplyingDiscountHandlesZeroDiscount() {
        Product product = new Product();
        product.setpPrice(500);
        product.setpDiscount(0);

        int result = product.getPriceAfterApplyingDiscount();

        assertEquals(500, result);
    }
}

