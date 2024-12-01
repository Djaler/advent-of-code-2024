import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()

        input.forEach { line ->
            val numbers = line.split("   ")
            left.add(numbers[0].toInt())
            right.add(numbers[1].toInt())
        }

        return left.sorted().zip(right.sorted())
            .sumOf { abs(it.first - it.second) }
    }

    fun part2(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()

        input.forEach { line ->
            val numbers = line.split("   ")
            left.add(numbers[0].toInt())
            right.add(numbers[1].toInt())
        }

        return left.sumOf { leftNumber ->
            leftNumber * right.count { it == leftNumber }
        }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
