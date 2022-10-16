package com.spotify.oauth2.tests;

import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class BaseTest {

    @BeforeMethod
    public void beforeTest(Method m){
        System.out.println("TEST NAME: " + m.getName());
        System.out.println("THREAD ID: " + Thread.currentThread().getId());
    }
}
