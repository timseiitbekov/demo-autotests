package com.tests.tagHandlers

import io.cucumber.java.Before

class BeforeHandler {

    @Before
    fun before() {
        println("This is the before step")
    }
}
