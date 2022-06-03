package com.zh.mvn.builder.server

import org.slf4j.LoggerFactory
import java.io.File
import java.io.PipedOutputStream

class ResultUploader(
    private val uploadFile: File,
    private val workingDir: String,
    private val id: String,
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
        logger.info(uploadFile.absolutePath)
        logger.info(url)

        outputStream.write("Implement as desired...\n".toByteArray())

        val resultFilePath = "${workingDir}/${id}.zip"

        outputStream.write(uploadFile.absolutePath.toByteArray())
        outputStream.write("\n".toByteArray())
        outputStream.write(resultFilePath.toByteArray())

        ResultZipper(
            target = uploadFile,
            resultZipPath = resultFilePath
        ).zip()

        // TODO implement uploading as desired...

        return url
    }
}
