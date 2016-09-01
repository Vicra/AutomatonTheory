package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons

open class PushDownAutomaton() : Automaton(){

    open var Stack:MutableList<String> = mutableListOf()
    init {
        Type = Automatons.PDA
    }

    constructor(automatonName: String) : this(){
        AutomatonName = automatonName
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}