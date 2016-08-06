package AutomatonTheory.Kotlin.AutomatonExtensions

import AutomatonTheory.Kotlin.AutomatonLogic.Automaton
import AutomatonTheory.Kotlin.AutomatonLogic.Automatons
import AutomatonTheory.Kotlin.AutomatonLogic.State
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

    fun toDeterministicFiniteAutomaton() : DeterministicFiniteAutomaton{
        var newDeterministicAutomaton:DeterministicFiniteAutomaton = DeterministicFiniteAutomaton(this.AutomatonName)
        var currentState:State = State()

        //agregar todos los estados del nfa a dfa
        //addStatesFromNFAtoDFA(newDeterministicAutomaton)

        for(state in States){
            for(symbol in Alphabet){
                currentState = getState(state.Name)
                var destinyStates:MutableList<State> = currentState.getDestinyStates(symbol)
                newDeterministicAutomaton.addState(State(currentState.Name,currentState.InitialState,currentState.AcceptanceState))
                if(destinyStates.size > 0){
                    var newState:State = State()
                    var newStateName = ""

                    //definir nombre nuevo estado
                    for(destinyState in destinyStates){
                        if(newStateName.isEmpty()){
                            newStateName += destinyState.Name
                        }
                        else{
                            if(!newStateName.contains(destinyState.Name)){
                                newStateName += "," + destinyState.Name
                            }
                        }
                        if(destinyState.AcceptanceState){
                            newState.AcceptanceState = true
                        }

                    }

                    if(newStateName.isNotEmpty()){
                        newState.Name = newStateName
                        newDeterministicAutomaton.addState(State(currentState.Name, currentState.InitialState, currentState.AcceptanceState))
                        newDeterministicAutomaton.addState(newState)
                        newDeterministicAutomaton.getState(currentState.Name).addTransition(Transition(newState, symbol))
                    }
                }
            }
        }
        for(dfaState in newDeterministicAutomaton.States){
            for(symbol in Alphabet) {
                var newStateCompositionName = ""
                var compositionStates = dfaState.Name.split(",")
                if(compositionStates.size > 1){ //ie: p,q son dos composition states
                    for (composition in compositionStates) {
                        var destinyStates:MutableList<State> = getState(composition).getDestinyStates(symbol)
                        var compositionStateName = ""
                        for(destiny in destinyStates){
                            if(compositionStateName.isEmpty()){
                                compositionStateName = destiny.Name
                            }
                            else{
                                compositionStateName += "," + destiny.Name
                            }
                        }
                        if(newStateCompositionName.isEmpty()){
                            newStateCompositionName += compositionStateName
                        }
                        else{
                            if(compositionStateName.isNotEmpty()){
                                newStateCompositionName += "," + compositionStateName
                            }
                        }
                    }
                    var newState:State = newDeterministicAutomaton.getState(newStateCompositionName)
                    dfaState.addTransition(Transition(newState,symbol))
                }
            }
        }
        //eliminar estados busqueda a profundidad
        for(state in newDeterministicAutomaton.States){

        }
        return newDeterministicAutomaton
    }

    fun addStatesFromNFAtoDFA(dfa:DeterministicFiniteAutomaton) : Unit{
        for(state in States){
            dfa.addState(State(state.Name,state.InitialState, state.AcceptanceState))
        }
    }
}