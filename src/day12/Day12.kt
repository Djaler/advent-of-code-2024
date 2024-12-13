package day12;

import readInput
import readTestInput

data class Position(val x: Int, val y: Int)

fun Position.top() = Position(x, y - 1)
fun Position.right() = Position(x + 1, y)
fun Position.bottom() = Position(x, y + 1)
fun Position.left() = Position(x - 1, y)

fun Position.neighbours() = listOf(
    top(),
    right(),
    left(),
    bottom(),
)

fun Position.inGrid(grid: List<String>): Boolean {
    return x in grid.indices && y in grid.indices
}

fun List<String>.value(position: Position): Char? {
    return if (position.inGrid(this)) {
        this[position.y][position.x]
    } else {
        null
    }
}

fun Position.walkRegion(
    grid: List<String>,
    checkedNodes: MutableSet<Position>,
    onSameRegion: (() -> Unit)? = null,
    onOtherRegion: (() -> Unit)? = null,
    onCorner: ((Position) -> Unit)? = null,
) {
    neighbours().forEach {
        if (it.inGrid(grid) && grid.value(this) == grid.value(it)) {
            val notChecked = checkedNodes.add(it)
            if (notChecked) {
                onSameRegion?.invoke()
                it.walkRegion(grid, checkedNodes, onSameRegion, onOtherRegion, onCorner)
            }
        } else {
            onOtherRegion?.invoke()
        }
    }

    if (onCorner != null) {
        val value = grid.value(this)
        val top = grid.value(top())
        val right = grid.value(right())
        val left = grid.value(left())
        val bottom = grid.value(bottom())

        if (top != value && right != value) {
            onCorner(this)
        } else if (top == value && right == value && grid.value(top().right()) != value) {
            onCorner(this)
        }
        if (top != value && left != value) {
            onCorner(this)
        } else if (top == value && left == value && grid.value(top().left()) != value) {
            onCorner(this)
        }
        if (bottom != value && right != value) {
            onCorner(this)
        } else if (bottom == value && right == value && grid.value(bottom().right()) != value) {
            onCorner(this)
        }
        if (bottom != value && left != value) {
            onCorner(this)
        } else if (bottom == value && left == value && grid.value(bottom().left()) != value) {
            onCorner(this)
        }
    }
}

fun part1(input: List<String>): Int {
    val checkedNodes = mutableSetOf<Position>()

    var totalPrice = 0

    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, _ ->
            val currentPosition = Position(x, y)
            val notChecked = checkedNodes.add(currentPosition)
            if (notChecked) {
                var regionArea = 1
                var regionPerimeter = 0
                currentPosition.walkRegion(
                    grid = input,
                    checkedNodes,
                    onSameRegion = {
                        regionArea++
                    },
                    onOtherRegion = {
                        regionPerimeter++
                    }
                )
                val regionPrice = regionArea * regionPerimeter
                totalPrice += regionPrice
            }
        }
    }

    return totalPrice
}

fun part2(input: List<String>): Int {
    val checkedNodes = mutableSetOf<Position>()

    var totalPrice = 0

    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, _ ->
            val currentPosition = Position(x, y)
            val notChecked = checkedNodes.add(currentPosition)
            if (notChecked) {
                var regionArea = 1
                var regionCorners = 0
                currentPosition.walkRegion(
                    grid = input,
                    checkedNodes,
                    onSameRegion = {
                        regionArea++
                    },
                    onCorner = {
                        regionCorners++
                    }
                )
                val regionPrice = regionArea * regionCorners
                totalPrice += regionPrice
            }
        }
    }

    return totalPrice
}

fun main() {
    val testInput = readTestInput(day = 12)
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)

    val input = readInput(day = 12)
    println(part1(input))
    println(part2(input))
}
