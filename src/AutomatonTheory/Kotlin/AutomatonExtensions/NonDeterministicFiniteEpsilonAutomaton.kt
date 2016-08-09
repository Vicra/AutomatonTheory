package AutomatonTheory.Kotlin.AutomatonExtensions

import AutomatonTheory.Kotlin.AutomatonLogic.Automaton
import AutomatonTheory.Kotlin.AutomatonLogic.Automatons
import AutomatonTheory.Kotlin.AutomatonLogic.State
import AutomatonTheory.Kotlin.AutomatonLogic.Transition
import java.util.*

open class NonDeterministicFiniteEpsilonAutomaton (automatonName:String) : Automaton() {
    var evaluatedStates:MutableList<String> = ArrayList()
    var epsilonClosure:MutableList<State> = ArrayList()

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        val originState = getState(originStateName)
        val destinyState = getState(destinyStateName)
        return originState.addTransition(Transition(destinyState, symbol))
    }

    init{
        AutomatonName = automatonName
        Type = Automatons.NFAe
    }

    constructor(automatonName: String, alphabet: MutableList<String>) : this(automatonName) {
        setAlphabet(alphabet)
        Alphabet.add("e")
    }

    override fun evaluateString(stringEvaluate:String):Boolean{
        return toNFA().evaluateString(stringEvaluate)
    }
    open fun getClosure(state:State){
        if(!evaluatedStates.contains(state.Name)){
            epsilonClosure.add( state )
            evaluatedStates.add( state.Name )
            for(transition in state.Transitions){
                if(transition.Symbol.equals('e')){
                    var nextState = transition.DestinyState
                    getClosure( nextState )
                }
            }
        }
    }

    open fun getReachableStates(closureStates:MutableList<State>, alphabetItem:Char):MutableList<State>{
        var  reachableStates:MutableList<State> = mutableListOf()
        for(state in closureStates){
            for (transition in state.Transitions){
                if(transition.Symbol.equals(alphabetItem)){
                    reachableStates.add(transition.DestinyState)
                }
            }
        }
        closureStates.clear()
        evaluatedStates.clear()
        return reachableStates
    }

    open fun toNFA():NonDeterministicFiniteAutomaton{
        var nfa:NonDeterministicFiniteAutomaton = NonDeterministicFiniteAutomaton()
        nfa.Alphabet = Alphabet

        for(thisStates in States) {
            nfa.States.add(State(thisStates.Name,thisStates.InitialState,thisStates.AcceptanceState))
        }

        for(state in States){
            for(symbol in Alphabet){
                getClosure(state)
                var reachableStates = getReachableStates(epsilonClosure, symbol[0])
                for(destinyState in reachableStates){
                    getClosure(destinyState)
                }
                for(currentState in epsilonClosure){
                    var stateToModify = nfa.getState(state.Name)

                    if(currentState.AcceptanceState){
                        stateToModify.AcceptanceState = true
                    }
                    stateToModify.addTransition(Transition(currentState,symbol))
                }
                evaluatedStates.clear()
                epsilonClosure.clear()
            }
        }
        return nfa
    }
}