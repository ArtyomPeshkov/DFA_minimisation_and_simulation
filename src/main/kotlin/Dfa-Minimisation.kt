import java.io.File
import java.util.*

fun converter(
    automaton: Automaton
): Automaton {
    val edges:MutableList<MutableList<MutableList<Int>>> = mutableListOf()
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
                    edges.addAll(List(stateFrom-edges.size + 1){ MutableList(automaton.alphabetSize){ mutableListOf()} })
                val symbol = i
                val stateTo = groups.indexOf(newElem)
                edges[stateFrom][symbol].add(stateTo)

            }
        }
    }

    return Automaton(edges.size,automaton.alphabetSize, listOf(0), finishes, edges)
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
            state.forEachIndexed{ chrIndex, chr ->
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

    return Automaton(n,m,start,finish,states)
}

fun bfs(nodes:Automaton, u: Int, used: MutableList<Boolean>) {
    val queue = mutableListOf<Int>()
    queue.add(u)
    while(queue.isNotEmpty()) {
        val current = queue.removeAt(0)
        used[current] = true
        nodes.states[current].forEach{
            it.forEach { elem ->
                queue.add(elem)
            }
        }
    }
}

fun minimisation(automaton: Automaton){
    val newAutomaton = converter(automaton)
    val used = MutableList(newAutomaton.stateNumber) {false}
    bfs(newAutomaton,0,used)
    automaton.states = automaton.states.filterIndexed {index,_ -> used[index] }.toMutableList()
    automaton.finish = automaton.finish.filterIndexed {index,_ -> used[index] }.toMutableList()
    automaton.start = automaton.start.filterIndexed {index,_ -> used[index] }.toMutableList()
    automaton.stateNumber = automaton.states.size

    var classByElem: MutableList<Int> = MutableList(automaton.stateNumber) {0}
    automaton.finish.forEach { classByElem[it] = 1 }

    var elementsByClass: MutableList<MutableList<Int>> = MutableList(2) { mutableListOf()}
    (0..automaton.stateNumber).forEach{
        elementsByClass[if (automaton.finish.contains(it)) 1 else 0].add(it)
    }
    var canSplit = true
    while (canSplit)
    {
        canSplit = false
        var newClassByElem =  mutableListOf<Int>()
        val newElementsByClass =  mutableListOf<MutableList<Int>>()
        elementsByClass.forEach { currentClass ->
            for (chr in 0 until automaton.alphabetSize)
            {
                val classes: MutableSet<Int> = mutableSetOf()
                currentClass.forEach lbl@ {
                    if (automaton.states[it][chr].isEmpty()) {
                        classes.add(-1)
                        return@lbl
                    }
                    classes.add(classByElem[automaton.states[it][chr].first()])
                }

                if (classes.size > 1)
                {
                    canSplit = true
                    val newClassesHelper = MutableList(classes.maxOf { it } + 2) {-1}
                    var index = 0
                    classes.forEach{
                        newClassesHelper[it+1] = index++
                    }
                    val newSets = MutableList<MutableList<Int>>(classes.size) { mutableListOf()}
                    currentClass.forEach lbl@ {
                        if (automaton.states[it][chr].isEmpty()) {
                            newSets[0].add(it)
                            return@lbl
                        }
                        val classIndex = newClassesHelper[classByElem[automaton.states[it][chr].first()]]
                        newSets[classIndex].add(it)
                    }
                    newElementsByClass.addAll(newSets)
                    newClassByElem = MutableList(newElementsByClass.size) {-1}
                    newElementsByClass.forEachIndexed { classNumber, list->
                            list.forEach {
                                newClassByElem[it] = classNumber
                            }
                    }

                }
            }
        }
        elementsByClass = newElementsByClass
        classByElem = newClassByElem
    }
}

fun main(args: Array<String>) {
    val automaton = convertInfo("src/main/resources/convert.txt");
    println(converter(automaton))
}
