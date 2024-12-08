package day06;

import readInput
import readTestInput

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

const val START = '^'
const val OBSTRUCTION = '#'

data class Position(val x: Int, val y: Int)

fun findStart(map: List<String>): Position {
    map.forEachIndexed { y, line ->
        val x = line.indexOf(START)
        if (x >= 0) {
            return Position(x, y)
        }
    }

    throw IllegalArgumentException("No start found")
}

fun positionAtDirection(currentPosition: Position, direction: Direction): Position {
    return when (direction) {
        Direction.UP -> Position(currentPosition.x, currentPosition.y - 1)
        Direction.DOWN -> Position(currentPosition.x, currentPosition.y + 1)
        Direction.LEFT -> Position(currentPosition.x - 1, currentPosition.y)
        Direction.RIGHT -> Position(currentPosition.x + 1, currentPosition.y)
    }
}

fun getValueAtPosition(map: List<String>, position: Position): Char? {
    if (position.y !in map.indices || position.x !in map[position.y].indices) {
        return null
    }
    return map[position.y][position.x]
}

fun Direction.turnRight(): Direction {
    return when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
    }
}

fun part1(map: List<String>): Int {
    val visitedPositions = mutableSetOf<Position>()

    var currentDirection = Direction.UP

    var currentPosition = findStart(map)
    visitedPositions.add(currentPosition)

    while (true) {
        val positionAtDirection = positionAtDirection(currentPosition, currentDirection)
        val valueAtDirection = getValueAtPosition(map, positionAtDirection) ?: break
        if (valueAtDirection == OBSTRUCTION) {
            currentDirection = currentDirection.turnRight()
        } else {
            currentPosition = positionAtDirection
            visitedPositions.add(currentPosition)
        }
    }

    return visitedPositions.size
}

fun willStuck(map: List<String>, start: Position, newObstruction: Position): Boolean {
    val visitedPositionsWithDirections = mutableSetOf<Pair<Position, Direction>>()

    var currentDirection = Direction.UP

    var currentPosition = start
    visitedPositionsWithDirections.add(currentPosition to currentDirection)

    while (true) {
        val positionAtDirection = positionAtDirection(currentPosition, currentDirection)
        val valueAtDirection = getValueAtPosition(map, positionAtDirection) ?: break
        if (valueAtDirection == OBSTRUCTION || positionAtDirection == newObstruction) {
            currentDirection = currentDirection.turnRight()
            if (currentPosition to currentDirection in visitedPositionsWithDirections) {
                return true
            }
        } else {
            currentPosition = positionAtDirection
            if (currentPosition to currentDirection in visitedPositionsWithDirections) {
                return true
            }
            visitedPositionsWithDirections.add(currentPosition to currentDirection)
        }
    }

    return false
}

fun part2(map: List<String>): Int {
    var currentDirection = Direction.UP

    val start = findStart(map)
    var currentPosition = start

    val newObstructionVariants = mutableSetOf<Position>()

    while (true) {
        val positionAtDirection = positionAtDirection(currentPosition, currentDirection)
        val valueAtDirection = getValueAtPosition(map, positionAtDirection) ?: break
        if (valueAtDirection == OBSTRUCTION) {
            currentDirection = currentDirection.turnRight()
        } else {
            if (willStuck(map, start, newObstruction = positionAtDirection)) {
                newObstructionVariants.add(positionAtDirection)
            }
            currentPosition = positionAtDirection
        }
    }

    return newObstructionVariants.size
}

fun main() {
    val testInput = readTestInput(day = 6)
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput(day = 6)
    println(part1(input))
    println(part2(input))
}
