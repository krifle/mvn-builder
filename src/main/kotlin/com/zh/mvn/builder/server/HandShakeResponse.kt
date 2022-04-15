package com.zh.mvn.builder.server

import java.util.*

data class HandShakeResponse (
    val token: String = UUID.randomUUID().toString(),
    val port: Int
)
