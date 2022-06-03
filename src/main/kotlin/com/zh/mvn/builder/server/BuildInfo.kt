package com.zh.mvn.builder.server

data class BuildInfo(
    val id: String,
    val state: BuildState,
    val source: String,
    val buildOpt: String,
    val outputBuffer: String,
    val resultOutput: String
)
