package AutomatonTheory.Logic

import java.util.*

open class State() {
    open var Name : String = ""
    open var InitialState:Boolean = false
    open var AcceptanceState : Boolean = false
    open var Transitions : MutableList<Transition> = ArrayList()

    constructor(name:String, initialState: Boolean, acceptanceState: Boolean) : this(){
        Name = name
        InitialState = initialState
        AcceptanceState = acceptanceState
    }

    fun addTransition(newTransition : Transition) : Boolean{
        if(!existsTransition(newTransition)){
            return Transitions.add(newTransition)
        }
        return false
    }

    fun addTransition2(destinyState:String, symbol:String) : Boolean {
        //var newTransition = Transition(destinyState, symbol)
        return true;
    }

    fun removeTransition(transition:Transition):Boolean{
        if(existsTransition(transition)){
            return Transitions.remove(transition)
        }
        return false
    }

    fun existsTransition(transition : Transition) : Boolean {
        for(item in Transitions){
            if(item.DestinyState.Name.equals(transition.DestinyState.Name) &&
                item.Symbol.equals(transition.Symbol)){
                return true
            }
        }
        return false
    }
}