package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons
import AutomatonsTheory.AutomatonLogic.Transition

open class PushDownAutomaton() : Automaton(){

    var startState: String? = null
    var initialStackSymbol: String = "Z0"

    val epsilon = 'ε'

    override fun evaluateString(stringEvaluate: String): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var stack = Stack()

    open var Stack:MutableList<String> = mutableListOf()
    init {
        Type = Automatons.PDA
    }

    constructor(automatonName: String) : this(){
        AutomatonName = automatonName
    }

    constructor(automatonName: String, alphabet:MutableList<String>) : this(){
        AutomatonName = automatonName
        this.Alphabet = alphabet
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        return getState(originStateName).Transitions.add(Transition(getState(destinyStateName), symbol))
    }
}