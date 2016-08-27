package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons
import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition

open class NonDeterministicFiniteAutomaton() : Automaton() {

    init {
        Type = Automatons.NFA
    }

    constructor(automatonName: String, alphabet: MutableList<String>) : this(automatonName) {
        setAlphabet(alphabet)
    }

    constructor(automatonName: String) : this(){
        AutomatonName = automatonName
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        val characters = stringEvaluate.toCharArray()
        var currentState = getInitialState()
        var currentStates = mutableListOf<State>()
        currentStates.add(currentState)

        for (character in characters) {
            val currentStatesFiltered = mutableListOf<State>()
            for (currState in currentStates)
            {
                for (transition in currState.Transitions) {
                    if (character.toString() == transition.Symbol)
                        currentStatesFiltered.add(getState(transition.DestinyState.Name))
                }
            }
            currentStates.clear()
            for(newCurrentState in currentStatesFiltered){
                currentStates.add(newCurrentState)
            }
        }
        if(stringEvaluate.isEmpty()){
            val currentStatesFiltered = mutableListOf<State>()
            for (currState in currentStates)
            {
                for (transition in currState.Transitions) {
                    if ("e" == transition.Symbol)
                        currentStatesFiltered.add(getState(transition.DestinyState.Name))
                }
            }
            currentStates.clear()
            for(newCurrentState in currentStatesFiltered){
                currentStates.add(newCurrentState)
            }
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

    fun toDeterministicFiniteAutomaton() : DeterministicFiniteAutomaton {
        var newDeterministicAutomaton: DeterministicFiniteAutomaton = DeterministicFiniteAutomaton(this.AutomatonName)
        var currentState: State = State()

        for(state in States){
            for(symbol in Alphabet){
                currentState = getState(state.Name)
                var destinyStates:MutableList<State> = currentState.getDestinyStates(symbol)
                newDeterministicAutomaton.addState(State(currentState.Name,currentState.InitialState,currentState.AcceptanceState))
                if(destinyStates.size > 0){
                    var newState: State = State()
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
                if(compositionStates.size > 1){
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
                    var newState: State = newDeterministicAutomaton.getState(newStateCompositionName)
                    dfaState.addTransition(Transition(newState,symbol))
                }
            }
        }
        return newDeterministicAutomaton
    }
}