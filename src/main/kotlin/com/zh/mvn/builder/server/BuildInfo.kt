package com.zh.mvn.builder.server

import java.time.LocalDateTime

data class BuildInfo(
    val id: String,
    val created: LocalDateTime,
    val state: BuildState,
    val source: String,
    val buildOpt: String,
    val outputBuffer: String,
    val resultUrl: String
) {

    companion object {
        fun of(processManager: ProcessManager): BuildInfo {
            return BuildInfo(
                id = processManager.id,
                created = processManager.created,
                state = processManager.buildState,
                source = processManager.source,
                buildOpt = processManager.buildOpt,
                outputBuffer = processManager.readOutputBuffer(),
                resultUrl = processManager.uploadedUrl
            )
        }

        fun ofError(errorMessage: String): BuildInfo {
            return BuildInfo(
                id = "",
                created = LocalDateTime.now(),
                state = BuildState.ERROR,
                source = "",
                buildOpt = "",
                outputBuffer = errorMessage,
                resultUrl = ""
            )
        }
    }
}
