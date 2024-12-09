package day09;

import readInput
import readTestInput

fun parseBlocks(input: String): List<Int?> {
    return input.flatMapIndexed { index, char ->
        List(char.digitToInt()) {
            if (index % 2 == 0) {
                index / 2
            } else {
                null
            }
        }
    }
}

fun compactBlocks(blocks: List<Int?>): List<Int?> {
    val mutableBlocks = blocks.toMutableList()

    for ((index, value) in blocks.withIndex()) {
        if (value != null) {
            continue
        }
        while (mutableBlocks.last() == null) {
            mutableBlocks.removeLast()
        }
        if (index >= mutableBlocks.size) {
            break
        }
        mutableBlocks[index] = mutableBlocks.removeLast()
    }

    return mutableBlocks
}

fun calculateChecksum(blocks: List<Int?>): Long {
    return blocks
        .withIndex()
        .sumOf { (index, value) ->
            if (value == null) {
                0
            } else {
                index * value.toLong()
            }
        }
}

fun part1(input: String): Long {
    val blocks = parseBlocks(input)

    val compactedBlocks = compactBlocks(blocks)

    return calculateChecksum(compactedBlocks)
}

data class FreeSpace(val start: Int, val end: Int) {
    val size get() = end - start + 1
}

fun findFreeSpaces(blocks: List<Int?>): List<FreeSpace> {
    val freeSpaces = mutableListOf<FreeSpace>()
    var freeSpaceStart: Int? = null
    for ((index, value) in blocks.withIndex()) {
        if (value == null) {
            if (freeSpaceStart == null) {
                freeSpaceStart = index
            }
        } else if (freeSpaceStart != null) {
            freeSpaces.add(FreeSpace(freeSpaceStart, end = index - 1))
            freeSpaceStart = null
        }
    }

    return freeSpaces
}

fun defragmenation(blocks: List<Int?>): List<Int?> {
    val mutableBlocks = blocks.toMutableList()

    val freeSpaces = findFreeSpaces(blocks).toMutableList()

    var fileEnd: Int? = null
    var fileId: Int? = null
    for ((index, value) in blocks.withIndex().reversed()) {
        if (fileEnd != null && (value == null || value != fileId)) {
            val fileSize = fileEnd - index
            val matchingFreeSpaceIndex = freeSpaces.indexOfFirst {
                it.size >= fileSize && it.end <= index
            }
            if (matchingFreeSpaceIndex > -1) {
                val freeSpace = freeSpaces[matchingFreeSpaceIndex]
                for (i in 0 until fileSize) {
                    mutableBlocks[freeSpace.start + i] = fileId
                    mutableBlocks[index + 1 + i] = null
                }
                if (fileSize == freeSpace.size) {
                    freeSpaces.removeAt(matchingFreeSpaceIndex)
                } else {
                    freeSpaces[matchingFreeSpaceIndex] = freeSpaces[matchingFreeSpaceIndex].copy(
                        start = freeSpace.start + fileSize
                    )
                }
            }
            fileEnd = null
        }
        if (fileEnd == null && value != null) {
            fileEnd = index
            fileId = value
        }
    }

    return mutableBlocks
}

fun part2(input: String): Long {
    val blocks = parseBlocks(input)

    val defragmented = defragmenation(blocks)

    return calculateChecksum(defragmented)
}

fun main() {
    val testInput = readTestInput(day = 9).first()
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput(day = 9).first()
    println(part1(input))
    println(part2(input))
}
