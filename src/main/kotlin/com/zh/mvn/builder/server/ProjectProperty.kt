package com.zh.mvn.builder.server

import com.zh.mvn.builder.exception.ServerInitializationException
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.config")
data class ProjectProperty(
    val java: List<Map<String, String>>,
    val maven: List<Map<String, String>>
) {
    var git: String = ""
    var home: String = ""
    var uploadUrl: String = ""

    fun getJavaHomeList(): List<JavaHome> {
        return java.map {
            val version = it.get("version")
            val home = it.get("home")
            if (version == null) {
                throw ServerInitializationException("app.config.java.version is not initialized")
            }
            if (home == null) {
                throw ServerInitializationException("app.config.java.home is not initialized")
            }
            return@map JavaHome(version, home)
        }
    }

    fun getMavenHomeList(): List<MavenHome> {
        return maven.map {
            val version = it.get("version")
            val home = it.get("home")
            if (version == null) {
                throw ServerInitializationException("app.config.maven.version is not initialized")
            }
            if (home == null) {
                throw ServerInitializationException("app.config.maven.home is not initialized")
            }
            return@map MavenHome(version, home)
        }
    }
}
