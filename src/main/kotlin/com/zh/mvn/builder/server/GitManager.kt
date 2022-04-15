package com.zh.mvn.builder.server

import org.slf4j.LoggerFactory
import org.zeroturnaround.exec.ProcessExecutor

class GitManager(
    private val gitPath: String
) {

    companion object {
        private val logger = LoggerFactory.getLogger(GitManager::class.java)
    }

    fun clone(
        gitRepository: String,
        directory: String
    ) {
        val command = listOf(
            gitPath,
            "clone", gitRepository,
            directory
        )
        val result = ProcessExecutor()
            .readOutput(true)
            .command(command)
            .exitValueNormal()
            .destroyOnExit()
            .executeNoTimeout()
            .outputUTF8()
        logger.info(result)
    }
}
