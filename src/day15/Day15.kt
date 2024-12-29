package day15;

import readInput
import readTestInput

data class Position(val x: Int, val y: Int)

data class Input(
    val map: List<List<Boolean>>,
    val boxes: Set<Position>,
    val robot: Position,
    val moves: List<Char>,
)

fun Position.atMoveDirection(move: Char) = when (move) {
    '<' -> Position(x - 1, y)
    '>' -> Position(x + 1, y)
    '^' -> Position(x, y - 1)
    'v' -> Position(x, y + 1)
    else -> throw IllegalArgumentException("Unknown move")
}

fun part1(input: List<String>): Int {
    fun parse(input: List<String>): Input {
        val indexOfSpace = input.indexOfFirst { it.isEmpty() }
        val map = mutableListOf<MutableList<Boolean>>()
        val boxes = mutableSetOf<Position>()
        var robot: Position? = null
        input.take(indexOfSpace).forEachIndexed { y, line ->
            map.add(mutableListOf())
            line.forEachIndexed { x, char ->
                map.last().add(char == '#')
                if (char == '@') {
                    robot = Position(x, y)
                } else if (char == 'O') {
                    boxes.add(Position(x, y))
                }
            }
        }

        val moves = mutableListOf<Char>()
        input.drop(indexOfSpace + 1).forEach { line ->
            line.forEach {
                moves.add(it)
            }
        }

        return Input(map, boxes, robot!!, moves)
    }

    val (map, boxes, robot, moves) = parse(input)

    fun Position.isWall() = map[y][x]

    fun Position.canPushBox(move: Char, boxes: Set<Position>): Boolean {
        val targetPosition = this.atMoveDirection(move)
        if (targetPosition in boxes) {
            return targetPosition.canPushBox(move, boxes)
        }
        return !targetPosition.isWall()
    }

    fun Position.pushBox(boxes: MutableSet<Position>, move: Char) {
        val targetPosition = this.atMoveDirection(move)
        if (targetPosition in boxes) {
            targetPosition.pushBox(boxes, move)
        }
        boxes.remove(this)
        boxes.add(targetPosition)
    }

    fun processMove(
        robot: Position,
        boxes: Set<Position>,
        move: Char,
    ): Pair<Position, Set<Position>> {
        val targetPosition = robot.atMoveDirection(move)
        if (targetPosition in boxes) {
            if (targetPosition.canPushBox(move, boxes)) {
                val mutableBoxes = boxes.toMutableSet()
                targetPosition.pushBox(mutableBoxes, move)
                return targetPosition to mutableBoxes
            }
            return robot to boxes
        }
        if (targetPosition.isWall()) {
            return robot to boxes
        }
        return targetPosition to boxes
    }

    var currentRobot = robot
    var currentBoxes = boxes

    moves.forEach { move ->
        val (newRobot, newBoxes) = processMove(currentRobot, currentBoxes, move)
        currentRobot = newRobot
        currentBoxes = newBoxes
    }

    return currentBoxes.sumOf {
        it.x + 100 * it.y
    }
}

data class Box(
    val left: Position,
    val right: Position,
)

data class Input2(
    val map: List<List<Boolean>>,
    val boxes: Set<Box>,
    val robot: Position,
    val moves: List<Char>,
)

fun part2(input: List<String>): Int {
    fun parse(input: List<String>): Input2 {
        val indexOfSpace = input.indexOfFirst { it.isEmpty() }
        val map = mutableListOf<MutableList<Boolean>>()
        val boxes = mutableSetOf<Box>()
        var robot: Position? = null
        input.take(indexOfSpace).forEachIndexed { y, line ->
            map.add(mutableListOf())
            line.forEachIndexed { x, char ->
                map.last().add(char == '#')
                map.last().add(char == '#')
                if (char == '@') {
                    robot = Position(x * 2, y)
                } else if (char == 'O') {
                    boxes.add(
                        Box(
                            Position(x * 2, y),
                            Position(x * 2 + 1, y),
                        )
                    )
                }
            }
        }

        val moves = mutableListOf<Char>()
        input.drop(indexOfSpace + 1).forEach { line ->
            line.forEach {
                moves.add(it)
            }
        }

        return Input2(map, boxes, robot!!, moves)
    }

    val (map, boxes, robot, moves) = parse(input)

    fun Position.isWall() = map[y][x]

    fun Position.boxAtPosition(boxes: Set<Box>) = boxes.find {
        it.left == this || it.right == this
    }

    fun Box.canPushBox(move: Char, boxes: Set<Box>): Boolean {
        val targetPositions = setOf(
            left.atMoveDirection(move),
            right.atMoveDirection(move),
        )

        val boxesAtMoveDirection = targetPositions.mapNotNull {
            it.boxAtPosition(boxes)?.takeIf { box -> box != this }
        }.toSet()

        return targetPositions.none { it.isWall() } && boxesAtMoveDirection.all { it.canPushBox(move, boxes) }
    }

    fun Box.pushBox(boxes: MutableSet<Box>, move: Char) {
        val leftTargetPosition = left.atMoveDirection(move)
        val rightTargetPosition = right.atMoveDirection(move)

        val boxesAtMoveDirection =
            setOfNotNull(
                leftTargetPosition.boxAtPosition(boxes)?.takeUnless { it == this },
                rightTargetPosition.boxAtPosition(boxes)?.takeUnless { it == this }
            )
        if (boxesAtMoveDirection.isNotEmpty()) {
            boxesAtMoveDirection.forEach {
                it.pushBox(boxes, move)
            }
        }
        boxes.remove(this)
        boxes.add(
            Box(
                leftTargetPosition,
                rightTargetPosition,
            )
        )
    }

    fun processMove(
        robot: Position,
        boxes: Set<Box>,
        move: Char,
    ): Pair<Position, Set<Box>> {
        val targetPosition = robot.atMoveDirection(move)
        val boxAtTargetPosition = targetPosition.boxAtPosition(boxes)
        if (boxAtTargetPosition != null) {
            if (boxAtTargetPosition.canPushBox(move, boxes)) {
                val mutableBoxes = boxes.toMutableSet()
                boxAtTargetPosition.pushBox(mutableBoxes, move)
                return targetPosition to mutableBoxes
            }
            return robot to boxes
        }
        if (targetPosition.isWall()) {
            return robot to boxes
        }
        return targetPosition to boxes
    }

    var currentRobot = robot
    var currentBoxes = boxes

    moves.forEach { move ->
        val (newRobot, newBoxes) = processMove(currentRobot, currentBoxes, move)
        currentRobot = newRobot
        currentBoxes = newBoxes
    }

    return currentBoxes.sumOf {
        it.left.x + 100 * it.left.y
    }
}

fun main() {
    val testInput = readTestInput(day = 15)
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)

    val input = readInput(day = 15)
    println(part1(input))
    println(part2(input))
}
