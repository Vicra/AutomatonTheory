package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons

open class  PushDownAutomaton() : Automaton(){

    var startState: String = ""
    var initialStackSymbol: String = "Z"
    val epsilon = "e"
    var Transitions: MutableMap<Triple<String, String, String>, Set<Pair<String, List<String>>>>? = mutableMapOf()

    init {
        Type = Automatons.PDA
    }

    constructor(automatonName: String) : this(){
        AutomatonName = automatonName
    }

    constructor(automatonName: String, alphabet:MutableList<String>) : this(){
        AutomatonName = automatonName
        this.Alphabet = alphabet
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        var initState = getInitial()
        if (initState != null){
            startState = initState.Name
        }
        var myStack:java.util.Stack<String> = java.util.Stack()
        myStack.push(initialStackSymbol)
        var endState: String? = delta(startState, stringEvaluate, myStack)
        return isAcceptanceState(endState)
    }

    fun addTransition(from: String, to: String, symbol: Char, stackSymbol: String, pushSymbols: List<String>): Boolean {
        if(Transitions!!.contains(Triple(from, symbol.toString(), stackSymbol))){
            var mySet:MutableSet<Pair<String, List<String>>> = mutableSetOf()
            mySet.add(Pair(to, pushSymbols))
            var myMap:MutableMap<Triple<String, String, String>, Set<Pair<String, List<String>>>> = mutableMapOf()
            Transitions!!.plus(myMap)
        }else{
            var mySet:MutableSet<Pair<String, List<String>>> = mutableSetOf()
            mySet.add(Pair(to, pushSymbols))
            Transitions!!.set(Triple(from, symbol.toString(),stackSymbol),mySet.toSet())
        }
        return true
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        var valores = symbol.split(",") // vienen 2 valores {symbolo , pop y push}
        var popYpush = valores[1].split("/")
        var pushList = popYpush[1].split(".")
        return addTransition(originStateName, destinyStateName,valores[0][0],popYpush[0],pushList)
    }

    fun delta(q:String,wa:String, stack:java.util.Stack<String>): String? {
        var poppedValue = "ε"
        if(stack.size != 0)
            poppedValue = stack.pop()
        if(wa.length == 0){
            if(Transitions!!.contains(Triple(q,epsilon,poppedValue))){
                var destinations = Transitions!![Triple(q, epsilon, poppedValue)]
                for (destination in destinations!!){

                    var newStack = stack
                    for(component in destination.component2()){
                        newStack.push(component)
                    }
                    var result = delta(destination.component1(), wa, newStack)
                    if(result != null){
                        return  result
                    }
                }
            }
            return q
        }
        var symbol : Char = wa[0]
        val w : String = wa.drop(1)
        if(Transitions!!.contains(Triple(q, symbol.toString(), poppedValue))){
            var destinations = Transitions!![Triple(q, symbol.toString(), poppedValue)]
            for(destination in destinations!!){
                var newStack = stack
                for(component in destination.component2()){
                    newStack.push(component)
                }
                var result = delta(destination.component1(), w, newStack)
                if(result != null){
                    return  result
                }
            }
        }
        if(Transitions!!.contains(Triple(q, epsilon, poppedValue))){
            var destinations = Transitions!![Triple(q, epsilon, poppedValue)]
            for(destination in destinations!!){
                var newStack = stack
                for(component in destination.component2()){
                    newStack.push(component)
                }
                var result = delta(destination.component1(), wa, newStack)
                if(result != null){
                    return  result
                }
            }
        }
        return null
    }

    open fun isAcceptanceState(stateName:String?):Boolean{
        for(state in States){
            if(state.Name.equals(stateName)){
                if(state.AcceptanceState){
                    return true
                }
                else{
                    return false
                }
            }
        }
        return false
    }
}