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

                    for(transitionA in originState.Transitions) {
                        if (transitionA.DestinyState.Name == state.Name) { // si la transicion va hacia el estado a borrar

                            for (transitionB in state.Transitions) { // por cada transicion que sale del estado a borrar
                                //originState.Transitions.add(Transition(transitionB.DestinyState, transitionB.Symbol + "," + transitionA.Symbol ))

                                var orState = originState.Name
                                var desState = transitionB.DestinyState.Name

                                if (orState == desState) {
                                    var trans = Transition(transitionB.DestinyState, "(" + transitionA.Symbol + transitionB.Symbol + ")*")
                                    transitionsToAdd.add(trans)
                                } else {
                                    transitionsToAdd.add(Transition(transitionB.DestinyState, transitionA.Symbol + transitionB.Symbol))
                                }
                            }
                        }
                    }
                    for(newTransitions in transitionsToAdd){
                        originState.Transitions.add(newTransitions)
                        regularExpression = newTransitions.Symbol
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

    fun minimize() : Unit {
        var elementos: Array2D<Elemento> = Array2D<Elemento>(States.size, States.size) as Array2D<Elemento>
        for (originIndex in this.States.indices) {
            for (destinyIndex in this.States.indices) {
                if (originIndex != destinyIndex && originIndex > destinyIndex) {
                    equivalencia(originIndex, destinyIndex, elementos)
                }
            }
        }
        for (originIndex in this.States.indices) {
            for (destinyIndex in this.States.indices) {
                if (originIndex != destinyIndex && originIndex > destinyIndex) {
                    if(elementos.get(originIndex, destinyIndex).Equivalente){
                        var originState = getState(originIndex)
                        var destinyState = getState(destinyIndex)
                        this.States.add(State(originState.Name + "," + destinyState.Name ,originState.InitialState, originState.AcceptanceState))
                    }
                }
            }
        }
    }

    private fun equivalencia(originIndex: Int, destinyIndex: Int, elementos:Array2D<Elemento>) : Boolean{

        if(elementos.get(originIndex, destinyIndex).Visitado == false){
            elementos.get(originIndex, destinyIndex).Visitado = true
            var originState = getState(originIndex)
            var destinyState = getState(destinyIndex)

            if(originState.AcceptanceState == destinyState.AcceptanceState){ // si ambos son de aceptacion o no

                var validaciones:MutableList<Boolean> = ArrayList<Boolean>()
                for (symbol in this.Alphabet) {
                    var stateA = originState.getDestinyState(symbol)
                    var stateB = destinyState.getDestinyState(symbol)

                    if (!stateA.Name.equals(stateB.Name)) { // si los destinos no son  iguales se van en la recusion
                        var stateAIndex = this.States.indexOf(stateA)
                        var stateBIndex = this.States.indexOf(stateB)
                        if(equivalencia(stateAIndex, stateBIndex, elementos)){
                            validaciones.add(true)
                        }
                        else{
                            validaciones.add(false)
                        }
                    }
                    else{ // si todos los estados van al mismo estado con un simbolo
                        validaciones.add(true)
                    }
                }
                if(!validaciones.contains(false)){ // si son equivalentes entonces ... osea si existe equivalencia con todos los simbolos
                    elementos.get(originIndex, destinyIndex).Equivalente = true
                    var newState = State(originState.Name + "," + destinyState.Name ,originState.InitialState, originState.AcceptanceState)
                    this.States.add(newState)
                    for(trans in originState.Transitions){
                        newState.Transitions.add(Transition(getState(trans.DestinyState.Name),trans.Symbol))
                    }
                    return true
                }
                else{

                }
            }
        }
        return false
    }


}

class Array2D<T> (val xSize: Int, val ySize: Int, val array: Array<Array<T>>) {

    companion object {

        inline operator fun <reified T> invoke() = Array2D(0, 0, Array(0, { emptyArray<T>() }))

        inline operator fun <reified T> invoke(xWidth: Int, yWidth: Int) =
                Array2D(xWidth, yWidth, Array(xWidth, { arrayOfNulls<T>(yWidth) }))

        inline operator fun <reified T> invoke(xWidth: Int, yWidth: Int, operator: (Int, Int) -> (T)): Array2D<T> {
            val array = Array(xWidth, {
                val x = it
                Array(yWidth, {operator(x, it)})})
            return Array2D(xWidth, yWidth, array)
        }
    }

    operator fun get(x: Int, y: Int): T {
        return array[x][y]
    }

    operator fun set(x: Int, y: Int, t: T) {
        array[x][y] = t
    }

    inline fun forEach(operation: (T) -> Unit) {
        array.forEach { it.forEach { operation.invoke(it) } }
    }

    inline fun forEachIndexed(operation: (x: Int, y: Int, T) -> Unit) {
        array.forEachIndexed { x, p -> p.forEachIndexed { y, t -> operation.invoke(x, y, t) } }
    }
}

class Elemento{
    public var Equivalente:Boolean = false
    public var Visitado:Boolean = false
}