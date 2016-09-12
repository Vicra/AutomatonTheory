package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automatons
import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition

class TuringMachine(automatonName:String, alphabet:MutableList<String>) : DeterministicFiniteAutomaton(automatonName, alphabet) {

    init{
        this.Type = Automatons.TuringMachine
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        return super.addTransition(originStateName, destinyStateName, symbol)
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        if(stringEvaluate.isEmpty()){
            return false
        }
        var cinta:String = "B" + stringEvaluate + "B"
        var pointer:Int = 1
        var currentState:State? = null
        for(state in States){
            if (state.InitialState) {
                currentState = state
            }
        }
        if(currentState != null) {
            while(!currentState!!.AcceptanceState) {
                if(currentState.Name.isNotEmpty()){
                    if(pointer == cinta.length){
                        pointer -= 1
                    }
                    else if(pointer == -1){
                        pointer += 1
                    }
                    var currentTransition: Transition? = null
                    for(transition in currentState.Transitions){
                        if(transition.Symbol[0] == cinta[pointer]){
                            currentTransition = transition
                        }
                    }
                    if(currentTransition == null){
                        return false
                    }
                    val stringbuilder = StringBuilder(cinta)
                    stringbuilder[pointer] = currentTransition.Symbol[2]
                    cinta = stringbuilder.toString()
                    val direction = currentTransition.Symbol[4].toString()
                    if(direction == "L"){
                        pointer -= 1
                    }
                    else if(direction=="R") {
                        pointer += 1
                    }
                    currentState = getState(currentTransition.DestinyState.Name)
                }
            }
        }
        return true
    }
}