package com.rombsquare.code_parser

sealed class Node {
    data class BiOper(val op: Operation, val left: Node, val right: Node)
    data class Num(val value: Double)
}

