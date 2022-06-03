package com.zh.mvn.builder.server

import com.google.gson.Gson
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*

@Controller
class ServerController(
    private val processManagerPoolRepository: ProcessManagerPoolRepository,
    private val serverInitializer: ServerInitializer,
    private val projectProperty: ProjectProperty
) {

    companion object {
        private val gson = Gson()
    }

    @GetMapping("info")
    @ResponseBody
    fun info(): String {
        val javaHomeList = serverInitializer.getJavaHomeList()
        val mavenHomeList = serverInitializer.getMavenHomeList()

        val infoResponse = InfoResponse(javaHomeList, mavenHomeList)

        return gson.toJson(infoResponse)
    }

    @GetMapping("start")
    @ResponseBody
    fun start(
        @RequestParam(required = false, defaultValue = "1.8") javaVersion: String,
        @RequestParam(required = false, defaultValue = "3.6.3") mavenVersion: String,
        @RequestParam source: String,
        @RequestParam branch: String,
        @RequestParam buildOpt: String
    ): String {
        val id = UUID.randomUUID().toString()
        val javaHome = projectProperty.getJavaHomeList().first { it.version == javaVersion }
        val mavenHome = projectProperty.getMavenHomeList().first { it.version == mavenVersion }
        val processManager = ProcessManager(
            id = id,
            pool = processManagerPoolRepository,
            javaHome = javaHome,
            mavenHome = mavenHome,
            source = source,
            branch = branch,
            buildOpt = buildOpt,
            gitHome = projectProperty.git!!,
            workingDir = projectProperty.home!!
        )

        processManagerPoolRepository.put(id, processManager)
        processManager.start()
        val buildInfo = BuildInfo(id, BuildState.START, source, buildOpt, "", "")
        return gson.toJson(buildInfo)
    }

    @GetMapping("check")
    @ResponseBody
    fun check(@RequestParam id: String): String {
        val processManager = processManagerPoolRepository.get(id) ?: return "not found id => $id"

        val outputBuffer = processManager.read()
        val buildInfo = BuildInfo(id, BuildState.WORKING, processManager.source, processManager.buildOpt, outputBuffer, "")
        return gson.toJson(buildInfo)
    }

    @GetMapping("stop")
    @ResponseBody
    fun stop(@RequestParam id: String): String {
        val processManager = processManagerPoolRepository.get(id) ?: return "not found id => $id"
        val buildInfo = BuildInfo(id, BuildState.STOPPED, processManager.source, processManager.buildOpt, "", "")

        processManager.stop()

        return gson.toJson(buildInfo)
    }
}
