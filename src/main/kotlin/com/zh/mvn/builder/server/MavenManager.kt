package com.zh.mvn.builder.server

import org.slf4j.LoggerFactory

class MavenManager(
    private val mavenPath: String
) {

    companion object {
        private val logger = LoggerFactory.getLogger(MavenManager::class.java)
    }

    fun build(mavenCommand: List<String>, directory: String) {
        val command = listOf(
            mavenPath

        )
    }
}
