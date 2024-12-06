private sealed interface Instruction

private data object Do : Instruction
private data object Dont : Instruction
data class Mul(val left: Int, val right: Int) : Instruction

fun main() {
    fun part1(input: String): Int {
        val regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()
        return regex.findAll(input)
            .sumOf {
                it.groupValues[1].toInt() * it.groupValues[2].toInt()
            }
    }

    fun part2(input: String): Int {
        val regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)|don't\\(\\)|do\\(\\)".toRegex()
        val instructions = regex.findAll(input)
            .map {
                when (it.value) {
                    "do()" -> Do
                    "don't()" -> Dont
                    else -> Mul(it.groupValues[1].toInt(), it.groupValues[2].toInt())
                }
            }

        var enabled = true
        var sum = 0
        instructions.forEach {
            when (it) {
                Do -> enabled = true
                Dont -> enabled = false
                is Mul -> if (enabled) sum += it.left * it.right
            }
        }
        return sum
    }

    val testInput = readInput("Day03_test").joinToString("")
    check(part1(testInput) == 161)

    val input = readInput("Day03").joinToString("")
    part1(input).println()
    part2(input).println()
}