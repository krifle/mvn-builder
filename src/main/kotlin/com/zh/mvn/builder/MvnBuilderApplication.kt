package com.zh.mvn.builder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class MvnBuilderApplication

fun main(args: Array<String>) {
    runApplication<MvnBuilderApplication>(*args)
}
