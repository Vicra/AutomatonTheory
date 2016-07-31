package AutomatonTheory.JavaLogic;

import java.util.ArrayList;
import java.util.List;

public class State {
    String Name;
    Boolean InitialState;
    Boolean AcceptanceState;
    List<Transition> Transitions = new ArrayList<Transition>();

    public State(){

    }
    public State(String name, Boolean initialState, Boolean acceptanceState){
        Name = name;
        InitialState = initialState;
        AcceptanceState =acceptanceState;
    }

    public boolean addTransition (Transition newTransition){
        if(!existsTransition(newTransition)){
            return Transitions.add(newTransition);
        }
        return false;
    }
    public boolean existsTransition(Transition transition) {
        for(Transition item : Transitions){
            if(item.DestinyState.Name.equals(transition.DestinyState.Name) &&
                    item.Symbol.equals(transition.Symbol)){
                return true;
            }
        }
        return false;
    }
}
