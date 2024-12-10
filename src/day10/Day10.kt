package day10;

import readInput
import readTestInput

data class Position(val x: Int, val y: Int)

fun List<List<Int>>.trailheads(): List<Position> {
    val trailheads = mutableListOf<Position>()

    this.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == 0) {
                trailheads.add(Position(x, y))
            }
        }
    }

    return trailheads
}

fun Position.neighbours(gridSize: Int): List<Position> {
    return listOf(
        Position(x - 1, y),
        Position(x, y - 1),
        Position(x + 1, y),
        Position(x, y + 1),
    ).filter {
        it.x in 0..<gridSize && it.y in 0..<gridSize
    }
}

fun List<List<Int>>.trailEnds(position: Position): Set<Position> {
    val neighbours = position.neighbours(gridSize = this.size)

    val currentValue = this[position.y][position.x]

    return neighbours
        .filter { this[it.y][it.x] == currentValue + 1 }
        .flatMap {
            if (this[it.y][it.x] == 9) {
                setOf(it)
            } else {
                this.trailEnds(it)
            }
        }
        .toSet()
}

fun part1(input: List<String>): Int {
    val grid = input.map { line ->
        line.map { it.digitToInt() }
    }

    return grid.trailheads()
        .sumOf {
            grid.trailEnds(it).size
        }
}

fun List<List<Int>>.trails(position: Position, path: List<Position> = listOf(position)): Set<List<Position>> {
    val neighbours = position.neighbours(gridSize = this.size)

    val currentValue = this[position.y][position.x]

    return neighbours
        .filter { this[it.y][it.x] == currentValue + 1 }
        .flatMap {
            if (this[it.y][it.x] == 9) {
                setOf(path + it)
            } else {
                this.trails(it, path + it)
            }
        }
        .toSet()
}

fun part2(input: List<String>): Int {
    val grid = input.map { line ->
        line.map { it.digitToInt() }
    }

    return grid.trailheads()
        .sumOf {
            grid.trails(it).size
        }
}

fun main() {
    val testInput = readTestInput(day = 10)
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput(day = 10)
    println(part1(input))
    println(part2(input))
}
