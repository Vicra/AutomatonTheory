package AutomatonTheory.Console

import AutomatonTheory.Logic.DeterministicFiniteAutomaton
import AutomatonTheory.Logic.State
import AutomatonTheory.Logic.Transition

fun main(args : Array<String>) {
    var dfa : DeterministicFiniteAutomaton = DeterministicFiniteAutomaton()

    //adding states
    dfa.addState(State("s1", true, true))
    dfa.addState(State("s2", false, false))
    dfa.addState(State("s2", false, false))
    dfa.addState(State("s2", false, false))

    //adding transition
    var state1:State = dfa._automaton.getState("s1")
    var state2:State = dfa._automaton.getState("s2")

    state1.addTransition(Transition(state2,"1"))
    state1.addTransition(Transition(state1,"0"))

    state2.addTransition(Transition(state1,"1"))
    state2.addTransition(Transition(state2,"0"))


    //evaluate automaton
    println("States")
    dfa._automaton.printStates()

    println("\nTransition")
    dfa._automaton.printTransitions()


    println("\nEvaluations Should Pass")
    if(dfa.evaluateString("1010")) println("Paso") else println("NO paso")
    if(dfa.evaluateString("1001")) println("Paso") else println("NO paso")
    if(dfa.evaluateString("101")) println("Paso") else println("NO paso")
    if(dfa.evaluateString("00")) println("Paso") else println("NO paso")

    println("\nEvaluations Should NOT Pass")
    if(dfa.evaluateString("100")) println("Paso") else println("NO paso")
    if(dfa.evaluateString("111")) println("Paso") else println("NO paso")
    if(dfa.evaluateString("1101")) println("Paso") else println("NO paso")
    if(dfa.evaluateString("01")) println("Paso") else println("NO paso")

}