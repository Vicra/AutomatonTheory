package AutomatonTheory.Kotlin.AutomatonExtensions

import AutomatonTheory.Kotlin.AutomatonLogic.Automaton
import AutomatonTheory.Kotlin.AutomatonLogic.Automatons
import AutomatonTheory.Kotlin.AutomatonLogic.Transition

open class NonDeterministicFiniteAutomaton(automatonName: String) : Automaton() {

    init {
        AutomatonName = automatonName
        Type = Automatons.NFA
    }

    constructor(automatonName: String, alphabet: MutableList<String>) : this(automatonName) {
        setAlphabet(alphabet)
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        val characters = stringEvaluate.toCharArray()
        var currentState = getInitialState()
        var currentStates = mutableListOf<AutomatonTheory.Kotlin.AutomatonLogic.State>()
        if (currentState != null)
            currentStates.add(currentState)

        for (character in characters) {
            val currentStatesFiltered = mutableListOf<AutomatonTheory.Kotlin.AutomatonLogic.State>()
            for (currState in currentStates)
            {
                for (transition in currentState!!.Transitions) {
                    if (character.toString() == transition.Symbol)
                        currentStatesFiltered.add(getState(transition.DestinyState.Name))
                }
            }
            currentStates = currentStatesFiltered
        }

        for (state in currentStates)
        {
            if (state.AcceptanceState)
            {
                return true
            }
        }
        return false
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        val originState = getState(originStateName)
        val destinyState = getState(destinyStateName)
        return originState.addTransition(Transition(destinyState, symbol))
    }
}