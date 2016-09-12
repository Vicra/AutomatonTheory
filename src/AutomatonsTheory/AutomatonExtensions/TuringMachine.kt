package AutomatonsTheory.AutomatonExtensions

class TuringMachine(automatonName:String) : DeterministicFiniteAutomaton(automatonName) {
    
    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}