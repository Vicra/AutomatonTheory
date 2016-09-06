package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons
import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition

open class PushDownAutomaton() : Automaton(){
    var stack = Stack()

    open var Stack:MutableList<String> = mutableListOf()
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
        val initial = "Z"
        var transitionValues:MutableList<String> = mutableListOf()
        var consumed:MutableList<String> = mutableListOf()
        var currentState:State? = null
        var accepted=true

        for(state in States){
            if(state.InitialState){
                currentState=state
            }
            for(trans in state.Transitions){
                val pvalues = trans.Symbol
                val plist = pvalues.split("-")
                if(!transitionValues.contains(plist[0]))
                    transitionValues.add(plist[0])
            }
        }

        var stack:Stack = Stack()
        stack.push(initial)

        for(character in stringEvaluate.toCharArray()){
            var pop = stack.peek()
            stack.pop()
            var tempTransition: Transition? =null
            for(trans in currentState!!.Transitions){
                val plist = trans.Symbol.split("-")
                if(plist[0]==character.toString() && plist[1]==pop ){
                    tempTransition=trans
                }
            }
            if(tempTransition==null){
                accepted=false
                stack.push(pop)
            }else{
                val plist = tempTransition.Symbol.split("-")
                val push = plist[2]
                if(!push.isEmpty() && push!=" "){
                    for(j in push.toCharArray().indices){
                        stack.push(push.substring(push.length-1-j,push.length-j))
                    }
                }
                currentState=getState(tempTransition.DestinyState.Name)
            }
        }
        while(currentState!=null && !currentState.AcceptanceState && !consumed.contains(currentState.Name)){
            consumed.add(currentState.Name)
            val pop = stack.peek()
            stack.pop()
            var tempTransition:Transition? = null

            for(trans in currentState.Transitions ){
                val plist = trans.Symbol.split("-")
                if(plist[0]==" " && plist[1] == pop){
                    tempTransition=trans
                }
            }
            if(tempTransition!=null){
                val pvalue = tempTransition.Symbol.split("-")
                val push:String = pvalue[2]
                if(!push.isEmpty()){
                    for(j in push.toCharArray().indices){
                        stack.push(push.substring(push.length-1-j,push.length-j))
                    }
                }
                currentState=getState(tempTransition.DestinyState.Name)
            }else{
                currentState=null
            }
        }
        return accepted
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        return getState(originStateName).Transitions.add(Transition(getState(destinyStateName), symbol))
    }

    fun evaluate(strEvString:String):Boolean {
        var strToEval = strEvString.toList()
        var currentState = getInitialState()
        var strcpy = strEvString.toList()
        if (currentState != null) {
            for (character in strToEval) {
                for (transition in currentState!!.Transitions) {
                    var Symbol = getTransitionSymbol(transition.Symbol)
                    var pushValues = getTransitionPushValues(transition.Symbol)
                    if (stack.list.isEmpty() && getPushSymbol(transition.Symbol).equals("Z0")) {

                        if (pushValues.contains("Z0")) {
                            pushValues = pushValues.removeSuffix("Z0")
                            pushValues = pushValues.removePrefix("Z0")
                        }
                        for (item in pushValues) {
                            stack.push(item.toString())
                        }
                        var destination = getState(currentState.getDestinyState(transition.Symbol).Name.toString())
                        if (destination != null) {
                            currentState = destination
                        }
                        break
                    } else if (Symbol.equals(character) && !stack.list.isEmpty() && !pushValues.contains("E") && !getPushSymbol(transition.Symbol).equals("Z0")) {
                        var pushValues = getTransitionPushValues(transition.Symbol)
                        stack.pop()
                        for (item in pushValues) {
                            stack.push(item.toString())
                        }
                        var destination = getState(currentState.getDestinyState(transition.Symbol).Name.toString())
                        if (destination != null) {
                            currentState = destination
                        }
                        break
                    } else if (pushValues.contains("E") && !stack.list.isEmpty()) {
                        stack.pop()
                        var destination = getState(currentState.getDestinyState(transition.Symbol).Name.toString())
                        if (destination != null) {
                            currentState = destination
                        }
                        break
                    }
                }
            }
        }

        if (isAcceptancePDA() && currentState!!.AcceptanceState) {
            return true
        } else if (!isAcceptancePDA() && stack.list.isEmpty()) {
            return true
        }

        return false
    }

    fun isAcceptancePDA(): Boolean {
        for (state in States) {
            if (state.AcceptanceState) {
                return true
            }
        }
        return false
    }

    fun getTransitionSymbol(str:String): String {
        var pushVal = str.subSequence(str.lastIndexOf("(",str.length, false)+1, str.lastIndexOf(",",str.length, false))
        return pushVal.toString()
    }
    fun getPushSymbol (str: String): String {
        var pushVal = str.subSequence(str.lastIndexOf(",",str.length, false)+1, str.lastIndexOf("/",str.length, false))
        return pushVal.toString()
    }
    fun getTransitionPushValues(str:String):  String {
        var removedParenthesisStr = str.removeSurrounding("(",")")
        var pushVal = removedParenthesisStr.subSequence(removedParenthesisStr.lastIndexOf("/",removedParenthesisStr.length, false)+1, removedParenthesisStr.length)
        return pushVal.toString()
    }
}