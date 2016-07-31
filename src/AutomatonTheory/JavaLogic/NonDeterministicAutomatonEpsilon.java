package AutomatonTheory.JavaLogic;

public class NonDeterministicAutomatonEpsilon extends NonDeterministicAutomaton {

    public NonDeterministicAutomatonEpsilon(){
        Type = Automatons.NFAe;
    }

    public NonDeterministicAutomatonEpsilon(String automatonName) {
        this();
        AutomatonName = automatonName;
    }
}
