package AutomatonTheory.JavaLogic;

import java.util.List;

public class DeterministicFiniteAutomaton extends Automaton {

    public DeterministicFiniteAutomaton(String automatonName){
        AutomatonName = automatonName;
        Type = Automatons.DFA;
    }

    public DeterministicFiniteAutomaton(String automatonName, List<String> alphabet){
        this(automatonName);
        setAlphabet(alphabet);
    }

    @Override
    public boolean evaluateString(String evaluationString) {
        char[] characters = evaluationString.toCharArray();
        State currentState = getInitialState();

        if(characters.length > 0) {
            for (char character : characters) {
                if(Alphabet.contains(""+character)){
                    boolean hasTransition = false;
                    for(Transition item : currentState.Transitions){

                        if(item.Symbol.equals(""+character)){
                            currentState = item.DestinyState;
                            hasTransition = true;
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
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean addTransition(String originStateName, String destinyStateName, String symbol) {
        State originState = getState(originStateName);
        State destinyState = getState(destinyStateName);

        //validar que el origen y symbol no se pueden repetir
        boolean canAddTransition = true;
        for(State state : States){
            for(Transition transition : state.Transitions){
                if(state.Name.equals(originStateName) &&
                        transition.Symbol.equals(symbol)){
                    canAddTransition = false;
                }
            }
        }
        if(canAddTransition){
            return originState.addTransition(new Transition(destinyState, symbol));
        }
        return false;
    }
}