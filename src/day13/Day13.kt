package day13;

import readInput
import readTestInput

data class Button(val xMovement: Int, val yMovement: Int)

data class Position(val x: Long, val y: Long)

data class ClawMachine(
    val buttonA: Button,
    val buttonB: Button,
    val prizeLocation: Position,
)

const val buttonACost = 3
const val buttonBCost = 1

val buttonRegex = Regex("X\\+(\\d+), Y\\+(\\d+)")
val prizeRegex = Regex("X=(\\d+), Y=(\\d+)")

fun parse(input: List<String>): List<ClawMachine> {
    return input.chunked(4).map {
        val buttonA = buttonRegex.find(it[0])!!
        val buttonB = buttonRegex.find(it[1])!!
        val prize = prizeRegex.find(it[2])!!

        ClawMachine(
            buttonA = Button(buttonA.groupValues[1].toInt(), buttonA.groupValues[2].toInt()),
            buttonB = Button(buttonB.groupValues[1].toInt(), buttonB.groupValues[2].toInt()),
            prizeLocation = Position(prize.groupValues[1].toLong(), prize.groupValues[2].toLong()),
        )
    }
}

fun ClawMachine.minimalWinCost(maxButtonPresses: Long): Long {
    val maxBButtonPresses = sequenceOf(
        prizeLocation.x / buttonB.xMovement,
        prizeLocation.y / buttonB.yMovement,
        maxButtonPresses
    ).min()


    for (bPresses in 0..maxBButtonPresses) {
        val buttonBTotalXMovement = buttonB.xMovement * bPresses
        val buttonBTotalYMovement = buttonB.yMovement * bPresses

        val minAButtonPresses = sequenceOf(
            (prizeLocation.x - buttonBTotalXMovement) / buttonA.xMovement,
            (prizeLocation.y - buttonBTotalYMovement) / buttonA.yMovement,
            0
        ).max()
        val maxAButtonPresses = sequenceOf(
            prizeLocation.x / buttonA.xMovement,
            prizeLocation.y / buttonA.yMovement,
            maxButtonPresses
        ).min()

        for (aPresses in minAButtonPresses..maxAButtonPresses) {
            val buttonATotalXMovement = buttonA.xMovement * aPresses
            val buttonATotalYMovement = buttonA.yMovement * aPresses
            if (prizeLocation.x == buttonATotalXMovement + buttonBTotalXMovement &&
                prizeLocation.y == buttonATotalYMovement + buttonBTotalYMovement
            ) {
                return aPresses * buttonACost + bPresses * buttonBCost
            }
        }
    }

    return 0
}

fun part1(input: List<String>): Long {
    val clawMachines = parse(input)

    return clawMachines.sumOf {
        it.minimalWinCost(maxButtonPresses = 100)
    }
}

fun ClawMachine.minimalWinCost(): Long {
    // a * buttonA.xMovement + b * buttonB.xMovement = prizeLocation.x
    // a * buttonA.yMovement + b * buttonB.yMovement = prizeLocation.y

    // b = (prizeLocation.y - a * buttonA.yMovement) / buttonB.yMovement
    // a * buttonA.xMovement * buttonB.yMovement + buttonB.xMovement * (prizeLocation.y - a * buttonA.yMovement) = prizeLocation.x * buttonB.yMovement
    // a * buttonA.xMovement * buttonB.yMovement + buttonB.xMovement * prizeLocation.y - a * buttonA.yMovement * buttonB.xMovement = prizeLocation.x * buttonB.yMovement
    // a * (buttonA.xMovement * buttonB.yMovement - buttonA.yMovement * buttonB.xMovement) = prizeLocation.x * buttonB.yMovement - buttonB.xMovement * prizeLocation.y
    // a = (prizeLocation.x * buttonB.yMovement - buttonB.xMovement * prizeLocation.y) / (buttonA.xMovement * buttonB.yMovement - buttonA.yMovement * buttonB.xMovement)

    val a = (prizeLocation.x * buttonB.yMovement - buttonB.xMovement * prizeLocation.y).toDouble() /
        (buttonA.xMovement * buttonB.yMovement - buttonA.yMovement * buttonB.xMovement)
    val b = (prizeLocation.y - a * buttonA.yMovement) / buttonB.yMovement

    return if (a >= 0 && b >= 0 && a % 1 == 0.0 && b % 1 == 0.0) {
        a.toLong() * buttonACost + b.toLong() * buttonBCost
    } else {
        0
    }
}

fun part2(input: List<String>): Long {
    val clawMachines = parse(input).map {
        it.copy(
            prizeLocation = Position(
                it.prizeLocation.x + 10000000000000,
                it.prizeLocation.y + 10000000000000,
            )
        )
    }

    return clawMachines.sumOf {
        it.minimalWinCost()
    }
}

fun main() {
    val testInput = readTestInput(day = 13)
    check(part1(testInput) == 480L)

    val input = readInput(day = 13)
    println(part1(input))
    println(part2(input))
}
