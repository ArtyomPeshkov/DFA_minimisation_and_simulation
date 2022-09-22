import java.io.File
import java.util.*

fun simulatorTM(
    states: MutableList<MutableList<MutableList<Int?>>>,
    start: List<Int>,
    finish: List<Int>,
    string: List<Int>
): Boolean {
    val q: Queue<Pair<Int, Int>> = LinkedList()
    start.forEach {
        q.add(Pair(0, it))
    }
    while (!q.isEmpty()) {
        val element = q.poll()
        if (finish.contains(element.second) and (string.size == element.first))
            return true
        if (element.first < string.size)
            states[element.second][string[element.first]].mapNotNull { it }.forEach {
                q.add(Pair(element.first + 1, it))
            }

    }
    return false
}

fun simulator(fileName: String): Boolean{
    val input = Scanner(File(fileName))
    val string: List<Int> = input.nextLine().split(' ').map { it.toInt() }
    val n: Int = input.nextLine().toInt()
    val m: Int = input.nextLine().toInt()
    val start: List<Int> = input.nextLine().split(' ').map { it.toInt() }
    val finish: List<Int> = input.nextLine().split(' ').map { it.toInt() }
    val states = MutableList(n) { MutableList(m) { mutableListOf<Int?>(null) } }
    while (input.hasNextLine()) {
        val stateInfo = input.nextLine().split(' ').map { it.toInt() }
        states[stateInfo[0]][stateInfo[1]].add(stateInfo[2])
    }
    return simulatorTM(states, start, finish, string)
}

fun main(args: Array<String>) {
    simulator("src/main/resources/sample.txt")
}