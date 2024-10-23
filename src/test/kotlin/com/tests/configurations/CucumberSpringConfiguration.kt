package com.tests.configurations

import com.tests.AutomatedtestsApplication
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest

@CucumberContextConfiguration
@SpringBootTest(classes = [AutomatedtestsApplication::class])
class CucumberSpringConfiguration
