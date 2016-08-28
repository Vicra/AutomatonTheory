package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.State

open class AutomatonCombinations {

    fun Union(automatonA:DeterministicFiniteAutomaton, automatonB:DeterministicFiniteAutomaton) : DeterministicFiniteAutomaton{
        var returnDFA:DeterministicFiniteAutomaton = merge(automatonA, automatonB)
        //set initial State
        var initialStateA:String = automatonA.getInitialState().Name
        var initialStateB:String = automatonA.getInitialState().Name
        for(state in returnDFA.States){
            if(state.Name.equals(initialStateA+initialStateB)){
                state.InitialState = true
            }
        }

        //set acceptance States
        var acceptanceStatesAutomatonA:MutableList<State> = mutableListOf()
        var acceptanceStatesAutomatonB:MutableList<State> = mutableListOf()

        acceptanceStatesAutomatonA = automatonA.getAcceptanceStates()
        acceptanceStatesAutomatonB = automatonB.getAcceptanceStates()

        for(state in returnDFA.States){
            if(acceptanceStatesAutomatonA.contains(state) || acceptanceStatesAutomatonB.contains(state)){
                state.AcceptanceState = true
            }
        }
        println()

        return returnDFA
    }

    fun merge(automatonA:DeterministicFiniteAutomaton, automatonB:DeterministicFiniteAutomaton) : DeterministicFiniteAutomaton{
        var returnDFA:DeterministicFiniteAutomaton = DeterministicFiniteAutomaton("")
        for(stateA in automatonA.States){
            for(stateB in automatonB.States){
                returnDFA.States.add(State(stateA.Name + stateB.Name, false, false))
            }
        }
        return returnDFA
    }
}