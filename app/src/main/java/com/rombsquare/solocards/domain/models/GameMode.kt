package com.rombsquare.solocards.domain.models

enum class GameMode(val string: String) {
    Flip("Flip mode"),
    Writing("Writing mode"),
    Boolean("Boolean mode"),
    Option("Option mode"),
    Mixed("Mixed mode");

    // Get random mode except Mixed
    companion object {
        fun random(): GameMode {
            return entries.filterNot { it == Mixed }.random()
        }
    }
}