package com.zh.mvn.builder.server

import org.junit.jupiter.api.Test

internal class GitManagerTest {

    @Test
    fun `checkout 동작 테스트`() {
        val sut = GitManager("/usr/bin/git")
        sut.clone(
            gitRepository = "https://github.com/krifle/vtt-smi-converter.git",
            directory = "/Users/janghokim/Desktop/temp/git/"
        )
    }
}
