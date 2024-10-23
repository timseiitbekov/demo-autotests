package com.tests.configurations

import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest

@CucumberContextConfiguration
@SpringBootTest(classes = [CucumberTest::class])
class CucumberSpringConfiguration {
}