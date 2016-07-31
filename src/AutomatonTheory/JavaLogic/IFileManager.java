package AutomatonTheory.JavaLogic;

public interface IFileManager {
    Automaton loadAutomaton(String automatonName);
    boolean saveAutomaton(Automaton automaton);
}
