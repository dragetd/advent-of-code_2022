package solution

fun solve1a(rawInput: String): String {
    // one for each elf
    val supplyList = rawInput
        .split("\n\n")
        .map { it ->
            it.split("\n")
                .sumOf { it.toInt() }
        }

    return supplyList.max().toString()
}

fun solve1b(rawInput: String): String {
    // one for each elf
    val supplyList = rawInput
        .split("\n\n")
        .map { it ->
            it.split("\n")
                .sumOf { it.toInt() }
        }
        .sorted()
        .takeLast(3)

    return supplyList.sum().toString()
}
