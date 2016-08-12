package AutomatonsTheory.AutomatonLogic

import java.io.Serializable
import java.util.*

open abstract class Automaton : Serializable {
    var AutomatonName = ""
    var States: MutableList<State> = ArrayList()
    open var Alphabet: MutableList<String> = ArrayList()
    var Type = Automatons.DFA

    abstract fun evaluateString(stringEvaluate: String): Boolean
    abstract fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean

    //region States
    fun addState(newState: State): Boolean {
        if (!existsState(newState.Name)) {
            return States.add(newState)
        }
        return false
    }

    fun removeState(stateName: String): Boolean {
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

    fun existsState(stateName: String): Boolean {
        for (state in States) {
            if (state.Name == stateName) {
                return true
            }
        }
        return false
    }

    fun toggleAcceptanceState(stateName: String): Boolean {
        for (state in States) {
            if (state.Name == stateName) {
                state.AcceptanceState = !state.AcceptanceState
                return true
            }
        }
        return false
    }

    fun getInitialState() : State {
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

    fun getState(stateName: String): State {
        for (state in States) {
            if (state.Name == stateName) {
                return state
            }
        }
        return State()
    }
    //endregion

    //region Transitions
    fun removeTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        val originState = getState(originStateName)
        val transition = getTransition(originStateName, destinyStateName, symbol)
        if (transition != null) {
            return originState.Transitions.remove(transition)
        }
        return false
    }

    fun getTransition(originStateName: String, destinyStateName: String, symbol: String): Transition? {
        for (state in States) {
            for (transition in state.Transitions) {
                if (state.Name == originStateName && transition.DestinyState.Name == destinyStateName && transition.Symbol == symbol) {
                    return transition
                }
            }
        }
        return null
    }

    fun existsTransition(originStateName: String, destinyStatName: String, symbol: String): Boolean? {
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

    fun getAutomatonInfo() : String {
        var returnString = ""
        returnString += "Name:" + AutomatonName + "\n"
        returnString += "Type:" + Type.name + "\n" + "\n"
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

    fun setAlphabet(alphabet: MutableList<String>): Boolean {
        Alphabet = alphabet
        return true
    }

    fun getStatesNames() : Array<String> {
        val states = ArrayList<String>()
        for (state in States) {
            states.add(state.Name)
        }
        return states.toTypedArray()
    }

    fun getAllAlphabet() : Array<String>{
        return Alphabet.toTypedArray()
    }
}
