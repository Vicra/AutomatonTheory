package AutomatonsTheory.AutomatonExtensions.RegularExpressionExtensions

import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition
import java.util.*


class AutomatonToRegularExpressionParser(){
    private val EPSILON = "e"

    fun ParseDFAToRegex(deterministicAutomatonStates: ArrayList<State>):String{

        val states = ArrayList<RegexState>()
        deterministicAutomatonStates.forEach {
            val state = RegexState()
            state.Name = it.Name
            state.AcceptanceState = it.AcceptanceState
            state.InitialState = it.InitialState
            state.Transitions = it.Transitions
            states.add(state)
        }

        val initialState = states.filter { it.InitialState }
        if(initialState.isEmpty())
            return ""
        changeTheNewInitialState(states)
        changeTheFinalState(states)
        updateTargetCountForEachState(states)
        sortByLessTarget(states)
        updatePointToMeStates(states)
        return getRegex(states,initialState.elementAt(0))
    }

    private fun changeTheNewInitialState(states: ArrayList<RegexState>){
        val newInitialState = RegexState()
        newInitialState.Name = "S"
        newInitialState.InitialState = true
        val transition = Transition()
        transition.Symbol = EPSILON
        transition.DestinyState.Name = (states.filter { it.InitialState }).elementAt(0).Name
        (states.filter { it.InitialState }).elementAt(0).InitialState = false
        newInitialState.Transitions.add(transition)
        states.add(newInitialState)
    }

    private fun changeTheFinalState(states: ArrayList<RegexState>){
        val newFinalState = RegexState()
        newFinalState.Name = "F"
        newFinalState.AcceptanceState = true
        states.filter { it.AcceptanceState }.forEach {
            val transition = Transition()
            transition.Symbol = EPSILON
            transition.DestinyState.Name = "F"
            it.Transitions.add(transition)
            it.AcceptanceState = false
        }
        states.add(newFinalState)
    }

    private fun updateTargetCountForEachState(states: ArrayList<RegexState>){
        for(it in states) {
            if(it.Name.equals("S")||it.Name.equals("F"))
                continue
            it.targetCount = 0
            for(xState in 0..(states.size-1)){
                for(transition in states[xState].Transitions){
                    if(transition.Symbol.equals(EPSILON))
                        continue
                    if(transition.DestinyState.Name.equals(it.Name)) it.targetCount++ else it.targetCount
                }
            }
        }
    }

    private fun sortByLessTarget(states: ArrayList<RegexState>){
        states.sortBy { it.targetCount }
    }

    private fun removeStateByLessTarget(stateToRemove:RegexState,states: ArrayList<RegexState>){
        if(stateToRemove.hasTransitionToItself())
            removeTransitionsThatPointingToMyself(stateToRemove)
        for(sourceToMe in stateToRemove.statesPointingToMe){
            states.filter {it.Name.equals(sourceToMe)}.forEach {stateSource ->
                val transitionsPendingToAdd = ArrayList<Transition>()
                stateSource.Transitions.filter { it.DestinyState.Name.equals(stateToRemove.Name)}
                        .forEach {
                            transitionToStateToRemove ->
                            stateToRemove.Transitions.filter {!it.DestinyState.Name.equals(stateToRemove.Name)}
                                    .forEach { transitionToOtherStateFromStateToRemove ->
                                        val newTransition = getNewTransition(stateSource, stateToRemove,
                                                transitionToOtherStateFromStateToRemove, transitionToStateToRemove)
                                        transitionsPendingToAdd.add(newTransition)
                                        stateSource.Transitions.remove(transitionToStateToRemove)
                                    }
                        }
                transitionsPendingToAdd.forEach { stateSource.Transitions.add(it)}
            }
        }
        states.remove(stateToRemove)
    }

    private fun getNewTransition(stateSource: RegexState, stateToRemove: RegexState,
                                 transitionToOtherStateFromStateToRemove: Transition,
                                 transitionToStateToRemove: Transition):Transition {
        val newTransition = Transition()
        newTransition.DestinyState.Name = transitionToOtherStateFromStateToRemove.DestinyState.Name
        newTransition.Symbol = transitionToStateToRemove.Symbol
        newTransition.Symbol += if (stateToRemove.hasTransitionToItself())
            "(" + stateToRemove.Transitions.filter { it.DestinyState.Name.equals(stateToRemove.Name) }
                    .elementAt(0).Symbol + ")*" else ""
        newTransition.Symbol += transitionToOtherStateFromStateToRemove.Symbol
        return newTransition
    }

    private fun updatePointToMeStates(states: ArrayList<RegexState>){
        for(it in states) {
            if(it.Name.equals("S")||it.Name.equals("F"))
                continue
            it.statesPointingToMe.clear()
            for(x in 0..(states.size-1)){
                if(states[x].Name.equals(it.Name)||(states[x].Name.equals("S")||states[x].Name.equals("F")))
                    continue
                for(iTrans in states[x].Transitions){
                    if(iTrans.DestinyState.Name.equals(it.Name)&&!iTrans.Symbol.equals(it.Name))
                        it.statesPointingToMe.add(states[x].Name!!)
                }
            }
        }
    }

    private fun removeTransitionsThatPointingToMyself(state: RegexState){
        val trans = Transition()
        trans.DestinyState.Name = state.Name
        trans.Symbol = ""
        state.Transitions.filter { it.DestinyState.Name.equals(state.Name) }.forEach {
            trans.Symbol += it.Symbol + "+"
            state.Transitions.remove(it)
        }
        trans.Symbol = trans.Symbol.toString().substring(0,(trans.Symbol.toString().length-1))
        state.Transitions.add(trans)
    }

    private fun getRegex(states: ArrayList<RegexState>, initialState: RegexState):String{
        while(states.size > 3){
            var positionToRemove = 2
            if(ignoreState(states.elementAt(positionToRemove),initialState))
                positionToRemove = 3
            removeStateByLessTarget(states.elementAt(positionToRemove),states)
            updateTargetCountForEachState(states)
            sortByLessTarget(states)
            updatePointToMeStates(states)
        }
        if(states.elementAt(2).hasTransitionToItself())
            removeTransitionsThatPointingToMyself(states.elementAt(2))
        val finalState =states.filter { !it.Name.equals("S")&&!it.Name.equals("F") }.elementAt(0)
        var regex = concatDifferentRegex(finalState)
        regex = deleteTheLastUFromRegex(regex)
        return regex.toString()
    }

    private fun concatDifferentRegex(finalState: RegexState): String {
        var regex1 = ""
        finalState.Transitions.filter { !it.Symbol.equals(EPSILON) }.forEach {
            transition ->
            regex1 += "["
            regex1 += if (transition.DestinyState.Name.equals(finalState.Name)) "(" + transition.Symbol + ")*" else transition.Symbol
            regex1 += "] . "
        }
        return regex1
    }

    private fun deleteTheLastUFromRegex(regex: String): String {
        var regex1 = regex
        if (regex1.elementAt(regex1.length - 2).equals('.')) {
            regex1 = regex1.substring(0, (regex1.length - 2))
            val regexSp = regex1.split(EPSILON)
            regex1 = ""
            regexSp.forEach {
                regex1 += it
            }
        }
        return regex1
    }

    private fun ignoreState(state:RegexState, initialState: RegexState):Boolean{
        return state.Name.equals(initialState.Name)||state.Name.equals("S")||state.Name.equals("F")
    }


}