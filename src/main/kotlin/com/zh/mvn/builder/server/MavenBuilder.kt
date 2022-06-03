package com.zh.mvn.builder.server

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.PipedOutputStream

class MavenBuilder(
    private val id: String,
    private val workingDir: String,
    private val javaHome: JavaHome,
    private val mavenHome: MavenHome,
    private val gitHome: String,
    private val source: String,
    private val branch: String,
    private val buildOpt: String,
    private val outputStream: PipedOutputStream
) {
    fun build() {
        val directory = "$workingDir/$id/"
        FileUtils.forceMkdir(File(directory))

        GitManager(
            gitPath = gitHome,
            gitRepository = source,
            directory = directory,
            gitBranch = branch,
            outputStream = outputStream
        ).clone()

        MavenManager(
            javaHome = javaHome,
            mavenHome = mavenHome,
            workingPath = directory,
            buildOpt = buildOpt,
            outputStream = outputStream
        ).build()
    }
}
