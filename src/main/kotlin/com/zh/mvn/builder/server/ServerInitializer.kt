package com.zh.mvn.builder.server

import com.zh.mvn.builder.exception.ServerInitializationException
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

@Component
@Profile("server")
class ServerInitializer(
    private val projectProperty: ProjectProperty
) {

    companion object {
        private val logger = LoggerFactory.getLogger(ServerInitializer::class.java)
    }

    private lateinit var javaHomeList: List<JavaHome>
    private lateinit var mavenHomeList: List<MavenHome>
    private lateinit var git: String
    private lateinit var home: String

    @PostConstruct
    fun initialCheck() {
        // 모든 java 및 maven 경로 확인하고 체크
        javaHomeList = projectProperty.getJavaHomeList()
        mavenHomeList = projectProperty.getMavenHomeList()

        javaHomeList.forEach { checkJava(it) }
        mavenHomeList.forEach { checkMaven(it) }

        git = projectProperty.git!!
        checkGit()

        home = projectProperty.home!!
        checkHome()
    }

    private fun checkJava(javaHome: JavaHome) {
        if (!File(javaHome.home, "/bin/java").exists()) {
            throw ServerInitializationException("cannot find java for ${javaHome.home}")
        }
        logger.info("Java checked => Version: {}, Java Home: {}", javaHome.version, javaHome.home)
    }

    private fun checkMaven(mavenHome: MavenHome) {
        if (!File(mavenHome.home, "/bin/mvn").exists()) {
            throw ServerInitializationException("cannot find maven for ${mavenHome.home}")
        }
        logger.info("Maven checked => Version: {}, Maven Home: {}", mavenHome.version, mavenHome.home)
    }

    private fun checkGit() {
        if (!File(git).exists()) {
            throw ServerInitializationException("cannot find git for $git")
        }
        logger.info("Git checked: {}", git)
    }

    private fun checkHome() {
        val homeDirectory = File(home)
        if (!homeDirectory.exists()) {
            throw ServerInitializationException("cannot find home directory for $home")
        }
        if (!homeDirectory.isDirectory) {
            throw ServerInitializationException("home directory for $home is not a directory")
        }
        logger.info("Home Directory checked: {}", home)
    }

    fun getJavaHomeList(): List<JavaHome> {
        return javaHomeList
    }

    fun getMavenHomeList(): List<MavenHome> {
        return mavenHomeList
    }

    fun getGit(): String {
        return git
    }

    fun getHome(): String {
        return home
    }
}
