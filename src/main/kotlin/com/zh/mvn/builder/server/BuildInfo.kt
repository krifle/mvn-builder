package com.zh.mvn.builder.server

import java.sql.Timestamp
import java.time.LocalDateTime

data class BuildInfo(
    val id: String,
    val created: Long,
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
                created = Timestamp.valueOf(processManager.created).time,
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
                created = Timestamp.valueOf(LocalDateTime.now()).time,
                state = BuildState.ERROR,
                source = "",
                buildOpt = "",
                outputBuffer = errorMessage,
                resultUrl = ""
            )
        }
    }
}
