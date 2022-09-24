import java.io.File
import java.util.*

fun converter(
    states: MutableList<MutableList<MutableList<Int?>>>,
    start: List<Int>,
    finish: List<Int>,
    symbols: Int
): String {
    val ans = StringBuilder("")
    val finishes = mutableListOf<Int>()
    val groups = mutableListOf<List<Int>>()
    val q: Queue<List<Int>> = LinkedList()
    groups.add(start)
    q.add(start)
    if (start.containsAll(finish) && finish.containsAll(start))
        finishes.add(groups.size - 1)
    while (!q.isEmpty()) {
        val from = q.poll()
        for (i in 0 until symbols) {
            val to = mutableSetOf<Int>()
            from.forEach {
                to.addAll(states[it][i].mapNotNull { el -> el })
            }
            val newElem = to.toList().sorted()
            if (!groups.contains(newElem) && to.isNotEmpty()) {
                groups.add(newElem)
                q.add(newElem)
                if (to.any { finish.contains(it) }) //содержит что-то из finish
                    finishes.add(groups.size - 1)
            }
            if (to.isNotEmpty())
                ans.append("${groups.indexOf(from)} $i ${groups.indexOf(newElem)}\n")
        }
    }
    var fin = ""
    finishes.forEach {
        fin = "$fin$it "
    }
    return "${groups.size}\n$symbols\n0\n$fin\n${ans}"
}


fun convertInfo(fileName: String): String {
    val input = Scanner(File(fileName))
    val n: Int = input.nextLine().toInt()
    val m: Int = input.nextLine().toInt()
    val start: List<Int> = input.nextLine().split(' ').map { it.toInt() }
    val finish: List<Int> = input.nextLine().split(' ').map { it.toInt() }
    val states = MutableList(n) { MutableList(m) { mutableListOf<Int?>(null) } }
    while (input.hasNextLine()) {
        val stateInfo = input.nextLine().split(' ').map { it.toInt() }
        states[stateInfo[0]][stateInfo[1]].add(stateInfo[2])
    }
    return converter(states, start, finish, m)
}

fun main(args: Array<String>) {
    println(convertInfo("src/main/resources/convert.txt"))
}
