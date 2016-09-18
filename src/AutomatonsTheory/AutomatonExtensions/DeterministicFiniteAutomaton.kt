package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons
import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition


open class DeterministicFiniteAutomaton(automatonName: String) : Automaton() {

    init {
        AutomatonName = automatonName
        Type = Automatons.DFA
    }

    constructor(automatonName: String, alphabet: MutableList<String>) : this(automatonName) {
        setAlphabet(alphabet)
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        val characters = stringEvaluate.toCharArray()
        var currentState = getInitialState()

        var trashState:State = State("", false, false)
        States.add(trashState)

        for(state in States){
            for(symbol in Alphabet) {
                var hasTransition = false
                for (transition in state.Transitions) {
                    if(transition.Symbol.equals(symbol)){
                        hasTransition = true
                    }
                }
                if(!hasTransition){
                    state.Transitions.add(Transition(getState(""),symbol))
                }
            }
        }

        if (characters.size > 0) {
            for (character in characters) {
                if (Alphabet.contains("" + character)) {
                    var hasTransition = false
                    for (item in currentState.Transitions) {

                        if (item.Symbol == "" + character) {
                            currentState = item.DestinyState
                            hasTransition = true
                        }
                    }
                    if (!hasTransition) {
                        return false
                    }
                } else {
                    return false
                }
            }
            if (currentState.AcceptanceState) {
                return true
            } else {
                return false
            }
        }
        return false
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        val originState = getState(originStateName)
        val destinyState = getState(destinyStateName)
        if(destinyState.Name.equals("")){
            addState(State(destinyStateName, false, false))
        }

        //validar que el origen y symbol no se pueden repetir
        var canAddTransition = true
        for (state in States) {
            for (transition in state.Transitions) {
                if (state.Name == originStateName && transition.Symbol == symbol) {
                    canAddTransition = false
                }
            }
        }
        if (canAddTransition) {
            return originState.addTransition(Transition(destinyState, symbol))
        }
        else{
            return false
        }
    }

    fun minimize():DeterministicFiniteAutomaton{
        var returnDfa = DeterministicFiniteAutomaton(this.AutomatonName)
        var states:MutableList<State> = mutableListOf()
        var acceptanceStates:MutableList<State> = mutableListOf()

        for (state in States) {
            if (state.AcceptanceState){
                acceptanceStates.add(state)
            }
            else{
                states.add(state)
            }
        }

        var equivalentStates:MutableList<MutableList<State>> = mutableListOf()
        if (states.isNotEmpty()){
            equivalentStates.add(states)
        }
        if (acceptanceStates.isNotEmpty()){
            equivalentStates.add(acceptanceStates)
        }

        var symbols:MutableList<String> = mutableListOf()

        for (state in States) {
            for (transition in  state.Transitions) {
                if(!symbols.contains(transition.Symbol)) {
                    symbols.add(transition.Symbol)
                }
            }
        }
        var hasNotEquivalentStates = true
        while (hasNotEquivalentStates) {
            var stateReferenceList:MutableList<MutableList<State>> = mutableListOf()
            var notEquivalentStates:MutableList<State> = mutableListOf()

            for (equivalentState in equivalentStates) {
                stateReferenceList.add(mutableListOf<State>())
                stateReferenceList.last().add(equivalentState.first())
                for (stateItem in equivalentState){
                if (stateItem.Name != equivalentState[0].Name) {
                    var equivalent = true
                    for (symbol in symbols) {
                        if (getTransitions(stateItem, symbol).isNotEmpty() && getTransitions(equivalentState.first(), symbol).isNotEmpty()) {
                            val state1 = getTransitions(stateItem, symbol).first()
                            val state2 = getTransitions(equivalentState.first(), symbol).first()
                            if (state1 != null && state2 != null) {
                                if (!statesAreEquivalent(getState(state1.DestinyState.Name), getState(state2.DestinyState.Name), equivalentStates)) {
                                    equivalent = false
                                }
                            }
                        }
                    }
                    if (equivalent) {
                        stateReferenceList[stateReferenceList.size - 1].add(stateItem)
                    } else {
                        notEquivalentStates.add(stateItem)
                    }
                }
            }
            }
            equivalentStates = stateReferenceList
            if (notEquivalentStates.isNotEmpty()) {
                equivalentStates.add(notEquivalentStates)
            } else {
                hasNotEquivalentStates = false
            }

        }

        for (equivalentState in equivalentStates) {
            var stateName:String = ""
            var acceptedState:Boolean = false
            var initialState:Boolean = false
            for (state in equivalentState) {
                stateName += state.Name
                if (state.AcceptanceState){
                    acceptedState = true
                }
                if (state.InitialState){
                    initialState = true
                }
            }
            var newState:State = State(stateName, initialState, acceptedState)
            returnDfa.addState(newState)
        }

        for (index in equivalentStates.indices) {
            for (state in States) {
                    if (state.Name == equivalentStates[index].first().Name) {
                    for (transition in state.Transitions) {
                        for (destinyIndex in equivalentStates.indices) {
                            if (equivalentStates[destinyIndex].contains(getState(transition.DestinyState.Name))) {
                                returnDfa.addTransition(returnDfa.States[index].Name, returnDfa.States[destinyIndex].Name, transition.Symbol)
                            }
                        }
                    }
                }
            }
        }
        return returnDfa
    }

    fun statesAreEquivalent(stateA:State, stateB:State, equivalentStates:MutableList<MutableList<State>>):Boolean{
        if (stateA.Name == stateB.Name) {
            return true
        }
        for (equivalentState in equivalentStates) {
            var existsEquivalence = false
            for (state in equivalentState) {
                if (state.Name == stateA.Name || state.Name == stateB.Name) {
                    if (existsEquivalence) {
                        return true
                    }
                    else {
                        existsEquivalence = true
                    }
                }
            }
            if (existsEquivalence) {
                return false
            }
        }
        return true
    }

    fun getTransitions(state:State, symbol:String) : MutableList<Transition> {
        var transitions:MutableList<Transition> = mutableListOf()
        for(transition in state.Transitions){
            if(transition.Symbol.equals(symbol)){
                transitions.add(transition)
            }
        }
        return transitions
    }

    fun toRegularExpression():String{
        return ""
    }
}