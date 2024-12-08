package day08;

import readInput
import readTestInput

data class Antenna(
    val x: Int,
    val y: Int,
    val frequency: Char,
)

data class Antinode(
    val x: Int,
    val y: Int,
)

fun parse(input: List<String>): List<Antenna> {
    val antennas = mutableListOf<Antenna>()

    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, frequency ->
            if (frequency != '.') {
                antennas.add(Antenna(x, y, frequency))
            }
        }
    }

    return antennas
}

fun antennasPairs(antennas: List<Antenna>): Set<Pair<Antenna, Antenna>> {
    val pairs = mutableSetOf<Pair<Antenna, Antenna>>()

    antennas.forEach { firstAntenna ->
        antennas.forEach { secondAntenna ->
            if (firstAntenna.frequency == secondAntenna.frequency && firstAntenna != secondAntenna) {
                if (secondAntenna to firstAntenna !in pairs) {
                    pairs.add(firstAntenna to secondAntenna)
                }
            }
        }
    }

    return pairs
}

fun antinodes(firstAntenna: Antenna, secondAntenna: Antenna, gridSize: Int): List<Antinode> {
    val xStep = secondAntenna.x - firstAntenna.x
    val yStep = secondAntenna.y - firstAntenna.y

    return listOf(
        Antinode(secondAntenna.x + xStep, secondAntenna.y + yStep),
        Antinode(firstAntenna.x - xStep, firstAntenna.y - yStep),
    ).filter {
        it.x in 0..<gridSize && it.y in 0..<gridSize
    }
}

fun part1(input: List<String>): Int {
    val antennas = parse(input)

    return antennasPairs(antennas)
        .flatMap {
            antinodes(it.first, it.second, gridSize = input.size)
        }
        .toSet()
        .count()
}

fun antinodes2(firstAntenna: Antenna, secondAntenna: Antenna, gridSize: Int): List<Antinode> {
    val xStep = secondAntenna.x - firstAntenna.x
    val yStep = secondAntenna.y - firstAntenna.y

    val distanceSequence = generateSequence(0) { it + 1 }

    val firstAntinodes = distanceSequence
        .map { index ->
            Antinode(firstAntenna.x - xStep * index, firstAntenna.y - yStep * index)
        }
        .takeWhile {
            it.x in 0..<gridSize && it.y in 0..<gridSize
        }

    val secondAntinodes = distanceSequence
        .map { index ->
            Antinode(secondAntenna.x + xStep * index, secondAntenna.y + yStep * index)
        }
        .takeWhile {
            it.x in 0..<gridSize && it.y in 0..<gridSize
        }

    return (firstAntinodes + secondAntinodes).toList()
}

fun part2(input: List<String>): Int {
    val antennas = parse(input)

    return antennasPairs(antennas)
        .flatMap {
            antinodes2(it.first, it.second, gridSize = input.size)
        }
        .toSet()
        .count()
}

fun main() {
    val testInput = readTestInput(day = 8)
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput(day = 8)
    println(part1(input))
    println(part2(input))
}
