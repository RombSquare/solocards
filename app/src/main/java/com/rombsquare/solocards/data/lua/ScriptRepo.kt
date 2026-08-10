package com.rombsquare.solocards.data.lua

import com.rombsquare.solocards.domain.repos.ScriptRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.luaj.vm2.LuaValue
import kotlin.time.Duration.Companion.milliseconds

val precode = """
    local rand = math.random
""".trimIndent()

class ScriptRepoImpl: ScriptRepo {
    override suspend fun runScript(code: String, vars: List<String>): Result<Map<String, String>> = withContext(Dispatchers.Default) {
        try {
            val globals = LuaEngine.createSandbox()

            withTimeout(2000.milliseconds) {
                val chunk = globals.load(precode + '\n' + code)
                chunk.call()

                val values = vars.associateWith { varName ->
                    val value = globals.get(varName)
                    if (!value.isnil()) value.tojstring() else throw Exception("Your question/answer contains '$varName', which your code doesn't contain it")
                }



                Result.success(values)
            }
        } catch (e: TimeoutCancellationException) {
            Result.failure(Exception("Error: Timed out"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}