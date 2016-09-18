package AutomatonsTheory.AutomatonExtensions.RegularExpressionExtensions

import AutomatonsTheory.AutomatonLogic.State
import java.util.*

class RegularExpressionState(): State(){
    var originStatesList:ArrayList<String> = ArrayList()

    fun containsCircularTransition():Boolean{
        for(transition in Transitions){
            if(transition.DestinyState.Name.equals(Name)){
                return true
            }
        }
        return false
    }
}