package com.zh.mvn.builder.server

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class PoolCleaner(
    private val processManagerPoolRepository: ProcessManagerPoolRepository
) {

    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS)
    fun clean() {
        val expiredIds = processManagerPoolRepository.findExpiredIds()
        expiredIds.forEach { id ->
            processManagerPoolRepository.delete(id)
        }
    }
}
