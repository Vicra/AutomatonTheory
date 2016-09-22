package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonExtensions.RegularExpressionParserDirectory.RegularExpressionParser
import AutomatonsTheory.AutomatonExtensions.RegularExpressionParserDirectory.tree.CharNode
import AutomatonsTheory.AutomatonExtensions.RegularExpressionParserDirectory.tree.Node
import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons
import AutomatonsTheory.AutomatonLogic.State

class RegularExpression : Automaton() {

    init{
        Type = Automatons.Regex
    }
    override fun evaluateString(stringEvaluate: String): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun toNonDeterministicFiniteEpsilonAutomaton(regularExpression:String):NonDeterministicFiniteEpsilonAutomaton{
        try{
            var rootNode: Node = RegularExpressionParser().Parse(regularExpression)
            return constructENFA(rootNode)
        }
        catch(e:Exception) {
            println(e.message)
        }
        return NonDeterministicFiniteEpsilonAutomaton("")
    }

    var stateSequence = 1
    fun constructENFA(rootNode:Node):NonDeterministicFiniteEpsilonAutomaton{


        if(rootNode.javaClass.isInstance(CharNode(""))){

            val charENFA = NonDeterministicFiniteEpsilonAutomaton("")
            val s1 = stateSequence.toString()
            charENFA.addState(State(s1, false, false))
            stateSequence += 1
            var s2 = stateSequence.toString()
            charENFA.addState(State(s2,false, false))
            stateSequence += 1
            charENFA.addTransition(s1, s2, (rootNode as CharNode).value)
            //charENFA.q0 = charENFA.States[s1.toInt()]
            charENFA.States[s2.toInt()].AcceptanceState = true

            return charENFA
        }
        return null!!
    }
}