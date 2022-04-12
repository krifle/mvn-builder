package com.zh.mvn.builder.server

import org.slf4j.LoggerFactory
import java.net.ServerSocket
import java.util.*

class SocketServer(
    private val port: Int
) {
    companion object {
        private val logger = LoggerFactory.getLogger(SocketServer::class.java)
    }

    private lateinit var serverSocket: ServerSocket
    private val sockets = Vector<SocketThread>()

    fun build() {
        try {
            serverSocket = ServerSocket(port)

            val socket = serverSocket.accept()

        } catch (e: Exception) {
            logger.error("", e)
        }
    }
}
