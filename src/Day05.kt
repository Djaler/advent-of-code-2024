fun main() {
    fun parse(input: List<String>): Pair<Map<String, Set<String>>, List<List<String>>> {
        val rules = mutableMapOf<String, MutableSet<String>>()
        val updates = mutableListOf<List<String>>()

        var rulesSection = true
        input.forEach {
            if (it.isEmpty()) {
                rulesSection = false
            } else if (rulesSection) {
                val (before, after) = it.split("|")
                rules.getOrPut(before) { mutableSetOf() }
                    .add(after)
            } else {
                val update = it.split(",")
                updates.add(update)
            }
        }

        return rules to updates
    }

    fun isCorrectlyOrdered(update: List<String>, rules: Map<String, Set<String>>): Boolean {
        update.forEachIndexed { index, pageNumber ->
            rules[pageNumber]?.let { shouldBeAfter ->
                if (shouldBeAfter.any { update.indexOf(it) in 0..<index }) {
                    return false
                }
            }
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val (rules, updates) = parse(input)

        return updates
            .filter { update -> isCorrectlyOrdered(update, rules) }
            .sumOf { update ->
                update[update.size / 2].toInt()
            }
    }

    fun ruleBasedComparator(rules: Map<String, Set<String>>): Comparator<String> {
        return Comparator { o1, o2 ->
            if (o1 in rules && o2 in rules[o1]!! ||
                o2 in rules && o1 !in rules[o2]!!
            ) {
                -1
            } else {
                1
            }
        }
    }

    fun part2(input: List<String>): Int {
        val (rules, updates) = parse(input)

        return updates
            .filter { update -> !isCorrectlyOrdered(update, rules) }
            .sumOf { update ->
                val correctlyOrdered = update.sortedWith(ruleBasedComparator(rules))

                correctlyOrdered[update.size / 2].toInt()
            }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
