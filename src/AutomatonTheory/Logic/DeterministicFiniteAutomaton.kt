package AutomatonTheory.Logic

open class DeterministicFiniteAutomaton{
    open var _automaton = Automaton()

    fun addState(newState:State) : Boolean{
        return _automaton.addState(newState)
    }

    fun removeState(state:String) : Boolean {
        return _automaton.removeState(_automaton.getState(state))
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
                //verificar char es parte de alfabeto
                if(_automaton.Alphabet.contains(char.toString())){
                    var hasTransition:Boolean = false
                    for(item in currentState.Transitions){

                        if(item.Symbol.equals(char.toString())){
                            currentState = item.DestinyState
                            hasTransition = true
                        }
                    }
                    if(!hasTransition){
                        return false;
                    }
                }
                else{
                    return false;
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