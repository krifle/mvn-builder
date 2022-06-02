package com.zh.mvn.builder.server

import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import org.zeroturnaround.exec.ProcessExecutor
import java.io.File

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
        logger.info("Removing old directory..")
        FileUtils.deleteDirectory(File(directory))
        Thread.sleep(1000L)
        logger.info("Old directory removed.")

        val command = listOf(
            gitPath,
            "clone", gitRepository,
            directory
        )
        logger.info(command.joinToString(" "))
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
