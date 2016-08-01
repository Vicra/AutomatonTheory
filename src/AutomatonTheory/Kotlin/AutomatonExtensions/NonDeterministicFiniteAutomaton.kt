package AutomatonTheory.Kotlin.AutomatonExtensions

import AutomatonTheory.Kotlin.AutomatonLogic.Automaton
import AutomatonTheory.Kotlin.AutomatonLogic.Automatons

open class NonDeterministicFiniteAutomaton(automatonName: String) : Automaton() {

    init {
        AutomatonName = automatonName
        Type = Automatons.NFA
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        return false
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        return false
    }
}