package day01;

import readInput
import readTestInput
import kotlin.math.abs

fun parse(input: List<String>): Pair<List<Int>, List<Int>> {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()

    input.forEach { line ->
        val numbers = line.split("   ")
        left.add(numbers[0].toInt())
        right.add(numbers[1].toInt())
    }

    return left to right
}

fun part1(input: List<String>): Int {
    val (left, right) = parse(input)

    return left.sorted().zip(right.sorted())
        .sumOf { abs(it.first - it.second) }
}

fun part2(input: List<String>): Int {
    val (left, right) = parse(input)

    return left.sumOf { leftNumber ->
        leftNumber * right.count { it == leftNumber }
    }
}

fun main() {
    val testInput = readTestInput(day = 1)
    check(part1(testInput) == 11)

    val input = readInput(day = 1)
    println(part1(input))
    println(part2(input))
}
