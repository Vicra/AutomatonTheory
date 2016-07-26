package AutomatonTheory.Logic

open class DeterministicFiniteAutomaton{
    open var _automaton = Automaton()

    fun addState(newState:State) : Boolean{
        return _automaton.addState(newState)
    }

    fun removeState(state:State) : Boolean {
        return _automaton.removeState(state)
    }

    /**
     * Evaluates the send string
     * @return true if the string passed the automaton
     */
    fun evaluateString(evaluationString:String) : Boolean {
        var characters = evaluationString.toCharArray()
        var currentState:State = _automaton.getInitialState()

        if(characters.any()) {
            for (char in characters) {
                for(item in currentState.Transitions){
                    if(item.Symbol.equals(char.toString())){
                        currentState = item.DestinyState
                    }
                }
            }
            if(currentState.AcceptanceState){
                return true
            }
            else{
                return false
            }
        }
        return false
    }
}