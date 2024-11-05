package com.tests.implementation.services

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.ResponseResultOf
import com.google.gson.Gson
import com.tests.implementation.models.User
import io.cucumber.datatable.DataTable
import io.qameta.allure.Step
import org.springframework.stereotype.Component

@Component
class DemoService {

    fun `some method service`() {
        println("demo step")
    }

    @Step("Send GET request to {endpoint} endpoint")
    fun `send GET request to endpoint`(endpoint: String): ResponseResultOf<String> {
        return Fuel.get(endpoint).header(returnAuthHeader()).responseString()
    }

    @Step("Create user model")
    fun `return model of user from datatable`(dataTable: DataTable): User {
        val map1 = dataTable.asMap()
        return Gson().fromJson(map1.toString(), User::class.java)
    }

    @Step("Send post request to {endpoint} endpoint with {jsonBody} body")
    fun `send POST request to endpoint with JSON body`(endpoint: String, jsonBody: String) {
        val (request, response, result) = Fuel.post(endpoint)
            .header(returnAuthHeader())
            .header("Content-Type", "application/json")
            .body(jsonBody)
            .responseString()

        println("Request: $request")
        println("Status Code: ${response.statusCode}")
        println("Response: ${response.body().asString("application/json")}")
    }

    fun `получить имя клиента через айди`() {
        println("DoSomeThing")
    }

    fun returnAuthHeader(): Map<String, String> {
        `получить имя клиента через айди`()
        return mapOf("Authorization" to "Bearer 2277d3ce250da91e2411e6053f671f44e46416c4a54276af7ad53ec325b172ed")
    }
}
