package com.zh.mvn.builder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class MvnBuilderApplication

fun main(args: Array<String>) {
    runApplication<MvnBuilderApplication>(*args)
}
