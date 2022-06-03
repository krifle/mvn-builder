package com.zh.mvn.builder.server

import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.slf4j.Slf4jInfoOutputStream
import java.io.File
import java.io.PipedOutputStream

class GitManager(
    private val gitPath: String,
    private val gitRepository: String,
    private val gitBranch: String,
    private val directory: String,
    private val outputStream: PipedOutputStream
) {

    companion object {
        private val logger = LoggerFactory.getLogger(GitManager::class.java)
        private const val terminateString = "FFA01D819D7F"
    }

    fun clone() {
        FileUtils.deleteDirectory(File(directory))
        Thread.sleep(1000L)

        val command = listOf(
            gitPath,
            "clone",
            "-b", gitBranch, "--single-branch",
            gitRepository,
            directory
        )

        val gitCommand = command.joinToString(" ")
        logger.info(gitCommand)
        outputStream.write(gitCommand.toByteArray())

        ProcessExecutor()
            .command(command)
            .redirectOutput(Slf4jInfoOutputStream(logger))
            .redirectOutputAlsoTo(outputStream)
            .redirectErrorAlsoTo(outputStream)
            .destroyOnExit()
            .executeNoTimeout()

        outputStream.write(terminateString.toByteArray())
    }
}
