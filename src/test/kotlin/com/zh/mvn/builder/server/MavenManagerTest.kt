package com.zh.mvn.builder.server

import org.junit.jupiter.api.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream

internal class MavenManagerTest {

    private val pipedOutputStream = PipedOutputStream()
    private val pipedInputStream = PipedInputStream(pipedOutputStream)

    @Test
    fun `build test`() {
        // given
        val sut = MavenManager(
            javaHome = JavaHome("1.8", "/Users/user/Library/Java/JavaVirtualMachines/azul-1.8.0_322/Contents/Home/"),
            mavenHome = MavenHome("3.6.3", "/Users/user/apps/apache-maven-3.6.3"),
            workingPath = "/Users/user/Desktop/temp/mvn-builder/sample",
            buildOpt = "package -DskipTests",
            outputStream = pipedOutputStream
        )
        readOutputThread.start()

        // when
        sut.build()

        // then
        pipedOutputStream.close()
        pipedInputStream.close()
    }

    private val readOutputThread = Thread {
        while (true) {
            Thread.sleep(1000L)
            val byteArray = ByteArray(1024)
            pipedInputStream.read(byteArray)
            val readString = String(byteArray)

            if (readString.isNotEmpty()) {
                println(readString)
            }

            if (readString.contains("972A0C59E2F0")) {
                break
            }
        }
    }
}
