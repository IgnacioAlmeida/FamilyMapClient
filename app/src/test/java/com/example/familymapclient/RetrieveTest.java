package com.example.familymapclient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Requests.LoginRequest;
import Responses.LoginResponse;
import cache.DataCache;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RetrieveTest {

    private LoginRequest main;
    private ServerProxy proxy;
    private String authToken;
    private LoginResponse loginResponse;

    @BeforeEach
    public void setUp(){
        proxy = new ServerProxy();
        main = new LoginRequest();
        main.setUserName("sheila");
        main.setPassword("parker");
        loginResponse = proxy.login(main);
        authToken = loginResponse.getAuthtoken();

    }

    @AfterEach
    public void tearDown(){
        DataCache.getInstance().clear();
    }

    @Test
    public void retrievePeoplePass() {
        assertNotNull(proxy.getPeople(authToken));
    }

    @Test
    public void retrievePeopleFail() {
        assertFalse(proxy.getPeople("123").isSuccess());
    }

    @Test
    public void retrieveEventPass() {
        assertNotNull(proxy.getEvents(authToken));
    }

    @Test
    public void retrieveEventFail() {
        assertFalse(proxy.getEvents("123").isSuccess());

    }
}
