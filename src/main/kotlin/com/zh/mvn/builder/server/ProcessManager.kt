package com.zh.mvn.builder.server

import org.slf4j.LoggerFactory
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.time.LocalDateTime

class ProcessManager(
    private val id: String,
    private val javaHome: JavaHome,
    private val mavenHome: MavenHome,
    private val gitHome: String,
    private val workingDir: String,
    val source: String,
    val branch: String,
    val buildOpt: String
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ProcessManager::class.java)
        private const val terminateString = "507FDFDFCC99"
    }

    var buildState: BuildState = BuildState.START

    private var occupied = false

    private val pipedOutputStream = PipedOutputStream()
    private val pipedInputStream = PipedInputStream(pipedOutputStream)
    private val outputBuffer = StringBuilder()

    private val created = LocalDateTime.now()

    private val runThread = Thread {
        logger.info("[$id] Running thread start")

        MavenBuilder(
            id = id,
            workingDir = workingDir,
            javaHome = javaHome,
            mavenHome = mavenHome,
            gitHome = gitHome,
            source = source,
            branch = branch,
            buildOpt = buildOpt,
            outputStream = pipedOutputStream
        ).build()

        pipedOutputStream.write(terminateString.toByteArray())

        logger.info("[$id] Running done")
    }

    private val readOutputThread = Thread {
        logger.info("[$id] Reading output thread start")

        while (true) {
            Thread.sleep(1000L)

            val byteArray = ByteArray(1024 * 1024)
            pipedInputStream.read(byteArray)
            val readString = String(byteArray)

            outputBuffer.append(readString)

            if (readString.contains(terminateString)) {
                break
            }
        }
        logger.info("[$id] Reading done.")
    }

    fun start() {
        if (occupied) {
            logger.info("[$id] Already occupied. Terminating..")
            return
        }

        kotlin.runCatching {
            buildState = BuildState.WORKING
            runThread.start()
            readOutputThread.start()
        }
        .onSuccess {
            occupied = false
            buildState = BuildState.DONE
        }
        .onFailure {
            occupied = false
            buildState = BuildState.ERROR
        }
    }

    fun read(): String {
        val bufferRead = outputBuffer.toString()
        outputBuffer.clear()
        return bufferRead
    }

    fun stop() {
        logger.info("[$id] Stopping...")

        runThread.stop()
        readOutputThread.stop()

        pipedOutputStream.close()
        pipedInputStream.close()
        outputBuffer.clear()

        occupied = false
        buildState = BuildState.STOPPED

        logger.info("[$id] Stopped")
    }

    fun expired(): Boolean {
        val now = LocalDateTime.now()
        return now.minusHours(10L).isBefore(created) // 10 hours..
    }
}
