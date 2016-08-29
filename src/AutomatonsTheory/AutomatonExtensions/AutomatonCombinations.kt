package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition

open class AutomatonCombinations {

    fun Union(automatonA:DeterministicFiniteAutomaton, automatonB:DeterministicFiniteAutomaton) : DeterministicFiniteAutomaton{
        var returnDFA:DeterministicFiniteAutomaton = merge(automatonA, automatonB)
        returnDFA.AutomatonName = "Union: " + automatonA.AutomatonName + " " + automatonB.AutomatonName
        for(stateA in automatonA.States){
            for(stateB in automatonB.States){
                if(stateA.InitialState && stateB.InitialState){
                    for(newState in returnDFA.States){
                        if(newState.Name.equals(stateA.Name + stateB.Name)){
                            newState.InitialState = true
                        }
                    }
                }
                if(stateA.AcceptanceState || stateB.AcceptanceState){
                    for(newState in returnDFA.States){
                        if(newState.Name.equals(stateA.Name + stateB.Name)){
                            newState.AcceptanceState = true
                        }
                    }
                }
            }
        }
        return returnDFA
    }

    fun Intersection(automatonA:DeterministicFiniteAutomaton, automatonB:DeterministicFiniteAutomaton) : DeterministicFiniteAutomaton{
        var returnDFA:DeterministicFiniteAutomaton = merge(automatonA, automatonB)
        returnDFA.AutomatonName = "Interseccion: " + automatonA.AutomatonName + " " + automatonB.AutomatonName
        for(stateA in automatonA.States){
            for(stateB in automatonB.States){
                if(stateA.InitialState && stateB.InitialState){
                    for(newState in returnDFA.States){
                        if(newState.Name.equals(stateA.Name + stateB.Name)){
                            newState.InitialState = true
                        }
                    }
                }
                if(stateA.AcceptanceState && stateB.AcceptanceState){
                    for(newState in returnDFA.States){
                        if(newState.Name.equals(stateA.Name + stateB.Name)){
                            newState.AcceptanceState = true
                        }
                    }
                }
            }
        }
        return returnDFA
    }

    fun Resta(automatonA:DeterministicFiniteAutomaton, automatonB:DeterministicFiniteAutomaton) : DeterministicFiniteAutomaton{
        var returnDFA:DeterministicFiniteAutomaton = merge(automatonA, automatonB)
        returnDFA.AutomatonName = "Resta: " + automatonA.AutomatonName + " " + automatonB.AutomatonName
        for(stateA in automatonA.States){
            for(stateB in automatonB.States){
                if(stateA.InitialState && stateB.InitialState){
                    for(newState in returnDFA.States){
                        if(newState.Name.equals(stateA.Name + stateB.Name)){
                            newState.InitialState = true
                        }
                    }
                }
                if(stateA.AcceptanceState && !stateB.AcceptanceState){
                    for(newState in returnDFA.States){
                        if(newState.Name.equals(stateA.Name + stateB.Name)){
                            newState.AcceptanceState = true
                        }
                    }
                }
            }
        }
        return returnDFA
    }

    fun merge(automatonA:DeterministicFiniteAutomaton, automatonB:DeterministicFiniteAutomaton) : DeterministicFiniteAutomaton{
        var returnDFA:DeterministicFiniteAutomaton = DeterministicFiniteAutomaton("")
        for(stateA in automatonA.States){
            for(stateB in automatonB.States){
                returnDFA.States.add(State(stateA.Name + stateB.Name, false, false))
            }
        }
        var transitionSymbols:MutableList<String> = mutableListOf()
        for(stateA in automatonA.States){
            for(transition in stateA.Transitions){
                if(!transitionSymbols.contains(transition.Symbol)){
                    transitionSymbols.add(transition.Symbol)
                }
            }
        }
        for(stateB in automatonB.States){
            for(transition in stateB.Transitions){
                if(!transitionSymbols.contains(transition.Symbol)){
                    transitionSymbols.add(transition.Symbol)
                }
            }
        }

        for(stateA in automatonA.States){
            for(stateB in automatonB.States){
                for(symbol in transitionSymbols){
                    val transition1 = getTransition(stateA, symbol)
                    val transition2 = getTransition(stateB, symbol)
                    val originStateName = stateA.Name + stateB.Name
                    var destinyStateName = ""
                    if(transition1!=null){
                        destinyStateName += transition1.DestinyState.Name
                    }
                    if(transition2!=null){
                        destinyStateName += transition2.DestinyState.Name
                    }
                    returnDFA.getState(originStateName).Transitions.add(Transition(returnDFA.getState(destinyStateName),symbol))
                }
            }
        }

        return returnDFA
    }

    fun Complemento(automaton:DeterministicFiniteAutomaton) : DeterministicFiniteAutomaton{
        var returnDFA:DeterministicFiniteAutomaton = DeterministicFiniteAutomaton("")
        returnDFA.AutomatonName = "Complemento: " +automaton.AutomatonName
        var transitionSymbols:MutableList<String> = mutableListOf()
        for(index in automaton.States.indices){
            var newState:State = State(automaton.getState(index).Name,!automaton.getState(index).InitialState, !automaton.getState(index).AcceptanceState)
            returnDFA.addState(newState)

        }
        for(state in automaton.States){
            for(transition in state.Transitions){
                returnDFA.getState(state.Name).Transitions.add(Transition(returnDFA.getState(transition.DestinyState.Name),transition.Symbol))
            }
        }
        for(state in automaton.States){
            for(transition in state.Transitions){
                if(!transitionSymbols.contains(transition.Symbol)){
                    transitionSymbols.add(transition.Symbol)
                }
            }
        }
        for(dfaState in returnDFA.States){
            for(symbol in transitionSymbols){
                val transition = getTransition(dfaState, symbol)
                if(transition == null){
                    var newState = State(dfaState.Name+symbol,false, false)
                    returnDFA.addState(newState)
                    newState.Transitions.add(Transition(returnDFA.getState(dfaState.Name+symbol),symbol))
                    for(stateA in returnDFA.States){
                        if(stateA.Name == (dfaState.Name+symbol)){
                            stateA.AcceptanceState = true
                        }
                    }
                }
            }
        }
        var stateSink:State = State("qT", false, false)
        returnDFA.addState(stateSink)
        for(dfaState in returnDFA.States){
            for(symbol in transitionSymbols){
                if(getStatesBySymbol(returnDFA, dfaState, symbol) == null){
                    dfaState.Transitions.add(Transition(returnDFA.getState(stateSink.Name),symbol))
                }
            }
        }
        return returnDFA
    }

    fun getTransition(state:State, symbol:String): Transition? {
        for(transition in state.Transitions){
            if(transition.Symbol.equals(symbol)){
                return transition
            }
        }
        return null
    }

    fun getStatesBySymbol(automaton:DeterministicFiniteAutomaton, state:State, symbol:String): MutableList<State>? {
        var states:MutableList<State> = mutableListOf()
        for(transition in state.Transitions){
            if(transition.Symbol.equals(symbol)){
                states.add(automaton.getState(transition.DestinyState.Name))
            }
        }
        if(states.size>=1){
            return states
        }
        return null
    }
}