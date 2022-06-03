package com.zh.mvn.builder.server

import net.lingala.zip4j.ZipFile
import java.io.File

class ResultZipper(
    private val target: File,
    private val resultZipPath: String
) {
    fun zip() {
        val zipFile = ZipFile(resultZipPath)

        if (target.isDirectory) {
            zipFile.addFolder(target)
        } else {
            zipFile.addFile(target)
        }
    }
}
