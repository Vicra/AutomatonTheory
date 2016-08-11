package AutomatonTheory.Kotlin.AutomatonLogic

import java.io.Serializable
import java.util.*

public open class State : Serializable {
    open var Name: String = ""
    open var InitialState: Boolean = false
    open var AcceptanceState: Boolean = false
    open var Transitions: MutableList<Transition> = ArrayList()

    constructor() {

    }

    constructor(name: String, initialState: Boolean, acceptanceState: Boolean) {
        Name = name
        InitialState = initialState
        AcceptanceState = acceptanceState
    }

    fun addTransition(newTransition: Transition): Boolean {
        if (!existsTransition(newTransition)) {
            return Transitions.add(newTransition)
        }
        return false
    }

    fun existsTransition(transition: Transition): Boolean {
        for (item in Transitions) {
            if (item.DestinyState.Name == transition.DestinyState.Name && item.Symbol == transition.Symbol) {
                return true
            }
        }
        return false
    }

    fun getDestinyStates(symbol:String) : MutableList<State> {
        var destinyStates:MutableList<State> = ArrayList()
        for(transition in Transitions){
            if(transition.Symbol == symbol) {
                destinyStates.add(transition.DestinyState)
            }
        }
        return destinyStates.distinct().toMutableList()
    }

    fun existsDestinyState(name:String) : Boolean{
        for(transition in Transitions){
            if(transition.DestinyState.Name == name){
                return true
            }
        }
        return false
    }
}
