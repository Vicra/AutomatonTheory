package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonExtensions.RegularExpressionExtensions.RegularExpressionState
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

    override fun evaluateString(stringEvaluate: String): Boolean {
        val characters = stringEvaluate.toCharArray()
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
                }
            }
        }
        if (canAddTransition) {
            return originState.addTransition(Transition(destinyState, symbol))
        }
        else{
            return false
        }
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

    fun toRegularExpression():String{
        return ConvertDeterministicToRegularExpression(ArrayList(this.States))
    }
    fun ConvertDeterministicToRegularExpression(deterministicAutomatonStates: ArrayList<State>):String{

        val states: ArrayList<RegularExpressionState> = ArrayList()
        for(state in deterministicAutomatonStates) {
            val newState = RegularExpressionState()
            newState.Name = state.Name
            newState.AcceptanceState = state.AcceptanceState
            newState.InitialState = state.InitialState
            newState.Transitions = state.Transitions
            states.add(newState)
        }

        var initialState: RegularExpressionState? = null
        for(state in states){
            if(state.InitialState){
                initialState = state
            }
        }

        if(initialState == null){
            return ""
        }

        val newInitialState: RegularExpressionState = RegularExpressionState()
        newInitialState.Name = "S"
        newInitialState.InitialState = true
        val transition:Transition = Transition()
        transition.Symbol = "e"

        var currentInitialState:State? = null
        for(state in states){
            if(state.InitialState){
                currentInitialState = state
            }
        }
        transition.DestinyState.Name = currentInitialState!!.Name
        currentInitialState.InitialState = false
        newInitialState.Transitions.add(transition)
        states.add(newInitialState)

        val newFinalState: RegularExpressionState = RegularExpressionState()
        newFinalState.Name = "F"
        newFinalState.AcceptanceState = true
        states.filter { it.AcceptanceState }.forEach {
            val transition = Transition()
            transition.Symbol = "e"
            transition.DestinyState.Name = "F"
            it.Transitions.add(transition)
            it.AcceptanceState = false
        }
        states.add(newFinalState)

        updateLevelForEachState(states)
        sortByLowerLevel(states)
        updateCircularTransitions(states)
        return getRegularExpression(states,initialState)
    }

    private fun updateLevelForEachState(states: ArrayList<RegularExpressionState>){
        for(state in states) {
            if(state.Name.equals("S")||state.Name.equals("F"))
                continue
            state.level = 0
            for(index in 0..(states.size-1)){
                for(transition in states[index].Transitions){
                    if(transition.Symbol.equals("e"))
                        continue
                    if(transition.DestinyState.Name.equals(state.Name)) state.level++ else state.level
                }
            }
        }
    }

    private fun sortByLowerLevel(states: ArrayList<RegularExpressionState>){
        states.sortBy { it.level }
    }

    private fun removeStateWithLowerLevel(stateToRemove: RegularExpressionState, states: ArrayList<RegularExpressionState>){
        if(stateToRemove.containsCircularTransition())
            removeTransitionsThatGoToSameState(stateToRemove)
        for(sourceToMe in stateToRemove.originStatesList){
            states.filter {it.Name.equals(sourceToMe)}.forEach {stateSource ->
                val toAddTransitions = ArrayList<Transition>()
                stateSource.Transitions.filter { it.DestinyState.Name.equals(stateToRemove.Name)}
                        .forEach {
                            transitionToStateToRemove ->
                            stateToRemove.Transitions.filter {!it.DestinyState.Name.equals(stateToRemove.Name)}
                                    .forEach { transitionToOtherStateFromStateToRemove ->
                                        val newTransition = getNewTransition(stateToRemove, transitionToOtherStateFromStateToRemove,
                                                transitionToStateToRemove)
                                        toAddTransitions.add(newTransition)
                                        stateSource.Transitions.remove(transitionToStateToRemove)
                                    }
                        }
                toAddTransitions.forEach { stateSource.Transitions.add(it)}
            }
        }
        states.remove(stateToRemove)
    }

    private fun getNewTransition(stateToRemove: RegularExpressionState, destinyTransition: Transition, pathSymbolTransition: Transition):Transition {
        val newTransition = Transition()
        newTransition.DestinyState.Name = destinyTransition.DestinyState.Name
        newTransition.Symbol = pathSymbolTransition.Symbol
        newTransition.Symbol += if (stateToRemove.containsCircularTransition())
            "(" + stateToRemove.Transitions.filter { it.DestinyState.Name.equals(stateToRemove.Name) }
                    .elementAt(0).Symbol + ")*" else ""
        newTransition.Symbol += destinyTransition.Symbol
        return newTransition
    }

    private fun updateCircularTransitions(states: ArrayList<RegularExpressionState>){
        for(state in states) {
            if(state.Name.equals("S")||state.Name.equals("F"))
                continue
            state.originStatesList.clear()
            for(x in 0..(states.size-1)){
                if(states[x].Name.equals(state.Name)||(states[x].Name.equals("S")||states[x].Name.equals("F")))
                    continue
                for(iTrans in states[x].Transitions){
                    if(iTrans.DestinyState.Name.equals(state.Name)&&!iTrans.Symbol.equals(state.Name))
                        state.originStatesList.add(states[x].Name!!)
                }
            }
        }
    }

    private fun removeTransitionsThatGoToSameState(state: RegularExpressionState){
        val trans:Transition = Transition()
        trans.DestinyState.Name = state.Name
        trans.Symbol = ""
        state.Transitions.filter { it.DestinyState.Name.equals(state.Name) }.forEach {
            trans.Symbol += it.Symbol + "+"
            state.Transitions.remove(it)
        }
        trans.Symbol = trans.Symbol.toString().substring(0,(trans.Symbol.toString().length-1))
        state.Transitions.add(trans)
    }

    private fun getRegularExpression(states: ArrayList<RegularExpressionState>, initialState: RegularExpressionState):String{
        while(states.size > 3){
            var removingIndex = 2
            if(ignoreState(states.elementAt(removingIndex),initialState))
                removingIndex = 3
            removeStateWithLowerLevel(states.elementAt(removingIndex),states)
            updateLevelForEachState(states)
            sortByLowerLevel(states)
            updateCircularTransitions(states)
        }
        if(states.elementAt(2).containsCircularTransition())
            removeTransitionsThatGoToSameState(states.elementAt(2))
        val finalState =states.filter { !it.Name.equals("S")&&!it.Name.equals("F") }.elementAt(0)
        var regex = concatenateRegularExpression(finalState)
        regex = removeLastConcatenation(regex)
        return regex.toString()
    }

    private fun concatenateRegularExpression(finalState: RegularExpressionState): String {
        var firstRegularExpression = ""
        finalState.Transitions.filter { !it.Symbol.equals("e") }.forEach {
            transition ->
            firstRegularExpression += "["
            firstRegularExpression += if (transition.DestinyState.Name.equals(finalState.Name)) "(" + transition.Symbol + ")*" else transition.Symbol
            firstRegularExpression += "] . "
        }
        return firstRegularExpression
    }

    private fun removeLastConcatenation(currentRegularExpression: String): String {
        var firstRegularExpression = currentRegularExpression
        if (firstRegularExpression.elementAt(firstRegularExpression.length - 2).equals('.')) {
            firstRegularExpression = firstRegularExpression.substring(0, (firstRegularExpression.length - 2))
            val resultantRegularExpression = firstRegularExpression.split("e")
            firstRegularExpression = ""
            for(str in resultantRegularExpression){
                firstRegularExpression += str
            }
        }
        return firstRegularExpression
    }

    private fun ignoreState(state: RegularExpressionState, initialState: RegularExpressionState):Boolean{
        return state.Name.equals(initialState.Name)||state.Name.equals("S")||state.Name.equals("F")
    }
}