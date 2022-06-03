package com.zh.mvn.builder.server

import java.io.File
import java.io.PipedOutputStream

class ResultUploader(
    private val uploadFile: File,
    private val workingDir: String,
    private val id: String,
    private val uploadUrl: String,
    private val outputStream: PipedOutputStream
) {
    /**
     * ./target 디렉토리 압축하고 url 로 업로드
     */
    fun upload(): String {
        outputStream.write("Implement as desired...\n".toByteArray())

        val resultFilePath = "${workingDir}/${id}.zip"

        outputStream.write(uploadFile.absolutePath.toByteArray())
        outputStream.write("\n".toByteArray())
        outputStream.write(resultFilePath.toByteArray())
        outputStream.write("\n".toByteArray())
        outputStream.write(uploadUrl.toByteArray())

        ResultZipper(
            target = uploadFile,
            resultZipPath = resultFilePath
        ).zip()

        // TODO implement uploading as desired...

        return uploadUrl
    }
}
