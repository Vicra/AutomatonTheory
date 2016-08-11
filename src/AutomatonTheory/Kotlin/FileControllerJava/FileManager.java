package AutomatonTheory.Kotlin.FileControllerJava;


import AutomatonTheory.Kotlin.AutomatonExtensions.DeterministicFiniteAutomaton;
import AutomatonTheory.Kotlin.AutomatonExtensions.NonDeterministicFiniteAutomaton;
import AutomatonTheory.Kotlin.AutomatonExtensions.NonDeterministicFiniteEpsilonAutomaton;
import AutomatonTheory.Kotlin.AutomatonLogic.Automaton;
import AutomatonTheory.Kotlin.AutomatonLogic.State;
import AutomatonTheory.Kotlin.AutomatonLogic.Transition;

import java.io.*;
import java.nio.charset.Charset;

public class FileManager implements IFileManager{

    String ln = System.lineSeparator();
    @Override
    public Automaton loadAutomaton(String automatonName) throws IOException {
        Automaton returnAutomaton = null;

        String allText = "";
        String line;
        try (
                InputStream fis = new FileInputStream(automatonName + ".txt");
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
        ) {
            while ((line = br.readLine()) != null) {
                if(line.contains("Type")){
                    String[] elements = line.split(":");
                    if(elements[1].equals("DFA")){
                        returnAutomaton = new DeterministicFiniteAutomaton(automatonName);
                    }
                    if(elements[1].equals("NFA")){
                        returnAutomaton = new NonDeterministicFiniteAutomaton(automatonName);
                    }
                    if(elements[1].equals("NFAe")){
                        returnAutomaton = new NonDeterministicFiniteEpsilonAutomaton(automatonName);
                    }
                }
                if(line.contains("Alphabet")){
                    String[] elements = line.split(":");
                    String[] symbols = elements[1].split(";");
                    for(int i = 0; i < symbols.length ; i++){
                        returnAutomaton.getAlphabet().add(symbols[i]);
                    }
                }
                if(line.contains("States")){
                    String[] elements = line.split(":");
                    String[] states = elements[1].split(";");
                    for(int i = 0; i < states.length ; i++){
                        String[] attributes = states[i].split(",");
                        returnAutomaton.getStates().add(new State(attributes[0], (attributes[1].equals("true"))?true:false, (attributes[2].equals("true"))?true:false));
                    }
                }
                if(line.contains("Transitions")){
                    String[] elements = line.split(":");
                    String[] transitions = elements[1].split(";");
                    for(int i = 0; i< transitions.length ; i++){
                        String [] attributes = transitions[i].split(",");
                        State currentState = returnAutomaton.getState(attributes[0]);
                        currentState.addTransition(new Transition(returnAutomaton.getState(attributes[1]),attributes[2]));
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnAutomaton;
    }

    @Override
    public boolean saveAutomaton(Automaton automaton) {
        try{
            FileWriter fw = new FileWriter(automaton.getAutomatonName() + ".txt");

            fw.write("Type:" + automaton.getType().toString() + ln);
            fw.write("Alphabet:");
            for(int i = 0; i < automaton.getAlphabet().size() ; i++){
                fw.write(automaton.getAlphabet().get(i));
                if(i < automaton.getAlphabet().size()-1){
                    fw.write(";");
                }
            }
            fw.write(ln);

            fw.write("States:");
            for(int i = 0; i < automaton.getStates().size() ; i++){
                State state = automaton.getStates().get(i);
                fw.write(state.getName() + "," + state.getInitialState()+ "," + state.getAcceptanceState());
                if(i < automaton.getAlphabet().size()-1){
                    fw.write(";");
                }
            }
            fw.write(ln);

            fw.write("Transitions:");

            for(int i = 0; i < automaton.getStates().size() ; i++){
                State state = automaton.getStates().get(i);
                for(int j = 0; j < state.getTransitions().size() ; j++){
                    Transition transition = state.getTransitions().get(j);
                    if(automaton.getStates().size()-1 == i && state.getTransitions().size()-1 == j){
                        fw.write(state.getName() + "," + transition.getDestinyState().getName() + "," + transition.getSymbol());
                    }
                    else{
                        fw.write(state.getName() + "," + transition.getDestinyState().getName() + "," + transition.getSymbol() + ";");
                    }

                }
            }

            fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
