package com.mycompany.phone_shoop.entities;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class CategoryTest {

    @Test
    public void constructorInitializesAllFields() {
        Category category = new Category(10, "Phones", "Smartphones");

        assertEquals(10, category.getCategoryId());
        assertEquals("Phones", category.getCategoryTitle());
        assertEquals("Smartphones", category.getCategoryDescription());
    }

    @Test
    public void productsListCanBeReplacedAndQueried() {
        Product product = new Product();
        List<Product> products = Arrays.asList(product);

        Category category = new Category("Accessories", "Phone accessories", products);

        assertSame(products, category.getProducts());
    }

    @Test
    public void settersUpdateValues() {
        Category category = new Category();
        category.setCategoryId(3);
        category.setCategoryTitle("Wearables");
        category.setCategoryDescription("Smart watches");

        assertEquals("Category{categoryId=3, categoryTitle=Wearables, categoryDescription=Smart watches}", category.toString());
    }
}

