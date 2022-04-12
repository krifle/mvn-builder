package com.zh.mvn.builder.server

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@Profile("server")
@RequestMapping("/server")
class ServerController {

    companion object {
        private val gson = Gson()
    }

    @Autowired
    private lateinit var serverInitializer: ServerInitializer

    @GetMapping(value = ["/info"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun info(): String {
        val javaHomeList = serverInitializer.getJavaHomeList()
        val mavenHomeList = serverInitializer.getMavenHomeList()

        val serverInfoResult = ServerInfoResponse(javaHomeList, mavenHomeList)

        return gson.toJson(serverInfoResult)
    }

    @GetMapping(value = ["/handshake"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun handshake(
        @RequestParam(required = false, defaultValue = "8") javaVersion: String,
        @RequestParam(required = false, defaultValue = "3.8.5") mavenVersion: String,
        @RequestParam source: String
    ): String {
        // TODO 소스 checkout
        // TODO server 소켓 만들고 소켓 정보 돌려주기
        return ""
    }
}
