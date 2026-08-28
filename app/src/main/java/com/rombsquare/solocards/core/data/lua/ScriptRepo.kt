package com.rombsquare.solocards.core.data.lua

import com.rombsquare.solocards.core.domain.repos.ScriptRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.luaj.vm2.Globals
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.Bit32Lib
import org.luaj.vm2.lib.PackageLib
import org.luaj.vm2.lib.StringLib
import org.luaj.vm2.lib.TableLib
import org.luaj.vm2.lib.jse.JseBaseLib
import org.luaj.vm2.lib.jse.JseMathLib
import kotlin.time.Duration.Companion.milliseconds

// Runs a Lua script and returns the list of variables and its values

class ScriptRepoImpl: ScriptRepo {

    // Language initialization
    private fun createSandbox(): Globals {
        val globals = Globals()

        globals.load(JseBaseLib())
        globals.load(PackageLib())
        globals.load(Bit32Lib())
        globals.load(TableLib())
        globals.load(StringLib())
        globals.load(JseMathLib())

        LuaC.install(globals)

        return globals
    }

    // It runs the code and returns all variables and its values
    override suspend fun runScript(
        precode: String,
        code: String,
        vars: List<String>
    ): Result<Map<String, String>> = withContext(Dispatchers.Default) {
        try {
            val globals = createSandbox()

            withTimeout(2000.milliseconds) {
                val chunk = globals.load(precode + '\n' + code + '\n')
                chunk.call()

                val values = vars.associateWith { varName ->
                    globals.get(varName).tojstring()
                }

                Result.success(values)
            }
        } catch (_: TimeoutCancellationException) {
            Result.failure(Exception("Error: Timed out"))
        } catch (e: Exception) {
            val errorReason = e.message?.lines()?.last()

            if (errorReason == null) {
                Result.failure(Exception("Unknown error"))
            } else {

                // Parse the line index from error message
                val lineIndex = errorReason
                    .substringBefore(" ")
                    .drop(1)
                    .toInt()
                    .minus(precode.lines().size)

                // Obtain the last line for error message
                Result.failure(Exception(
                    e.message
                        ?.plus(" [line ${lineIndex}]")
                        ?.lines()
                        ?.last()
                        ?.substringAfter(" ")
                ))
            }
        }
    }
}