import kotlin.math.pow

enum class Operator {
    ADD,
    MULTIPLY,
    CONCAT,
}

fun main() {
    data class Equation(
        val testValue: Long,
        val numbers: List<Long>,
    )

    fun operatorsVariants(allowedOperators: List<Operator>, operatorsCount: Int): List<List<Operator>> {
        val variantsNumber = allowedOperators.size.toDouble().pow(operatorsCount).toInt()

        return (0..variantsNumber)
            .map { it.toString(radix = allowedOperators.size).padStart(operatorsCount, padChar = '0') }
            .map {
                it.map { char ->
                    allowedOperators[char.digitToInt()]
                }
            }
    }

    fun solve(equation: Equation, operators: List<Operator>): Boolean {
        val result = equation.numbers.drop(1).zip(operators)
            .fold(initial = equation.numbers.first()) { acc, (number, operator) ->
                when (operator) {
                    Operator.ADD -> acc + number

                    Operator.MULTIPLY -> acc * number

                    Operator.CONCAT -> "$acc$number".toLong()
                }
            }

        return equation.testValue == result
    }

    fun canBeSolved(equation: Equation, allowedOperators: List<Operator>): Boolean {
        return operatorsVariants(allowedOperators, operatorsCount = equation.numbers.size - 1)
            .any { operators -> solve(equation, operators) }
    }

    fun parse(input: List<String>): List<Equation> {
        return input
            .map { line ->
                val (testValue, numbers) = line.split(": ")

                Equation(
                    testValue.toLong(),
                    numbers.split(" ").map { it.toLong() }
                )
            }
    }

    fun part1(input: List<String>): Long {
        return parse(input)
            .filter { canBeSolved(it, allowedOperators = listOf(Operator.ADD, Operator.MULTIPLY)) }
            .sumOf { it.testValue }
    }

    fun part2(input: List<String>): Long {
        return parse(input)
            .filter { canBeSolved(it, allowedOperators = listOf(Operator.ADD, Operator.MULTIPLY, Operator.CONCAT)) }
            .sumOf { it.testValue }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
