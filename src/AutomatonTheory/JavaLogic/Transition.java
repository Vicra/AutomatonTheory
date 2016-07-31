package AutomatonTheory.JavaLogic;

public class Transition {

    State DestinyState = new State();
    String Symbol = "";

    public Transition(){

    }
    public Transition(State destinyState, String symbol){
        DestinyState = destinyState;
        Symbol = symbol;
    }
}
