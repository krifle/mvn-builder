package com.zh.mvn.builder.server

data class InfoResponse(
    val javaHomeList: List<JavaHome>,
    val mavenHomeList: List<MavenHome>
)
