package com.zh.mvn.builder.server

import com.zh.mvn.builder.exception.ServerInitializationException
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.config")
class ProjectProperty {

    var java: List<Map<String, String>>? = null

    var maven: List<Map<String, String>>? = null

    var git: String? = null

    var home: String? = null

    fun getJavaHomeList(): List<JavaHome> {
        if (java == null) {
            throw ServerInitializationException("app.config.java property is not initialized")
        }
        return java!!.map {
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
        if (maven == null) {
            throw ServerInitializationException("app.config.maven property is not initialized")
        }
        return maven!!.map {
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
