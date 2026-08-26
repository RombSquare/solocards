package com.rombsquare.solocards.core.data.lua

import com.rombsquare.solocards.core.domain.repos.ScriptRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.milliseconds

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
    override suspend fun runScript(code: String, vars: List<String>): Result<Map<String, String>> = withContext(Dispatchers.Default) {
        try {
            val globals = LuaEngine.createSandbox()

            withTimeout(2000.milliseconds) {
                val chunk = globals.load(precode + '\n' + code + '\n')
                chunk.call()

                val values = vars.associateWith { varName ->
                    val value = globals.get(varName)
                    if (!value.isnil()) value.tojstring() else throw Exception("Your card contains '$varName', which your code doesn't")
                }



                Result.success(values)
            }
        } catch (e: TimeoutCancellationException) {
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