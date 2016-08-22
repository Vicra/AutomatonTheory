package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons
import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition
import java.util.*


open class DeterministicFiniteAutomaton(automatonName: String) : Automaton() {

    init {
        AutomatonName = automatonName
        Type = Automatons.DFA
    }

    constructor(automatonName: String, alphabet: MutableList<String>) : this(automatonName) {
        setAlphabet(alphabet)
    }

    override fun evaluateString(evaluationString: String): Boolean {
        val characters = evaluationString.toCharArray()
        var currentState = getInitialState()

        if (characters.size > 0) {
            for (character in characters) {
                if (Alphabet.contains("" + character)) {
                    var hasTransition = false
                    for (item in currentState.Transitions) {

                        if (item.Symbol == "" + character) {
                            currentState = item.DestinyState
                            hasTransition = true
                        }
                    }
                    if (!hasTransition) {
                        return false
                    }
                } else {
                    return false
                }
            }
            if (currentState.AcceptanceState) {
                return true
            } else {
                return false
            }
        }
        return false
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        val originState = getState(originStateName)
        val destinyState = getState(destinyStateName)

        //validar que el origen y symbol no se pueden repetir
        var canAddTransition = true
        for (state in States) {
            for (transition in state.Transitions) {
                if (state.Name == originStateName && transition.Symbol == symbol) {
                    canAddTransition = false
                    return false;
                }
            }
        }
        if (canAddTransition) {
            return originState.addTransition(Transition(destinyState, symbol))
        }
        return false
    }

    fun toRegularExpression():String{
        var regularExpression:String = ""

        var parsedAutomaton: DeterministicFiniteAutomaton = map()

        //initial should not have ingoing edges
        if(initialStateHasIngoingEdges()){
            createNewInitialState(parsedAutomaton);
        }
        //final should not have outgoing edges
        if(hasMoreThanOneAcceptanceState() || finalStateHasOutgoingEdges()){
            createNewAcceptanceState(parsedAutomaton);

            //estados aceptacion apuntan a el nuevo final con epsilon, y ya no son estados aceptacion
            for(state in parsedAutomaton.States){
                if(state.AcceptanceState && state.Name != "qf"){
                    state.Transitions.add(Transition(parsedAutomaton.getState("qf"),"e"))
                    state.AcceptanceState = false
                }
            }
        }
        //remove middle states
        var statesToRemove:MutableList<State> = ArrayList<State>()
        for(state in parsedAutomaton.States){
            if(!state.AcceptanceState && !state.InitialState){
                //transiciones que salen de el = osea las state.transition mas  n
                //transiciones que llegan a el                                  m
                //cruzarlas                                                     n x m

                for(originState in parsedAutomaton.States){// que pasa si hay una transicion que va a el mismo?

                    var transitionsToAdd:MutableList<Transition> = ArrayList<Transition>()

                    for(transitionA in originState.Transitions){
                        if(transitionA.DestinyState.Name == state.Name){ // si la transicion va hacia el estado a borrar

                            for(transitionB in state.Transitions){ // por cada transicion que sale del estado a borrar
                                //originState.Transitions.add(Transition(transitionB.DestinyState, transitionB.Symbol + "," + transitionA.Symbol ))
                                transitionsToAdd.add(Transition(transitionB.DestinyState, transitionB.Symbol + transitionA.Symbol ))
                            }
                        }
                    }

                    for(newTransitions in transitionsToAdd){
                        originState.Transitions.add(newTransitions)
                    }
                }
                //parsedAutomaton.removeState(state.Name)
                statesToRemove.add(state)
            }
        }
        for(state in statesToRemove){
            parsedAutomaton.removeState(state.Name)
        }
        println("fronen")
        return regularExpression
    }

    private fun map(): DeterministicFiniteAutomaton {
        var parsedAutomaton: DeterministicFiniteAutomaton = DeterministicFiniteAutomaton(this.AutomatonName);
        for (state in this.States) {
            var newState:State = State(state.Name, state.InitialState, state.AcceptanceState)
            parsedAutomaton.addState(newState)
        }
        for(state in States){
            for(transition in state.Transitions){
                parsedAutomaton.getState(state.Name).Transitions.add(Transition(parsedAutomaton.getState(transition.DestinyState.Name),transition.Symbol))
            }
        }
        for(symbol in this.Alphabet){
            parsedAutomaton.Alphabet.add(symbol)
        }
        return parsedAutomaton
    }

    fun initialStateHasIngoingEdges():Boolean{
        var initialState:State = this.getInitialState()
        for (state in this.States){
            for(transition in state.Transitions){
                if(transition.DestinyState.Name.equals(initialState.Name)){
                    return true
                }
            }
        }
        return false
    }

    fun finalStateHasOutgoingEdges() : Boolean{
        for(state in States){
            if(state.AcceptanceState){
                if(state.Transitions.count() > 0){
                    return true
                }
            }
        }
        return false
    }

    fun createNewInitialState(automaton:DeterministicFiniteAutomaton):Unit{
        var newInitialState:State = State("qs",false,false)
        var currentInitialState:State = automaton.getInitialState()
        newInitialState.addTransition(Transition(currentInitialState, "e"))
        automaton.addState(newInitialState)
        automaton.setInitialState(newInitialState.Name)
    }

    private fun createNewAcceptanceState(parsedAutomaton: DeterministicFiniteAutomaton) {
        var newAcceptanceState:State = State("qf", false, true)
        parsedAutomaton.States.add(newAcceptanceState)
    }

    fun hasMoreThanOneAcceptanceState():Boolean{
        var count = 0
        for(state in this.States){
            if(state.AcceptanceState){
                count ++
            }
        }
        if(count > 1){
            return true
        }
        return false
    }
}
