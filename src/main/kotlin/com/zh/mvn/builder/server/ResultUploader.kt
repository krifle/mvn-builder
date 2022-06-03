package com.zh.mvn.builder.server

import org.slf4j.LoggerFactory
import java.io.File
import java.io.PipedOutputStream

class ResultUploader(
    private val uploadDir: File,
    private val url: String,
    private val outputStream: PipedOutputStream
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ResultUploader::class.java)
    }

    /**
     * ./target 디렉토리 압축하고 url 로 업로드
     */
    fun upload(): String {
        logger.info(uploadDir.absolutePath)
        logger.info(url)

        outputStream.write("Implement as desired...".toByteArray())

        return url // TODO implement as desired...
    }
}
