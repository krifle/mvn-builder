package com.zh.mvn.builder.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.*

@RestController
class ServerController(
    private val processManagerPoolRepository: ProcessManagerPoolRepository,
    private val serverInitializer: ServerInitializer,
    private val projectProperty: ProjectProperty
) {

    @GetMapping("info")
    fun info(): InfoResponse {
        val javaHomeList = serverInitializer.getJavaHomeList()
        val mavenHomeList = serverInitializer.getMavenHomeList()

        return InfoResponse(javaHomeList, mavenHomeList)
    }

    @GetMapping("start")
    fun start(
        @RequestParam(required = false, defaultValue = "1.8") javaVersion: String,
        @RequestParam(required = false, defaultValue = "3.6.3") mavenVersion: String,
        @RequestParam source: String,
        @RequestParam branch: String,
        @RequestParam buildOpt: String,
        @RequestParam targetDir: String
    ): BuildInfo {
        val id = UUID.randomUUID().toString()
        val javaHome = projectProperty.getJavaHomeList().first { it.version == javaVersion }
        val mavenHome = projectProperty.getMavenHomeList().first { it.version == mavenVersion }
        val processManager = ProcessManager(
            id = id,
            javaHome = javaHome,
            mavenHome = mavenHome,
            source = source,
            branch = branch,
            buildOpt = buildOpt,
            gitHome = projectProperty.git!!,
            workingDir = projectProperty.home!!,
            targetDir = targetDir,
            uploadUrl = projectProperty.uploadUrl!!
        )

        processManagerPoolRepository.put(id, processManager)
        processManager.start()

        return BuildInfo(
            id = id,
            created = LocalDateTime.now(),
            state = BuildState.START,
            source = source,
            buildOpt = buildOpt,
            outputBuffer = "",
            resultUrl = ""
        )
    }

    @GetMapping("check")
    fun check(@RequestParam id: String): BuildInfo {
        val processManager = processManagerPoolRepository.get(id)
            ?: return BuildInfo.ofError("not found id => $id")

        return BuildInfo.of(processManager)
    }

    @GetMapping("stop")
    fun stop(@RequestParam id: String): BuildInfo {
        val processManager = processManagerPoolRepository.get(id)
            ?: return BuildInfo.ofError("not found id => $id")

        processManager.stop()

        return BuildInfo.of(processManager)
    }
}
