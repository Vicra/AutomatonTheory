package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition

class TuringMachine(automatonName:String) : DeterministicFiniteAutomaton(automatonName) {

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        if(stringEvaluate.isEmpty()){
            return false
        }
        var cinta:String = "B" + stringEvaluate + "B"
        var pointer:Int = 1
        var currentState:State? = null
        for(elem in States){
            if (elem.InitialState) {
                currentState = elem
            }
        }
        if(currentState != null) {
            while(!currentState!!.AcceptanceState) {
                if(pointer == cinta.length){
                    pointer -= 1
                }
                else if(pointer == -1){
                    pointer += 1
                }
                var tmpTrans: Transition? = null
                for(trans in currentState.Transitions){
                    if(trans.Symbol[0] == cinta[pointer]){
                        tmpTrans = trans
                    }
                }
                if(tmpTrans == null){
                    return false
                }
                val stringbuilder = StringBuilder(cinta)
                stringbuilder[pointer] = tmpTrans.Symbol[2]
                cinta = stringbuilder.toString()
                val direction = tmpTrans.Symbol[4].toString()
                if(direction == "L"){
                    pointer -= 1
                }
                else if(direction=="R") {
                    pointer += 1
                }
                currentState = getState(tmpTrans.DestinyState.Name)
            }
        }
        return true
    }
}