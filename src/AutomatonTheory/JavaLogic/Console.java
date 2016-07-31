package AutomatonTheory.JavaLogic;

import java.util.Arrays;

public class Console {
    public static void main(String [] args){
        FileManager fm = new FileManager();
        Automaton dfa = new DeterministicFiniteAutomaton("automaton", Arrays.asList("0","1"));

        State q0 = new State("q0", true, false);
        State q1 = new State("q1", false, true);

        q0.addTransition(new Transition(q1,"0"));
        q1.addTransition(new Transition(q0, "1"));

        dfa.addState(q0);
        dfa.addState(q1);

        fm.saveAutomaton(dfa);

        Automaton readAutomaton = fm.loadAutomaton("automaton");
        if(readAutomaton.evaluateString("01010101010")){
            System.out.println("paso");
        }
        else{
            System.out.println("no paso");
        }
        System.out.println(readAutomaton.getAutomatonInfo());

    }
}
