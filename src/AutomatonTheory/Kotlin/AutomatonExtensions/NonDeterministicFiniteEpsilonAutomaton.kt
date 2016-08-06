package AutomatonTheory.Kotlin.AutomatonExtensions

import AutomatonTheory.Kotlin.AutomatonLogic.Automaton
import AutomatonTheory.Kotlin.AutomatonLogic.Automatons

open class NonDeterministicFiniteEpsilonAutomaton (automatonName:String) : Automaton() {

    init {
        AutomatonName = automatonName
        Type = Automatons.NFAe
    }

    constructor(automatonName: String, alphabet: MutableList<String>) : this(automatonName) {
        setAlphabet(alphabet)
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        return true
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        return true
    }
}