package day14;

import readInput
import readTestInput

data class Position(val x: Int, val y: Int)
data class Velocity(val x: Int, val y: Int)

data class Robot(
    val position: Position,
    val velocity: Velocity,
)

val robotRegex = Regex("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)")

fun parse(input: List<String>): List<Robot> {
    return input.map { line ->
        val matchResult = robotRegex.find(line)!!
        Robot(
            Position(
                matchResult.groupValues[1].toInt(),
                matchResult.groupValues[2].toInt(),
            ),
            Velocity(
                matchResult.groupValues[3].toInt(),
                matchResult.groupValues[4].toInt(),
            )
        )
    }
}

fun tick(robot: Robot, width: Int, height: Int): Robot {
    var x = robot.position.x + robot.velocity.x
    var y = robot.position.y + robot.velocity.y
    if (x < 0) {
        x += width
    } else if (x >= width) {
        x -= width
    }
    if (y < 0) {
        y += height
    } else if (y >= height) {
        y -= height
    }
    return robot.copy(
        position = Position(x, y)
    )
}

enum class Quadrant {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_RIGHT,
    BOTTOM_LEFT,
}

fun countInQuadrant(quadrant: Quadrant, robots: List<Robot>, width: Int, height: Int): Int {
    return robots.count {
        when (quadrant) {
            Quadrant.TOP_LEFT -> it.position.x in 0..<width / 2 && it.position.y in 0..<height / 2
            Quadrant.TOP_RIGHT -> it.position.x in width / 2 + 1..<width && it.position.y in 0..<height / 2
            Quadrant.BOTTOM_RIGHT -> it.position.x in width / 2 + 1..<width && it.position.y in height / 2 + 1..<height
            Quadrant.BOTTOM_LEFT -> it.position.x in 0..<width / 2 && it.position.y in height / 2 + 1..<height
        }
    }
}

fun part1(input: List<String>, width: Int, height: Int): Int {
    var robots = parse(input)

    repeat(100) {
        robots = robots.map { tick(it, width, height) }
    }

    return Quadrant.entries.fold(1) { acc, quadrant ->
        acc * countInQuadrant(quadrant, robots, width, height)
    }
}

fun List<Robot>.print(width: Int, height: Int) {
    for (y in 0 until height) {
        for (x in 0 until width) {
            if (any { it.position.x == x && it.position.y == y }) {
                print("&")
            } else {
                print(".")
            }
        }
        println()
    }
}

fun part2(input: List<String>, width: Int, height: Int) {
    var robots = parse(input)

    var seconds = 0
    while (true) {
        robots = robots.map { tick(it, width, height) }
        println(++seconds)
        if ((seconds - 31) % 103 == 0 && (seconds - 68) % 101 == 0) {
            robots.print(width, height)
            readln()
        }
    }
}

fun main() {
    val testInput = readTestInput(day = 14)
    check(part1(testInput, width = 11, height = 7) == 12)

    val input = readInput(day = 14)
    println(part1(input, width = 101, height = 103))
    part2(input, width = 101, height = 103)
}
