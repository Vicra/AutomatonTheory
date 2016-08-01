package AutomatonTheory.Kotlin.AutomatonLogic

import java.util.*

public abstract class Automaton {
    public var AutomatonName = ""
    public var States: MutableList<State> = ArrayList()
    public var Alphabet: MutableList<String> = ArrayList()
    public var Type = Automatons.DFA

    public abstract fun evaluateString(stringEvaluate: String): Boolean
    public abstract fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean

    //region States
    public fun addState(newState: State): Boolean {
        if (!existsState(newState.Name)) {
            return States.add(newState)
        }
        return false
    }

    public fun removeState(stateName: String): Boolean {
        val originState = getState(stateName)
        //borra las transiciones que salen de el
        originState.Transitions.removeAll(originState.Transitions)

        //borra trans que llegan a el
        for (state in States) {
            val toRemoveTransitions = ArrayList<Transition>()
            for (trans in state.Transitions) {
                if (trans.DestinyState.Name == originState.Name) {
                    toRemoveTransitions.add(trans)
                }
            }
            state.Transitions.removeAll(toRemoveTransitions)
        }

        //se borra el mismo estado
        return States.remove(originState)
    }

    public fun existsState(stateName: String): Boolean {
        for (state in States) {
            if (state.Name == stateName) {
                return true
            }
        }
        return false
    }

    public fun toggleAcceptanceState(stateName: String): Boolean {
        for (state in States) {
            if (state.Name == stateName) {
                state.AcceptanceState = !state.AcceptanceState
                return true
            }
        }
        return false
    }

    public fun getInitialState() : State{
        for (state in States) {
            if (state.InitialState) {
                return state
            }
        }
        return State()
    }

    public fun setInitialState(stateName: String): Boolean {
        for (state in States) {
            if (state.Name == stateName) {
                state.InitialState = true
            } else {
                state.InitialState = false
            }
        }
        return true
    }

    public fun getState(stateName: String): State {
        for (state in States) {
            if (state.Name == stateName) {
                return state
            }
        }
        return State()
    }
    //endregion

    //region Transitions
    public fun removeTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        val originState = getState(originStateName)
        val transition = getTransition(originStateName, destinyStateName, symbol)
        if (transition != null) {
            return originState.Transitions.remove(transition)
        }
        return false
    }

    public fun getTransition(originStateName: String, destinyStateName: String, symbol: String): Transition? {
        for (state in States) {
            for (transition in state.Transitions) {
                if (state.Name == originStateName && transition.DestinyState.Name == destinyStateName && transition.Symbol == symbol) {
                    return transition
                }
            }
        }
        return null
    }

    public fun existsTransition(originStateName: String, destinyStatName: String, symbol: String): Boolean? {
        for (state in States) {
            for (transition in state.Transitions) {
                if (state.Name == originStateName &&
                        transition.DestinyState.Name == destinyStatName &&
                        transition.Symbol == symbol) {
                    return true
                }
            }
        }
        return false
    }
    //endregion

    public fun getAutomatonInfo() : String {
        var returnString = ""
        returnString += "Alphabet:" + "\n"
        for (symbol in Alphabet) {
            returnString += "  * " + symbol + "\n"
        }
        returnString += "\n" + "States: " + "\n"
        for (state in States) {
            returnString += " * " + state.Name + ", Initial State: " + state.InitialState + ", Acceptance State: " + state.AcceptanceState + "\n"
        }
        returnString += "\n" + "Transitions: " + "\n"
        for (state in States) {
            for (transition in state.Transitions) {
                returnString += " * Origin State: " + state.Name + ", Destiny State: " + transition.DestinyState.Name + ", Symbol: " + transition.Symbol + "\n"
            }
        }
        return returnString
    }

    public fun setAlphabet(alphabet: MutableList<String>): Boolean {
        Alphabet = alphabet
        return true
    }

    public fun getStatesNames() : Array<String> {
        val states = ArrayList<String>()
        for (state in States) {
            states.add(state.Name)
        }
        return states.toTypedArray()
    }

    public fun getAllAlphabet() : Array<String>{
        return Alphabet.toTypedArray()
    }
}
