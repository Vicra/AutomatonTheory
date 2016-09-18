package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons

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
}