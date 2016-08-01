package AutomatonTheory.Kotlin.FileController

import AutomatonTheory.Kotlin.AutomatonLogic.Automaton

interface IFileManager {
    fun loadAutomaton(automatonName: String): Automaton
    fun saveAutomaton(automaton: Automaton): Boolean
}
