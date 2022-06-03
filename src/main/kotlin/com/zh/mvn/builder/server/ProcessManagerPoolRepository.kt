package com.zh.mvn.builder.server

import org.springframework.stereotype.Repository

@Repository
class ProcessManagerPoolRepository {

    private val processManagerPool = mutableMapOf<String, ProcessManager>()

    fun put(id: String, processManager: ProcessManager) {
        processManagerPool[id] = processManager
    }

    fun get(id: String): ProcessManager? {
        return processManagerPool[id]
    }

    fun findExpiredIds(): List<String> {
        return processManagerPool.filterValues {
            it.expired()
        }.map {
            it.key
        }
    }

    fun delete(id: String) {
        processManagerPool.remove(id)
    }
}
