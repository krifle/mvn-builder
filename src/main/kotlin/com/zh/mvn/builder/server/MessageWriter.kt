package com.zh.mvn.builder.server

import java.io.DataOutputStream
import java.net.Socket

class MessageWriter(
    private val socket: Socket
) : Thread() {

    private lateinit var dataOutputStream: DataOutputStream

    init {
        dataOutputStream = DataOutputStream(socket.getOutputStream())
    }

    override fun run() {
        super.run()
    }
}
