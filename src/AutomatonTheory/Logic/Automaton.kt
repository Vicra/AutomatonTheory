package AutomatonTheory.Logic

import java.util.*

/**
 * Q is a finite set of states.
 *
 * Σ is a finite set of symbols, called the alphabet of the automaton.
 *
 * δ is the transition function, that is, δ: Q × Σ → Q.
 *
 * q0 is the start state, that is, the state of the automaton before any input has been processed, where q0∈ Q.
 *
 * F is a set of states of Q (i.e. F⊆Q) called accept states.
 *
 * taken from https://en.wikipedia.org/wiki/Automata_theory
 */
open class Automaton() {
    open var States : MutableList<State> = ArrayList()
    open var Alphabet : MutableList<String> = mutableListOf("0","1")
    open var Name : String = ""

    /**
     * @constructor
     * Initializes a generic automaton.
     */
    constructor(states: MutableList<State>, alphabet : MutableList<String>) : this(){
        States = states
        Alphabet = alphabet
    }

    constructor(states: MutableList<State>, alphabet : MutableList<String>, name: String) : this(){
        States = states
        Alphabet = alphabet
        setAutomatonName(name)
    }

    fun setTheAlphabet(newAlphabet : MutableList<String>): Unit{
        Alphabet = newAlphabet
    }

    fun setAutomatonName(name : String) : Unit{
        this.Name = name
    }

    /**
     * Add a state to Q only if it does not exist.
     * @return true if added, false if it already exists.
     */
    fun addState(newState: State) : Boolean {
        if(!existsState(newState.Name)){
            return States.add(newState)
        }
        return false
    }

    fun removeState(state:State) : Boolean{
        //borra trans que salen de el
        state.Transitions.removeAll(state.Transitions);
//        for(trans in state.Transitions){
//            state.Transitions.remove(trans)
//        }

        //borra trans que llegan a el
        for (item in States){
            var toRemoveTransitions:MutableList<Transition> = ArrayList()
            for(trans in item.Transitions){
                if(trans.DestinyState.Name.equals(state.Name)){
                    toRemoveTransitions.add(trans)
                }
            }
            item.Transitions.removeAll(toRemoveTransitions);
        }
        return States.remove(state)
    }

    fun existsState(stateName : String) : Boolean {
        for( state in States ){
            if(state.Name.equals(stateName)){
                return true
            }
        }
        return false
    }

    fun removeStateWithName(stateName : String) : Boolean {
        for(state in States){
            if(state.Name.equals(stateName)){
                States.remove(state)
                return true
            }
        }
        return false
    }

    fun setAcceptanceState(stateName:String) : Boolean{
        for(state in States){
            if(state.Name.equals(stateName)){
                state.AcceptanceState = true;
                return true;
            }
        }
        return false;
    }

    fun toggleAcceptanceState(stateName:String) : Boolean {
        for(state in States){
            if(state.Name.equals(stateName)){
                if(state.AcceptanceState){
                    state.AcceptanceState = false;
                    return false;
                }
                else {
                    state.AcceptanceState = true;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Modify and existing state, @param [stateName] is the state to modify, @param [modifiedState] are the changes you want to apply.
     */
    fun modifyState(stateName:String, modifiedState:State):Unit{
        for(state in States){
            if(state.Name.equals((stateName))){
                state.Name = modifiedState.Name
                state.AcceptanceState = modifiedState.AcceptanceState
                state.InitialState = modifiedState.InitialState
            }
        }
    }

    fun printStates(): Unit{
        for(state in States){
            println(state.Name + " Init: " + state.InitialState+ " Accep: "+ state.AcceptanceState)
        }
    }

    fun getInitialState():State{
        for(state in States){
            if(state.InitialState){
                return state
            }
        }
        return State("default",true, false)
    }

    fun setInitialState(stateName:String) : Boolean{
        for(state in States){
            if(state.Name.equals(stateName)){
                state.InitialState = true;
            }
            else{
                state.InitialState = false;
            }
        }
        return true;
    }

    fun getState(stateName:String) :State{
        for(state in States){
            if(state.Name.equals(stateName)){
                return state
            }
        }
        return State()
    }

    fun getStatesNames() : Array<String> {
        var states:MutableList<String> = ArrayList()
        for(state in States){
            states.add(state.Name)
        }
        return states.toTypedArray()
    }

    fun getAllAlphabet() : Array<String> {
        return Alphabet.toTypedArray();
    }

    fun printTransitions():Unit{
        for(state in States){
            for(transition in state.Transitions){
                print(state.Name + ",")
                print(transition.DestinyState.Name + ",")
                println(transition.Symbol)
            }
        }
    }

    fun getAutomatonInfo() : String {
        var returnString :String = ""
        returnString += "Alphabet:" + "\n"
        for(symbol in Alphabet){
            returnString += "  * " + symbol + "\n"
        }
        returnString+= "\n" + "States: " + "\n"
        for(state in States){
            returnString += " * " + state.Name + ", Initial State: " + state.InitialState + ", Acceptance State: "+ state.AcceptanceState + "\n"
        }
        returnString+= "\n" + "Transitions: " + "\n"
        for(state in States){
            for(transition in state.Transitions){
                returnString += " * Origin State: " + state.Name + ", Destiny State: " + transition.DestinyState.Name + ", Symbol: " + transition.Symbol + "\n"
            }
        }
        return returnString
    }
}