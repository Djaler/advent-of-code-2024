import kotlin.io.path.Path
import kotlin.io.path.readText

fun readInput(day: Int) =
    Path("src/day${dayNumberString(day)}/Day${dayNumberString(day)}.txt").readText().trim().lines()

fun readTestInput(day: Int) =
    Path("src/day${dayNumberString(day)}/Day${dayNumberString(day)}_test.txt").readText().trim().lines()

fun dayNumberString(day: Int) = day.toString().padStart(2, '0')
