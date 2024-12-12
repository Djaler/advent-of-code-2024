package day11;

import readInput
import readTestInput

fun blink1(stones: List<Long>): List<Long> {
    return stones.flatMap {
        val string = it.toString()
        when {
            it == 0L -> listOf(1)
            string.length % 2 == 0 -> {
                val leftStoneNumber = string.substring(0, string.length / 2).toLong()
                val rightStoneNumber = string.substring(string.length / 2).toLong()
                listOf(leftStoneNumber, rightStoneNumber)
            }

            else -> listOf(it * 2024)
        }
    }
}

fun part1(input: String): Int {
    var stones = input.split(" ").map { it.toLong() }

    repeat(25) {
        stones = blink1(stones)
    }

    return stones.count()
}

fun blink2(stonesCounts: Map<Long, Long>): Map<Long, Long> {
    val newCounts = mutableMapOf<Long, Long>()

    stonesCounts.forEach { (stoneNumber, count) ->
        val string = stoneNumber.toString()
        when {
            stoneNumber == 0L -> {
                val newNumber = 1L
                newCounts[newNumber] = (newCounts[newNumber] ?: 0) + count
            }

            string.length % 2 == 0 -> {
                val leftStoneNumber = string.substring(0, string.length / 2).toLong()
                val rightStoneNumber = string.substring(string.length / 2).toLong()

                newCounts[leftStoneNumber] = (newCounts[leftStoneNumber] ?: 0) + count
                newCounts[rightStoneNumber] = (newCounts[rightStoneNumber] ?: 0) + count
            }

            else -> {
                val newNumber = stoneNumber * 2024
                newCounts[newNumber] = (newCounts[newNumber] ?: 0) + count
            }
        }
    }

    return newCounts
}

fun part2(input: String): Long {
    var stonesCounts = input.split(" ").map { it.toLong() }
        .groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    repeat(75) {
        stonesCounts = blink2(stonesCounts)
    }

    return stonesCounts.values.sum()
}

fun main() {
    val testInput = readTestInput(day = 11).first()
    check(part1(testInput) == 55312)

    val input = readInput(day = 11).first()
    println(part1(input))
    println(part2(input))
}
