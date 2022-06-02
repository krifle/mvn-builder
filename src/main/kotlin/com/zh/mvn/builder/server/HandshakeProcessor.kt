package com.zh.mvn.builder.server

import com.zh.mvn.builder.model.HandshakeResponse
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.util.*

class HandshakeProcessor(
    private val projectProperty: ProjectProperty
) {
    fun process(
        javaHome: JavaHome,
        mavenHome: MavenHome,
        source: String,
        buildOpt: String
    ): HandshakeResponse {
        val token = UUID.randomUUID().toString()

        // git 클론
        val directory = "${projectProperty.home}/token/"
        GitManager(projectProperty.git!!).clone(gitRepository = source, directory = directory)

        // 소켓 생성
        val serverSocket = ServerSocket(0)

        // OutputStream 생성
        val byteArrayOutputStream = ByteArrayOutputStream()
        val dataOutputStream = DataOutputStream(byteArrayOutputStream)

        // maven build (별도 쓰레드로 돌리기)
        val mavenManager = MavenManager(token, javaHome, mavenHome, directory, buildOpt, dataOutputStream)

        // 소켓 listen 도 별도 쓰레드로 돌린다.

        return HandshakeResponse(token = token, port = serverSocket.localPort)
    }
}
