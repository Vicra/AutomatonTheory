package AutomatonTheory.JavaLogic;

import AutomatonTheory.Kotlin.AutomatonLogic.Automaton;

public interface IFileManager {
    Automaton loadAutomaton(String automatonName);
    boolean saveAutomaton(Automaton automaton);
}
