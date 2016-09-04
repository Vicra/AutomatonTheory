package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons
import AutomatonsTheory.AutomatonLogic.State

open class PushDownAutomaton() : Automaton(){

    open var Stack:MutableList<String> = mutableListOf()
    init {
        Type = Automatons.PDA
    }

    constructor(automatonName: String) : this(){
        AutomatonName = automatonName
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        val initial = "Z"
        var transitionValues:MutableList<String> = mutableListOf()
        var consumed:MutableList<String> = mutableListOf()
        var currentState:State? = null
        var accepted=true

        for(state in States){
            if(state.InitialState){
                currentState=state
            }
            for(trans in state.Transitions){
                val pvalues = trans.Symbol
                val plist = pvalues.split("-")
                if(!transitionValues.contains(plist[0]))
                    transitionValues.add(plist[0])
            }
        }



        return accepted
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}