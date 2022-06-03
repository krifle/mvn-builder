package com.zh.mvn.builder.server

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@EnableAutoConfiguration
internal class ProjectPropertyTest {

    @Autowired
    private lateinit var projectProperty: ProjectProperty

    @Test
    fun t() {
        println("/////////")
        println(projectProperty.java)
        println(projectProperty.maven)
    }
}
