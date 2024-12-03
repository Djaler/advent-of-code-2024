import kotlin.math.abs

fun main() {
    fun allIncreasingWithAllowedDifference(levels: List<Int>, allowedDifference: IntRange): Boolean {
        return levels.zipWithNext().all { (current, next) ->
            next > current && (abs(next - current) in allowedDifference)
        }
    }

    fun part1(input: List<String>): Int {
        val allowedDifference = 1..3

        return input.count { line ->
            val levels = line.split(" ").map { it.toInt() }

            allIncreasingWithAllowedDifference(levels, allowedDifference) ||
                allIncreasingWithAllowedDifference(levels.asReversed(), allowedDifference)
        }
    }

    fun variantsWithoutOneLevel(levels: List<Int>): List<List<Int>> {
        return List(levels.size) { index ->
            levels.toMutableList().apply {
                removeAt(index)
            }
        }
    }

    fun part2(input: List<String>): Int {
        val allowedDifference = 1..3

        return input.count { line ->
            val levels = line.split(" ").map { it.toInt() }

            allIncreasingWithAllowedDifference(levels, allowedDifference) ||
                allIncreasingWithAllowedDifference(levels.asReversed(), allowedDifference) ||
                variantsWithoutOneLevel(levels).any { levelsVariant ->
                    allIncreasingWithAllowedDifference(levelsVariant, allowedDifference) ||
                        allIncreasingWithAllowedDifference(levelsVariant.asReversed(), allowedDifference)
                }
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
