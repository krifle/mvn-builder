package com.zh.mvn.builder.server

data class ServerInfoResponse(
    val javaHomeList: List<JavaHome>,
    val mavenHomeList: List<MavenHome>
)
