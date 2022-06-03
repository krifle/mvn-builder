package com.zh.mvn.builder.server

import org.slf4j.LoggerFactory
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.slf4j.Slf4jInfoOutputStream
import java.io.File
import java.io.PipedOutputStream

class MavenManager(
    private val javaHome: JavaHome,
    private val mavenHome: MavenHome,
    private val workingPath: String,
    private val buildOpt: String,
    private val outputStream: PipedOutputStream
) {

    companion object {
        private val logger = LoggerFactory.getLogger(MavenManager::class.java)
        private const val terminateString = "972A0C59E2F0"
    }

    fun build() {
        checkPomFile()

        val command = listOf(
            "${mavenHome.home}/bin/mvn",
            "-f", "$workingPath/pom.xml"
        )
        val buildOptList = buildOpt.split(" ")

        logger.info("start")

        outputStream.write((command + buildOptList).joinToString(" ").toByteArray())

        ProcessExecutor()
            .command(command + buildOptList)
            .redirectOutput(Slf4jInfoOutputStream(logger))
            .redirectOutputAlsoTo(outputStream)
            .redirectErrorAlsoTo(outputStream)
            .environment("JAVA_HOME", javaHome.home)
            .destroyOnExit()
            .execute()
            .exitValue

        outputStream.write(terminateString.toByteArray())
    }

    private fun checkPomFile() {
        val pomFile = File("$workingPath/pom.xml")
        if (!pomFile.exists()) {
            throw IllegalStateException("pom.xml does not exists. check your directory $workingPath/pom.xml")
        }
    }
}
