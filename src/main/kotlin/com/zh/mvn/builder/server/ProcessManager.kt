package com.zh.mvn.builder.server

import org.slf4j.LoggerFactory
import java.io.PipedInputStream
import java.io.PipedOutputStream

class ProcessManager(
    private val id: String,
    private val javaHome: JavaHome,
    private val mavenHome: MavenHome,
    private val gitHome: String,
    private val workingDir: String,
    val source: String,
    val branch: String,
    val buildOpt: String,
    private val pool: ProcessManagerPoolRepository
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ProcessManager::class.java)
        private const val terminateString = "507FDFDFCC99"
    }

    private var occupied = false

    private val pipedOutputStream = PipedOutputStream()
    private val pipedInputStream = PipedInputStream(pipedOutputStream)
    private val outputBuffer = StringBuilder()

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
        occupied = false
        pool.delete(id)

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

        runThread.start()
        readOutputThread.start()
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

        occupied = false
        pipedOutputStream.close()
        pipedInputStream.close()
        outputBuffer.clear()

        pool.delete(id)

        logger.info("[$id] Stopped")
    }
}
