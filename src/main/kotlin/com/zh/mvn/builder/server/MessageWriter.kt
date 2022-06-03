package com.zh.mvn.builder.server

import java.io.DataOutputStream
import java.net.Socket

class MessageWriter(
    socket: Socket
) : Thread() {

    private var dataOutputStream: DataOutputStream = DataOutputStream(socket.getOutputStream())

    override fun run() {
        super.run()
    }
}
