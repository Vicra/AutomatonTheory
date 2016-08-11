package AutomatonTheory.Kotlin.FileControllerJava;

import AutomatonTheory.Kotlin.AutomatonLogic.Automaton;

import java.io.IOException;

public interface IFileManager {
    Automaton loadAutomaton(String automatonName) throws IOException;
    boolean saveAutomaton(Automaton automaton);
}
