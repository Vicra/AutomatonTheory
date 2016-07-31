package AutomatonTheory.JavaLogic;

public class NonDeterministicAutomaton extends Automaton {

    public NonDeterministicAutomaton(){
        Type = Automatons.NFA;
    }

    public NonDeterministicAutomaton(String automatonName){
        this();
        AutomatonName = automatonName;
    }

    @Override
    boolean evaluateString(String stringEvaluate) {
        return false;
    }

    @Override
    boolean addTransition(String originStateName, String destinyStateName, String symbol) {
        return false;
    }
}
