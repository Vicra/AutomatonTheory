class FileManager {//: IFileManager {

//    internal var ln = System.lineSeparator()
//    @Throws(IOException::class)
//    override fun loadAutomaton(automatonName: String): Automaton {
//        var returnAutomaton: Automaton? = null
//
//        val allText = ""
//        var line: String
//        try {
//            FileInputStream(automatonName + ".txt").use { fis ->
//                InputStreamReader(fis, Charset.forName("UTF-8")).use { isr ->
//                    BufferedReader(isr).use { br ->
//                        line = br.readLine()
//                        while ((line = br.readLine()) != null) {
//                            if (line.contains("Type")) {
//                                val elements = line.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                                if (elements[1] == "DFA") {
//                                    returnAutomaton = DeterministicFiniteAutomaton(automatonName)
//                                }
//                                if (elements[1] == "NFA") {
//                                    returnAutomaton = NonDeterministicFiniteAutomaton(automatonName)
//                                }
//                                if (elements[1] == "NFAe") {
//                                    returnAutomaton = NonDeterministicFiniteEpsilonAutomaton(automatonName)
//                                }
//                            }
//                            if (line.contains("Alphabet")) {
//                                val elements = line.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                                val symbols = elements[1].split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                                for (i in symbols.indices) {
//                                    returnAutomaton!!.Alphabet.add(symbols[i])
//                                }
//                            }
//                            if (line.contains("States")) {
//                                val elements = line.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                                val states = elements[1].split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                                for (i in states.indices) {
//                                    val attributes = states[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                                    returnAutomaton!!.States.add(State(attributes[0], if (attributes[1] == "true") true else false, if (attributes[2] == "true") true else false))
//                                }
//                            }
//                            if (line.contains("Transitions")) {
//                                val elements = line.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                                val transitions = elements[1].split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                                for (i in transitions.indices) {
//                                    val attributes = transitions[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                                    val currentState = returnAutomaton!!.getState(attributes[0])
//                                    currentState.addTransition(Transition(returnAutomaton!!.getState(attributes[1]), attributes[2]))
//                                }
//                            }
//
//                        }
//                    }
//                }
//            }
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        return returnAutomaton
//    }
//
//    override fun saveAutomaton(automaton: Automaton): Boolean {
//        try {
//            val fw = FileWriter(automaton.AutomatonName + ".txt")
//
//            fw.write("Type:" + automaton.Type.toString() + ln)
//            fw.write("Alphabet:")
//            for (i in 0..automaton.Alphabet.size - 1) {
//                fw.write(automaton.Alphabet[i])
//                if (i < automaton.Alphabet.size - 1) {
//                    fw.write(";")
//                }
//            }
//            fw.write(ln)
//
//            fw.write("States:")
//            for (i in 0..automaton.States.size - 1) {
//                val state = automaton.States[i]
//                fw.write(state.Name + "," + state.InitialState + "," + state.AcceptanceState)
//                if (i < automaton.Alphabet.size - 1) {
//                    fw.write(";")
//                }
//            }
//            fw.write(ln)
//
//            fw.write("Transitions:")
//
//            for (i in 0..automaton.States.size - 1) {
//                val state = automaton.States[i]
//                for (j in 0..state.Transitions.size - 1) {
//                    val transition = state.Transitions[j]
//                    if (automaton.States.size - 1 == i && state.Transitions.size - 1 == j) {
//                        fw.write(state.Name + "," + transition.DestinyState.Name + "," + transition.Symbol)
//                    } else {
//                        fw.write(state.Name + "," + transition.DestinyState.Name + "," + transition.Symbol + ";")
//                    }
//
//                }
//            }
//
//            fw.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return false
//    }
}
