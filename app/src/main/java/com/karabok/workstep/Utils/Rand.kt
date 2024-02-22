package com.karabok.workstep.Utils

class Rand(private var seed: Long = System.currentTimeMillis() / 1000) {
    private val a: Int = 1103515245
    private val c: Int = 12345
    private val m: Long = 2147483648

    private fun mathRand(){
        seed = (a * seed + c) % m
    }

    fun nextInt(): Long {
        mathRand()
        return seed % Int.MAX_VALUE
    }

    fun nextInt(until: Int): Long {
        mathRand()
        return seed % until
    }

    fun nextInt(from: Int, until: Int): Long {
        mathRand()
        return from + (seed % (until - from + 1))
    }

    fun nextFloat(): Float {
        mathRand()
        return seed.toFloat() / m
    }
}