package com.tests.tagHandlers

import io.cucumber.java.After

class AfterHandler {

    @After
    fun after() {
        println("This is the after step")
    }
}
