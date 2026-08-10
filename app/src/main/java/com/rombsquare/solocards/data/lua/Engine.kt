package com.rombsquare.solocards.data.lua

import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.Bit32Lib
import org.luaj.vm2.lib.PackageLib
import org.luaj.vm2.lib.StringLib
import org.luaj.vm2.lib.TableLib
import org.luaj.vm2.lib.jse.JseBaseLib
import org.luaj.vm2.lib.jse.JseMathLib
import org.luaj.vm2.compiler.LuaC

object LuaEngine {

    fun createSandbox(): Globals {
        val globals = Globals()

        // Load only safe, essential libraries
        globals.load(JseBaseLib())
        globals.load(PackageLib())
        globals.load(Bit32Lib())
        globals.load(TableLib())
        globals.load(StringLib())
        globals.load(JseMathLib())

        LuaC.install(globals)

        return globals
    }

    fun executeScript(script: String, configureEnv: (Globals) -> Unit = {}): String {
        return try {
            val globals = createSandbox()

            configureEnv(globals)

            val chunk = globals.load(script)
            val result = chunk.call()

            result.toString()
        } catch (e: Exception) {
            "Error: ${e.localizedMessage}"
        }
    }
}

