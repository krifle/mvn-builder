package com.zh.mvn.builder.server

import org.junit.jupiter.api.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream

internal class GitManagerTest {

    private val pipedOutputStream = PipedOutputStream()
    private val pipedInputStream = PipedInputStream(pipedOutputStream)

    @Test
    fun `checkout 동작 테스트`() {
        // given
        val sut = GitManager(
            gitPath = "/usr/bin/git",
            gitRepository = "https://github.com/krifle/mvn-builder/",
            gitBranch = "develop",
            directory = "/Users/user/Desktop/temp/mvn-builder/sample",
            pipedOutputStream
        )
        readOutputThread.start()

        // when
        sut.clone()
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

            if (readString.contains("FFA01D819D7F")) {
                break
            }
        }
    }
}
