package AutomatonTheory.JavaLogic;

import java.util.ArrayList;
import java.util.List;

public abstract class Automaton {
    String AutomatonName = "";
    List<State> States = new ArrayList<State>();
    List<String> Alphabet = new ArrayList<String>();
    Automatons Type = Automatons.DFA;

    abstract boolean evaluateString(String stringEvaluate);
    abstract boolean addTransition(String originStateName, String destinyStateName, String symbol);

    //region States
    boolean addState(State newState){
        if(!existsState(newState.Name)){
            return States.add(newState);
        }
        return false;
    }

    boolean removeState(String stateName){
        State originState = getState(stateName);
        //borra las transiciones que salen de el
        originState.Transitions.removeAll(originState.Transitions);

        //borra trans que llegan a el
        for (State state : States){
            List<Transition> toRemoveTransitions = new ArrayList<Transition>();
            for(Transition trans : state.Transitions){
                if(trans.DestinyState.Name.equals(originState.Name)){
                    toRemoveTransitions.add(trans);
                }
            }
            state.Transitions.removeAll(toRemoveTransitions);
        }

        //se borra el mismo estado
        return States.remove(originState);
    }

    boolean existsState(String stateName){
        for(State state : States){
            if(state.Name.equals(stateName)){
                return true;
            }
        }
        return false;
    }

    boolean toggleAcceptanceState(String stateName){
        for(State state : States){
            if(state.Name.equals(stateName)){
                state.AcceptanceState = !state.AcceptanceState;
                return true;
            }
        }
        return false;
    }

    State getInitialState(){
        for(State state : States){
            if(state.InitialState){
                return state;
            }
        }
        return new State();
    }

    boolean setInitialState(String stateName){
        for(State state : States){
            if(state.Name.equals(stateName)){
                state.InitialState = true;
            }
            else{
                state.InitialState = false;
            }
        }
        return true;
    }

    State getState(String stateName){
        for(State state : States){
            if(state.Name.equals(stateName)){
                return state;
            }
        }
        return new State();
    }
    //endregion

    //region Transitions
    boolean removeTransition(String originStateName, String destinyStateName, String symbol){
        State originState = getState(originStateName);
        Transition transition = getTransition(originStateName, destinyStateName, symbol);
        if(transition != null){
            return originState.Transitions.remove(transition);
        }
        return false;
    }

    Transition getTransition(String originStateName,String destinyStateName, String symbol){
        for(State state : States){
            for(Transition transition : state.Transitions){
                if(state.Name.equals(originStateName) && transition.DestinyState.Name.equals(destinyStateName) && transition.Symbol.equals(symbol)){
                    return transition;
                }
            }
        }
        return null;
    }

    Boolean existsTransition(String originStateName, String destinyStatName, String symbol){
        for(State state : States){
            for(Transition transition : state.Transitions){
                if(state.Name.equals(originStateName) &&
                        transition.DestinyState.Name.equals(destinyStatName) &&
                        transition.Symbol.equals(symbol)){
                    return true;
                }
            }
        }
        return false;
    }
    //endregion

    String getAutomatonInfo(){
        String returnString  = "";
        returnString += "Alphabet:" + "\n";
        for(String symbol : Alphabet){
            returnString += "  * " + symbol + "\n";
        }
        returnString+= "\n" + "States: " + "\n";
        for(State state : States){
            returnString += " * " + state.Name + ", Initial State: " + state.InitialState + ", Acceptance State: "+ state.AcceptanceState + "\n";
        }
        returnString+= "\n" + "Transitions: " + "\n";
        for(State state : States){
            for(Transition transition : state.Transitions){
                returnString += " * Origin State: " + state.Name + ", Destiny State: " + transition.DestinyState.Name + ", Symbol: " + transition.Symbol + "\n";
            }
        }
        return returnString;
    }

    boolean setAlphabet(List<String> alphabet){
        Alphabet = alphabet;
        return true;
    }
}
