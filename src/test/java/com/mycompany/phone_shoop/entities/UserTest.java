package com.mycompany.phone_shoop.entities;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    public void constructorPopulatesAllFields() {
        User user = new User(5, "Jane", "jane@mail.com", "pass", "111222", "pic.png", "address", "admin");

        assertEquals(5, user.getUserId());
        assertEquals("Jane", user.getUserName());
        assertEquals("jane@mail.com", user.getUserEmail());
        assertEquals("admin", user.getUserType());
    }

    @Test
    public void settersMutateValues() {
        User user = new User();
        user.setUserId(8);
        user.setUserName("Mike");
        user.setUserEmail("mike@mail.com");
        user.setUserPassword("secret");
        user.setUserPhone("999");
        user.setUserPic("pic.jpg");
        user.setUserAddress("Somewhere");
        user.setUserType("normal");

        assertEquals("User{userId=8, userName=Mike, userEmail=mike@mail.com, userPassword=secret, userPhone=999, userPic=pic.jpg, userAddress=Somewhere}", user.toString());
    }

    @Test
    public void constructorWithoutIdPopulatesFields() {
        User user = new User("Luke", "luke@mail.com", "pass", "321", "pic.png", "addr", "normal");

        assertEquals("Luke", user.getUserName());
        assertEquals("luke@mail.com", user.getUserEmail());
        assertEquals("normal", user.getUserType());
        // userId stays default
        assertEquals(0, user.getUserId());
    }
}

