import java.io.File
import java.util.*

fun converter(
    automaton: Automaton
): Automaton {
    val edges: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
    val finishes = mutableListOf<Int>()
    val groups = mutableListOf<List<Int>>()
    val q: Queue<List<Int>> = LinkedList()
    groups.add(automaton.start)
    q.add(automaton.start)
    if (automaton.start.any { automaton.finish.contains(it) })
        finishes.add(groups.size - 1)
    while (!q.isEmpty()) {
        val from = q.poll()
        for (i in 0 until automaton.alphabetSize) {
            val to = mutableSetOf<Int>()
            from.forEach {
                to.addAll(automaton.states[it][i])
            }
            val newElem = to.toList().sorted()
            if (!groups.contains(newElem) && to.isNotEmpty()) {
                groups.add(newElem)
                q.add(newElem)
                if (to.any { automaton.finish.contains(it) }) //содержит что-то из finish
                    finishes.add(groups.size - 1)
            }
            if (to.isNotEmpty()) {
                val stateFrom = groups.indexOf(from)
                if (edges.size <= stateFrom)
                    edges.addAll(List(stateFrom - edges.size + 1) { MutableList(automaton.alphabetSize) { mutableListOf() } })
                val symbol = i
                val stateTo = groups.indexOf(newElem)
                edges[stateFrom][symbol].add(stateTo)

            }
        }
    }
    while (edges.size < groups.size)
        edges.add(MutableList(automaton.alphabetSize) { mutableListOf() })
    return Automaton(groups.size, automaton.alphabetSize, listOf(0), finishes, edges)
}

class Automaton(
    var stateNumber: Int,
    var alphabetSize: Int,
    var start: List<Int>,
    var finish: List<Int>,
    var states: MutableList<MutableList<MutableList<Int>>>
) {
    override fun toString(): String {
        val ans = StringBuilder("")
        ans.append("${stateNumber}\n")
        ans.append("${alphabetSize}\n")
        start.forEachIndexed { ind, it ->
            if (ind != 0)
                ans.append(" ")
            ans.append(it)
        }
        ans.trim()
        ans.append("\n")
        finish.forEachIndexed { ind, it ->
            if (ind != 0)
                ans.append(" ")
            ans.append(it)
        }
        ans.trim()
        ans.append("\n")
        states.forEachIndexed { stateIndex, state ->
            state.forEachIndexed { chrIndex, chr ->
                chr.forEach {
                    ans.append("$stateIndex $chrIndex ${it}\n")
                }
            }
        }
        return ans.toString()
    }
}


fun convertInfo(fileName: String): Automaton {
    val input = Scanner(File(fileName))
    val n: Int = input.nextLine().toInt()
    val m: Int = input.nextLine().toInt()
    val start: List<Int> = input.nextLine().split(' ').map { it.toInt() }
    val finish: List<Int> = input.nextLine().split(' ').map { it.toInt() }
    val states = MutableList(n) { MutableList(m) { mutableListOf<Int>() } }
    while (input.hasNextLine()) {
        val stateInfo = input.nextLine().split(' ').map { it.toInt() }
        states[stateInfo[0]][stateInfo[1]].add(stateInfo[2])
    }

    return Automaton(n, m, start, finish, states)
}

fun bfs(nodes: Automaton, u: Int, used: MutableList<Boolean>) {
    val queue = mutableListOf<Int>()
    queue.add(u)
    used[u] = true
    while (queue.isNotEmpty()) {
        val current = queue.removeAt(0)
        nodes.states[current].forEach {
            it.forEach { elem ->
                if (!used[elem]) {
                    queue.add(elem)
                    used[elem] = true
                }
            }
        }
    }
}

fun minimisation(automaton: Automaton) : Automaton {
    val newAutomaton = converter(automaton)
    val used = MutableList(newAutomaton.stateNumber) { false }
    bfs(newAutomaton, 0, used)
    newAutomaton.states = newAutomaton.states.filterIndexed { index, _ -> used[index] }.toMutableList()
    newAutomaton.finish = newAutomaton.finish.filterIndexed { index, _ -> used[index] }.toMutableList()
    newAutomaton.start = newAutomaton.start.filterIndexed { index, _ -> used[index] }.toMutableList()
    newAutomaton.stateNumber = newAutomaton.states.size

    var classByElem: MutableList<Int> = MutableList(newAutomaton.stateNumber) { 0 }
    newAutomaton.finish.forEach { classByElem[it] = 1 }

    var elementsByClass: MutableList<MutableList<Int>> = MutableList(2) { mutableListOf() }
    (0 until newAutomaton.stateNumber).forEach {
        elementsByClass[if (newAutomaton.finish.contains(it)) 1 else 0].add(it)
    }
    var canSplit = true
    while (canSplit) {
        canSplit = false
        run algo@{
            elementsByClass.forEach { currentClass ->
                for (chr in 0 until newAutomaton.alphabetSize) {

                    var classes: MutableList<MutableList<Int>> =
                        MutableList(newAutomaton.stateNumber + 1) { mutableListOf() }
                    currentClass.forEach lbl@{
                        if (newAutomaton.states[it][chr].isEmpty()) {
                            classes[0].add(it)
                            return@lbl
                        }
                        classes[classByElem[newAutomaton.states[it][chr].first()] + 1].add(it)
                    }
                    classes = classes.filter { it.isNotEmpty() }.toMutableList()

                    if (classes.size > 1) {
                        elementsByClass = elementsByClass.filter { it != currentClass }.toMutableList()
                        elementsByClass.addAll(classes)
                        classByElem = MutableList(elementsByClass.maxOf { it.maxOf { it } } + 1) { -1 }
                        elementsByClass.forEachIndexed { ind, elems ->
                            elems.forEach {
                                classByElem[it] = ind
                            }
                        }
                        return@algo;

                    }
                }
            }
        }
    }
    val resultStates: MutableList<MutableList<MutableList<Int>>> = MutableList(elementsByClass.size){ MutableList(newAutomaton.alphabetSize){ mutableListOf()} }
    elementsByClass.forEachIndexed { index, elements ->
        for (chr in 0 until newAutomaton.alphabetSize)
            resultStates[index][chr].add(classByElem[newAutomaton.states[elements[0]][chr][0]])
    }
    val start = mutableListOf<Int>()
    val finish = mutableListOf<Int>()
    elementsByClass.forEachIndexed { index, state->
        if (state.any {newAutomaton.start.contains(it)})
            start.add(index)
        if (state.any {newAutomaton.finish.contains(it)})
            finish.add(index)
    }
    return Automaton(elementsByClass.size,newAutomaton.alphabetSize, start, finish, resultStates)
}

fun main(args: Array<String>) {
    val automaton = convertInfo("src/main/resources/convert.txt");
    println(minimisation(automaton).toString())
}
