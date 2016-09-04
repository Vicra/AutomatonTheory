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

    fun toDFA(): DeterministicFiniteAutomaton{
        var returnDFA = DeterministicFiniteAutomaton(this.AutomatonName)
        returnDFA.Alphabet = this.Alphabet
        var matrizPrincipal: Array2D<String> = Array2D<String>(States.size, Alphabet.size) as Array2D<String>

        //llenar la matriz
        for(stateIndex in States.indices){
            for(symbolIndex in Alphabet.indices){
                var state:State = getState(stateIndex)
                var destinyStates = state.getDestinyStates(Alphabet.get(symbolIndex))
                var destinyStatesName = getNewName(destinyStates)
                matrizPrincipal.set(stateIndex, symbolIndex,destinyStatesName)
            }
        }

        //copiar estados del nfa al dfa
        for(state in States){
            returnDFA.States.add(State(state.Name, state.InitialState, state.AcceptanceState))
        }

        //meter los nuevos estados al dfa
        for(stateIndex in States.indices){
            for(symbolIndex in Alphabet.indices){
                var stateName:String = matrizPrincipal.get(stateIndex, symbolIndex)
                var stateSplit = stateName.split(",")
                if(stateName.isNotEmpty() && stateSplit.size > 1){
                    returnDFA.States.add(State(stateName, false, false))
                }
            }
        }

        //estado incial
        var initialState:State = getInitialState()
        for(state in returnDFA.States){
            if(state.Name.equals(initialState.Name)){
                state.InitialState = true
            }
        }

        //estados finales
        for(state in States){
            for(dfaState in returnDFA.States){
                if(state.AcceptanceState && dfaState.Name.contains(state.Name)){
                    dfaState.AcceptanceState = true
                }
            }
        }

        //meter transiciones copiar de matriz
        for(stateIndex in States.indices){
            for(symbolIndex in Alphabet.indices){
                var currentState = returnDFA.getState(stateIndex)
                var destinyStateName = matrizPrincipal.get(stateIndex, symbolIndex)
                if(destinyStateName.isNotEmpty()){
                    currentState.Transitions.add(Transition(returnDFA.getState(destinyStateName),returnDFA.Alphabet[symbolIndex]))
                }
            }
        }

        //faltan las transiciones de los nuevos estados

        var iterator = States.toList().listIterator()
        while (iterator.hasNext()) {
            val state = iterator.next()
            if(state.Name.contains(",")){
                var stateNames = state.Name.split(",")
                for(symbol in returnDFA.Alphabet){

                    var newDestinyName:String = ""
                    for(stateName in stateNames){
                        var myState:State = returnDFA.getState(stateName)
                        var destinyState = myState.getDestinyState(symbol).Name.split(",")//esto puede ser una combinacion de estados ie: q0, q1

                        for(dest in destinyState){
                            if(!newDestinyName.contains(dest)){
                                if(newDestinyName.isEmpty()){
                                    newDestinyName += dest
                                }
                                else{
                                    newDestinyName += "," + dest
                                }
                            }
                        }
                    }
                    if(!returnDFA.existsState(newDestinyName)){
                        returnDFA.States.add(State(newDestinyName, false, false))
                    }
                    if(newDestinyName.isNotEmpty()){
                        state.Transitions.add(Transition(returnDFA.getState(newDestinyName),symbol))
                    }
                }
            }
        }
//        for(state in returnDFA.States){
//            if(state.Name.contains(",")){
//                var stateNames = state.Name.split(",")
//                for(symbol in returnDFA.Alphabet){
//
//                    var newDestinyName:String = ""
//                    for(stateName in stateNames){
//                        var myState:State = returnDFA.getState(stateName)
//                        var destinyState = myState.getDestinyState(symbol).Name.split(",")//esto puede ser una combinacion de estados ie: q0, q1
//
//                        for(dest in destinyState){
//                            if(!newDestinyName.contains(dest)){
//                                if(newDestinyName.isEmpty()){
//                                    newDestinyName += dest
//                                }
//                                else{
//                                    newDestinyName += "," + dest
//                                }
//                            }
//                        }
//                    }
//                    if(!returnDFA.existsState(newDestinyName)){
//                        returnDFA.States.add(State(newDestinyName, false, false))
//                    }
//                    if(newDestinyName.isNotEmpty()){
//                        state.Transitions.add(Transition(returnDFA.getState(newDestinyName),symbol))
//                    }
//                }
//            }
//        }

        println("fronen")
        return returnDFA
    }

    fun toDeterministicFiniteAutomaton() : DeterministicFiniteAutomaton{
        var returnDFA:DeterministicFiniteAutomaton = DeterministicFiniteAutomaton(this.AutomatonName)
        var initialState: State? = null
        var transitionSymbols:MutableList<String> = mutableListOf()

        for(state in States){
            if(state.InitialState){
                initialState = State(state.Name, state.InitialState, state.AcceptanceState)
            }
            for(transition in state.Transitions){
                if(!transitionSymbols.contains(transition.Symbol)){
                    transitionSymbols.add(transition.Symbol)
                }
            }
        }

        var entryStates:MutableList<State> = mutableListOf()
        return returnDFA
    }

    fun getNewName(destinyStates:MutableList<State>) : String{
        var returnString:String = ""
        if(destinyStates.size == 1){
            returnString = destinyStates[0].Name
        }
        else{
            for(destinyState in destinyStates){
                if(returnString.isEmpty()){
                    returnString += destinyState.Name
                }
                else{
                    returnString += "," + destinyState.Name
                }
            }
        }
        return returnString
    }
}