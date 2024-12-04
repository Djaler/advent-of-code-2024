fun main() {
    data class Direction(
        val xStep: Int,
        val yStep: Int,
    )

    data class Cell(val x: Int, val y: Int, var value: Char)

    val allDirections = listOf(
        Direction(-1, -1),
        Direction(0, -1),
        Direction(1, -1),
        Direction(1, 0),
        Direction(1, 1),
        Direction(0, 1),
        Direction(-1, 1),
        Direction(-1, 0),
    )

    fun directionIsPossible(direction: Direction, cell: Cell, gridSize: Int, lineLength: Int): Boolean {
        return cell.x + direction.xStep * (lineLength - 1) in 0..<gridSize &&
            cell.y + direction.yStep * (lineLength - 1) in 0..<gridSize
    }

    fun valueAtDirection(input: List<String>, cell: Cell, direction: Direction, distance: Int): Char {
        return input[cell.y + direction.yStep * distance][cell.x + direction.xStep * distance]
    }

    fun wordExistAtDirection(input: List<String>, cell: Cell, direction: Direction, word: String): Boolean {
        return word.withIndex().drop(1)
            .all { (index, char) ->
                valueAtDirection(input, cell, direction, distance = index) == char
            }
    }

    fun part1(input: List<String>): Int {
        val word = "XMAS"

        return input
            .flatMapIndexed { y, line ->
                line.mapIndexed { x, char -> Cell(x, y, char) }
            }
            .filter { it.value == word.first() }
            .flatMap { cell ->
                allDirections
                    .filter { directionIsPossible(it, cell, gridSize = input.size, lineLength = word.length) }
                    .map { direction -> cell to direction }
            }
            .count { (cell, direction) ->
                wordExistAtDirection(input, cell, direction, word)
            }
    }

    val diagonalDirections = listOf(
        Direction(-1, -1),
        Direction(1, -1),
        Direction(1, 1),
        Direction(-1, 1),
    )

    fun Direction.inversed() = Direction(-xStep, -yStep)

    fun part2(input: List<String>): Int {
        return input
            .flatMapIndexed { y, line ->
                line.mapIndexed { x, char -> Cell(x, y, char) }
            }
            .filter { it.value == 'A' }
            .count { cell ->
                diagonalDirections
                    .filter {
                        directionIsPossible(it, cell, gridSize = input.size, lineLength = 2) &&
                            directionIsPossible(it.inversed(), cell, gridSize = input.size, lineLength = 2)
                    }
                    .count {
                        valueAtDirection(input, cell, it, distance = 1) == 'M' &&
                            valueAtDirection(input, cell, it.inversed(), distance = 1) == 'S'
                    } == 2
            }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
