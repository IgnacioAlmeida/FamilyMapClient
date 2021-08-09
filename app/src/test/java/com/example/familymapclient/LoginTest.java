package com.example.familymapclient;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Requests.LoginRequest;
import Responses.LoginResponse;
import cache.DataCache;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
    private LoginRequest main;
    private LoginRequest test;
    private ServerProxy proxy;
    private LoginResponse loginResponse;

    @BeforeEach
    public void setUp(){
        proxy = new ServerProxy();

        main = new LoginRequest();
        main.setUserName("sheila");
        main.setPassword("parker");

        test = new LoginRequest();
        test.setUserName("usernameTest");
        test.setPassword("passTest");

        loginResponse = proxy.login(main);
    }

    @AfterEach
    public void tearDown(){
        DataCache.getInstance().clear();
    }

    @Test
    public void loginPass() {
        String loginResponseID = loginResponse.getPersonID();
        assertTrue(loginResponseID.equals("Sheila_Parker"));
    }

    @Test
    public void loginFail() {
        String personID = proxy.login(test).getPersonID();
        assertNull(personID);
        assertFalse(DataCache.getInstance().isStatus());
    }
}
