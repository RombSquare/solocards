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

// Built-in functions for scripting in Solocards
// 1. rand(a, b)  -  generates a random integer in [a; b]
// 2. pick(list)  -  pick a random item from the list
// 3. randOptions(min, max, n, excluded)
// -   it unpacks the values for n variables, where each value is random in [min; max] with excluded value

val precode = """    
    local rand = math.random
    local min = math.min
    local max = math.max
    local abs = math.abs
    
    local function randExcept(a, b, c)
        local val = math.random(a, b)
        if val == c and a ~= c then
            return a
        end
        return val
    end
    
    local function pick(list)
        if #list == 0 then return nil end
        return list[math.random(#list)]
    end
    
    local function randOptions(min, max, count, excluded)
        local pool = {}
        for i = min, max do
            if i ~= excluded then
                table.insert(pool, i)
            end
        end

        local totalAvailable = #pool
        local selectCount = math.min(count, totalAvailable)

        for i = 1, selectCount do
            local randIndex = math.random(i, totalAvailable)
            pool[i], pool[randIndex] = pool[randIndex], pool[i]
        end

        local results = { table.unpack(pool, 1, selectCount) }
        return table.unpack(results)
    end
""".trimIndent()

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
    override suspend fun runScript(code: String, vars: List<String>): Result<Map<String, String>> = withContext(Dispatchers.Default) {
        try {
            val globals = createSandbox()

            withTimeout(2000.milliseconds) {
                val chunk = globals.load(precode + '\n' + code + '\n')
                chunk.call()

                val values = vars.associateWith { varName ->
                    val value = globals.get(varName)
                    if (!value.isnil()) value.tojstring() else
                        throw Exception("Your card contains '$varName', which your code doesn't")
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