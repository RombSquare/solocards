package com.rombsquare.solocards.core.domain.repos

interface ScriptRepo {
    suspend fun runScript(precode: String, code: String, vars: List<String>): Result<Map<String, String>>
}