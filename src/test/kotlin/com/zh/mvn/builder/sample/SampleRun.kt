package com.zh.mvn.builder.sample

import org.junit.jupiter.api.Test
import org.zeroturnaround.exec.ProcessExecutor
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream

class SampleRun {

    private val terminateStr = "70D4BC64B5BA"

    @Test
    fun `ProcessExecutor 쓰레드 동작 테스트`() {
        val scriptResource = javaClass.getResource("/static/test-run.sh")!!
        val scriptFile = File(scriptResource.file)
        val scriptFilePath = scriptFile.absolutePath

        val pipedOutputStream = PipedOutputStream()
        val pipedInputStream = PipedInputStream(pipedOutputStream)

        var stringBuffer = ""

        val writeThread = Thread {
            println("Start writing")
            val command = listOf(
                "sh",
                scriptFilePath
            )
            ProcessExecutor()
                .command(command)
                .redirectOutputAlsoTo(pipedOutputStream)
                .destroyOnExit()
                .execute()
            pipedOutputStream.write(terminateStr.toByteArray())
            println("End writing")
        }

        // 읽기 쓰레드
        val writeBufferThread = Thread {
            println("Start writing to buffer")
            while (true) {
                Thread.sleep(100L)
                val byteArray = ByteArray(1024)
                pipedInputStream.read(byteArray)
                val readString = String(byteArray)
                stringBuffer += readString

                if (readString.contains(terminateStr)) {
                    break
                }
            }
        }
        writeThread.start()
        writeBufferThread.start()

        Thread.sleep(3000L)
        Thread {
            println("Read buffer1")
            println(stringBuffer)
            stringBuffer = ""
        }.start()

        Thread.sleep(2000L)
        Thread {
            println("Read buffer2")
            println(stringBuffer)
            stringBuffer = ""
        }.start()

        writeThread.join()
        writeBufferThread.join()

        println(stringBuffer)
    }
}
