package com.example.familymapclient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Requests.LoginRequest;
import Requests.RegisterRequest;
import cache.DataCache;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterTest {

    private RegisterRequest mainRegister;
    private LoginRequest main;
    private RegisterRequest testRegister;
    private ServerProxy proxy;

    @BeforeEach
    public void setUp(){
        proxy = new ServerProxy();
        mainRegister = new RegisterRequest();
        mainRegister.setGender("test");
        mainRegister.setEmail("test");
        mainRegister.setLastName("test");
        mainRegister.setFirstName("test");
        mainRegister.setPassword("test");
        mainRegister.setUserName("test");
        main = new LoginRequest();
        main.setUserName("test");
        main.setPassword("test");
        testRegister = new RegisterRequest();
        testRegister.setUserName("shakira");
        testRegister.setPassword("pique");

        proxy.register(mainRegister);

    }

    @AfterEach
    public void tearDown(){
        DataCache.getInstance().clear();
    }

    @Test
    public void registerPass() {
        assertTrue(DataCache.getInstance().isRegistered());
    }

    @Test
    public void registerFail() {
        //todo not really sure about this
       assertFalse(proxy.register(testRegister).isSuccess());
    }
}
