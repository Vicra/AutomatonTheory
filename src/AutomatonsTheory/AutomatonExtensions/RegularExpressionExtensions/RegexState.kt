package AutomatonsTheory.AutomatonExtensions.RegularExpressionExtensions

import AutomatonsTheory.AutomatonLogic.State
import java.util.*

class RegexState (): State(){
    var statesPointingToMe:ArrayList<String> = ArrayList()

    fun hasTransitionToItself():Boolean{
        return Transitions.filter { it.DestinyState.Name.equals(Name) }.size > 0
    }
}