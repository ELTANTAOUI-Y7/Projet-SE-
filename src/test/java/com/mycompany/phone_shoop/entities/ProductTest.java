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

    @Test
    public void fullConstructorPopulatesFields() {
        Category category = new Category();
        Product product = new Product("Phone", "Desc", "photo.jpg", 1200, 10, 3, category);

        assertEquals("Phone", product.getpName());
        assertEquals("Desc", product.getpDesc());
        assertEquals("photo.jpg", product.getpPhoto());
        assertEquals(1200, product.getpPrice());
        assertEquals(10, product.getpDiscount());
        assertEquals(3, product.getpQuantity());
        assertEquals(category, product.getCategory());
    }

    @Test
    public void settersMutateAllPropertiesAndToStringReflectsState() {
        Product product = new Product();
        product.setpId(9);
        product.setpName("Tablet");
        product.setpDesc("10 inch");
        product.setpPhoto("tablet.png");
        product.setpPrice(400);
        product.setpDiscount(5);
        product.setpQuantity(7);
        Category category = new Category();
        category.setCategoryId(4);
        product.setCategory(category);

        assertEquals("Product{pId=9, pName=Tablet, pDesc=10 inch, pPhoto=tablet.png, pPrice=400, pDiscount=5, pQuantity=7}", product.toString());
        assertEquals(category, product.getCategory());
    }
}

