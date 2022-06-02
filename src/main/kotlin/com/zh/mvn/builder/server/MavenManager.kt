package com.zh.mvn.builder.server

import org.slf4j.LoggerFactory
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.slf4j.Slf4jInfoOutputStream
import java.io.DataOutputStream
import java.io.File

class MavenManager(
    private val token: String,
    private val javaHome: JavaHome,
    private val mavenHome: MavenHome,
    private val workingPath: String,
    private val buildOpt: String,
    private val outputStream: DataOutputStream
): Thread() {

    companion object {
        private val logger = LoggerFactory.getLogger(MavenManager::class.java)
    }

    override fun run() {
        val mavenBuildScript = File(javaClass.getResource("/static/maven-builder.sh").file)
        val mavenBuildScriptPath = mavenBuildScript.absolutePath!!
        val command = listOf(
            "sh",
            mavenBuildScriptPath,
            javaHome.home,
            mavenHome.home,
            workingPath
        )
        val buildOptList = buildOpt.split(" ")

        ProcessExecutor()
            .command(command + buildOptList)
            .redirectOutput(Slf4jInfoOutputStream(logger))
            .redirectOutputAlsoTo(outputStream)
            .destroyOnExit()
            .execute()

        outputStream.writeUTF(token)
        outputStream.flush()
        outputStream.close()
    }
}
