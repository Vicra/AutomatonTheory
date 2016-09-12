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

        var trashState:State = State("", false, false)
        States.add(trashState)

        for(state in States){
            for(symbol in Alphabet) {
                var hasTransition = false
                for (transition in state.Transitions) {
                    if(transition.Symbol.equals(symbol)){
                        hasTransition = true
                    }
                }
                if(!hasTransition){
                    state.Transitions.add(Transition(getState(""),symbol))
                }
            }
        }

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
        if(destinyState.Name.equals("")){
            addState(State(destinyStateName, false, false))
        }

        //validar que el origen y symbol no se pueden repetir
        var canAddTransition = true
        for (state in States) {
            for (transition in state.Transitions) {
                if (state.Name == originStateName && transition.Symbol == symbol) {
                    canAddTransition = false
                    return false
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

    fun minimize():DeterministicFiniteAutomaton{
        var returnDfa = DeterministicFiniteAutomaton(this.AutomatonName)
        var states:MutableList<State> = mutableListOf()
        var acceptanceStates:MutableList<State> = mutableListOf()

        for (state in States) {
            if (state.AcceptanceState){
                acceptanceStates.add(state)
            }
            else{
                states.add(state)
            }
        }

        var equivalentStates:MutableList<MutableList<State>> = mutableListOf()
        if (states.isNotEmpty()){
            equivalentStates.add(states)
        }
        if (acceptanceStates.isNotEmpty()){
            equivalentStates.add(acceptanceStates)
        }

        var symbols:MutableList<String> = mutableListOf()

        for (state in States) {
            for (transition in  state.Transitions) {
                if(!symbols.contains(transition.Symbol)) {
                    symbols.add(transition.Symbol)
                }
            }
        }
        var hasNotEquivalentStates = true
        while (hasNotEquivalentStates) {
            var stateReferenceList:MutableList<MutableList<State>> = mutableListOf()
            var notEquivalentStates:MutableList<State> = mutableListOf()

            for (equivalentState in equivalentStates) {
                stateReferenceList.add(mutableListOf<State>())
                stateReferenceList.last().add(equivalentState.first())
                for (stateItem in equivalentState){
                if (stateItem.Name != equivalentState[0].Name) {
                    var equivalent = true
                    for (symbol in symbols) {
                        if (getTransitions(stateItem, symbol).isNotEmpty() && getTransitions(equivalentState.first(), symbol).isNotEmpty()) {
                            val state1 = getTransitions(stateItem, symbol).first()
                            val state2 = getTransitions(equivalentState.first(), symbol).first()
                            if (state1 != null && state2 != null) {
                                if (!statesAreEquivalent(getState(state1.DestinyState.Name), getState(state2.DestinyState.Name), equivalentStates)) {
                                    equivalent = false
                                }
                            }
                        }
                    }
                    if (equivalent) {
                        stateReferenceList[stateReferenceList.size - 1].add(stateItem)
                    } else {
                        notEquivalentStates.add(stateItem)
                    }
                }
            }
            }
            equivalentStates = stateReferenceList
            if (notEquivalentStates.isNotEmpty()) {
                equivalentStates.add(notEquivalentStates)
            } else {
                hasNotEquivalentStates = false
            }

        }

        for (equivalentState in equivalentStates) {
            var stateName:String = ""
            var acceptedState:Boolean = false
            var initialState:Boolean = false
            for (state in equivalentState) {
                stateName += state.Name
                if (state.AcceptanceState){
                    acceptedState = true
                }
                if (state.InitialState){
                    initialState = true
                }
            }
            var newState:State = State(stateName, initialState, acceptedState)
            returnDfa.addState(newState)
        }

        for (index in equivalentStates.indices) {
            for (state in States) {
                    if (state.Name == equivalentStates[index].first().Name) {
                    for (transition in state.Transitions) {
                        for (destinyIndex in equivalentStates.indices) {
                            if (equivalentStates[destinyIndex].contains(getState(transition.DestinyState.Name))) {
                                returnDfa.addTransition(returnDfa.States[index].Name, returnDfa.States[destinyIndex].Name, transition.Symbol)
                            }
                        }
                    }
                }
            }
        }
        return returnDfa
    }

    fun statesAreEquivalent(stateA:State, stateB:State, equivalentStates:MutableList<MutableList<State>>):Boolean{
        if (stateA.Name == stateB.Name) {
            return true
        }
        for (equivalentState in equivalentStates) {
            var existsEquivalence = false
            for (state in equivalentState) {
                if (state.Name == stateA.Name || state.Name == stateB.Name) {
                    if (existsEquivalence) {
                        return true
                    }
                    else {
                        existsEquivalence = true
                    }
                }
            }
            if (existsEquivalence) {
                return false
            }
        }
        return true
    }

    fun getTransitions(state:State, symbol:String) : MutableList<Transition> {
        var transitions:MutableList<Transition> = mutableListOf()
        for(transition in state.Transitions){
            if(transition.Symbol.equals(symbol)){
                transitions.add(transition)
            }
        }
        return transitions
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
                    this.States.add(newState) // crea y agrega un nuevo estado
                    for(trans in originState.Transitions){ // agrega las transiciones
                        newState.Transitions.add(Transition(getState(trans.DestinyState.Name),trans.Symbol))
                    }
                    //remueve los estados que ya no se ocupan
                    return true
                }
                else{
                    elementos.get(originIndex, destinyIndex).Equivalente = false
                }
            }
        }
        return false
    }

    fun toRegex():String{
        var returnRegex:String = ""

        var automaton:DeterministicFiniteAutomaton = DeterministicFiniteAutomaton(this.AutomatonName)
        for(state in this.States){
            automaton.States.add(State(state.Name, state.InitialState, state.AcceptanceState))
        }

        for(state in this.States){
            for(transition in state.Transitions){
                automaton.getState(state.Name).Transitions.add(Transition(automaton.getState(transition.DestinyState.Name),transition.Symbol))
            }
        }
        val automatonInitialState = automaton.getInitialState()
        var myArray:Array2D<String> = Array2D<String>(automaton.States.size, automaton.States.size) as Array2D<String>

        var i:Int = 0
        var j:Int = 0
        var k:Int = 0

        //llenar la matriz de vacios
        for(rowIndex in automaton.States.indices){
            for(columnIndex in automaton.States.indices){
                myArray.set(rowIndex, columnIndex, "")
            }
        }

        for(stateA in automaton.States) {
            val originState = stateA
            val originStateTransitions = originState.Transitions
            if (originStateTransitions.size > 0) {
                j = 0
                for (stateB in automaton.States) {
                    k = 0
                    val destinyState = stateB
                    val destinyStateTransitions = destinyState.Transitions

                    while(k < destinyStateTransitions.size){
                        val transition = destinyStateTransitions[k]
                        if(transition.DestinyState.Name.equals(destinyState.Name)){
                            if(k == 0){
                                if(myArray[i, j] == ""){
                                    myArray[i,j] = transition.Symbol
                                }
                                else{
                                    myArray[i,j] += transition.Symbol
                                }
                            }
                            else{
                                myArray[i, j] += "+" + transition.Symbol
                            }
                            if(originState.Name.equals(destinyState.Name)){
                                myArray[i, j] = "*"
                            }
                        }
                        k++
                    }
                    j++
                }
            }
            i++
        }
        k = 0
        while(k < automaton.States.size){
            i = 0
            for(stateA in automaton.States.indices){
                j = 0
                for(stateB in automaton.States.indices){
                    val sourceToPivot = myArray[i, k]
                    val pivotToDestiny = myArray[k, j]
                    var appendingString:String = "(" + sourceToPivot + "." + pivotToDestiny + ")"

                    if(sourceToPivot == "" || sourceToPivot == "")
                    {

                    }
                    else
                    {

                        if(i == j)
                        {
                            appendingString +="*"
                        }
                        if(myArray[i,j] == "")
                        {
                            myArray[i,j] = appendingString
                        }
                        else
                        {
                            myArray[i,j] += "+" +  appendingString
                        }

                    }
                    j++
                }
                i++
            }
            k++
        }

        var initialState = -1
        val finalStateIndexes = mutableListOf<Int>()
        for(stateA in automaton.getAcceptanceStates())
        {
            i = 0
            for (state in automaton.States)
            {
                if(stateA.Name.equals(state.Name))
                {
                    finalStateIndexes.add(i)
                }
                i++
            }
        }

        i= 0
        for(state in automaton.States)
        {
            if(state.Name.equals(automatonInitialState.Name))
            {
                initialState = i
            }
            i++
        }

        for(finalStateIndex in finalStateIndexes)
        {
            if(returnRegex == "")
            {
                returnRegex = myArray[initialState, finalStateIndex]
            }
            else
            {
                returnRegex += "+" + myArray[initialState, finalStateIndex]
            }
        }

        return returnRegex
    }
}