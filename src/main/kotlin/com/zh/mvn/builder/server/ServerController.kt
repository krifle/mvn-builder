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
    @Autowired
    private lateinit var projectProperty: ProjectProperty

    @GetMapping(value = ["/info"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun info(): String {
        val javaHomeList = serverInitializer.getJavaHomeList()
        val mavenHomeList = serverInitializer.getMavenHomeList()

        val infoResponse = InfoResponse(javaHomeList, mavenHomeList)

        return gson.toJson(infoResponse)
    }

    @GetMapping(value = ["/handshake"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun handshake(
        @RequestParam(required = false, defaultValue = "8") javaVersion: String,
        @RequestParam(required = false, defaultValue = "3.8.5") mavenVersion: String,
        @RequestParam source: String,
        @RequestParam buildOpt: String
    ): String {
        val javaHome = projectProperty.getJavaHomeList().first { it.version == javaVersion }
        val mavenHome = projectProperty.getMavenHomeList().first { it.version == mavenVersion }
        val handshakeResponse = HandshakeProcessor(projectProperty).process(
            javaHome = javaHome,
            mavenHome = mavenHome,
            source = source,
            buildOpt = buildOpt
        )
        return gson.toJson(handshakeResponse)
    }
}
