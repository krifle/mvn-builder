package com.zh.mvn.builder.server

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream
import java.io.File

internal class MavenManagerTest {

    companion object {
        private val logger = LoggerFactory.getLogger(MavenManager::class.java)
    }

    @Test
    fun `maven build 동작 테스트`() {
        val sut = MavenManager("/Users/janghokim/apps/mvn/apache-maven-3.6.3")
        sut.build(
            mavenCommand = listOf("package"),
            directory = "/Users/janghokim/Desktop/temp"
        )
    }

    @Test
    fun `JAVA_HOME 설정 테스트`() {
        val file = File(javaClass.getResource("/static/sample-run.sh").file)
        val path = file.absolutePath

        val command = listOf("sh", path)

        ProcessExecutor()
            .command(command)
            .redirectOutput(Slf4jStream.of(logger).asInfo())
            .execute()
    }
}
