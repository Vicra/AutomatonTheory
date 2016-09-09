package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.State

class ContextFreeGrammar {

    fun toPushDownAutomaton() : PushDownAutomaton{
        var returnPDA = PushDownAutomaton()
        val q0:State = State("q0", true, false)
        val q1:State = State("q1", false, false)
        val q2:State = State("q1", false, true)

        returnPDA.addState(q0)
        returnPDA.addState(q1)
        returnPDA.addState(q2)

        returnPDA.addTransition("q0", "q1", "e,Zo/eZo")
        returnPDA.addTransition("q1", "q2", "e,Zo/Zo")

        returnPDA.stack.push("Zo")
        return returnPDA
    }
}