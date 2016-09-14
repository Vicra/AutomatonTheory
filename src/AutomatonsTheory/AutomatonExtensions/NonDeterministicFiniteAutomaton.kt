package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons
import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition
import java.util.*

open class NonDeterministicFiniteAutomaton() : Automaton() {

    init {
        Type = Automatons.NFA
    }

    constructor(automatonName: String, alphabet: MutableList<String>) : this(automatonName) {
        setAlphabet(alphabet)
    }

    constructor(automatonName: String) : this(){
        AutomatonName = automatonName
    }

    override fun evaluateString(stringEvaluate: String): Boolean {
        val characters = stringEvaluate.toCharArray()
        var currentState = getInitialState()
        var currentStates = mutableListOf<State>()
        currentStates.add(currentState)

        for (character in characters) {
            val currentStatesFiltered = mutableListOf<State>()
            for (currState in currentStates)
            {
                for (transition in currState.Transitions) {
                    if (character.toString() == transition.Symbol)
                        currentStatesFiltered.add(getState(transition.DestinyState.Name))
                }
            }
            currentStates.clear()
            for(newCurrentState in currentStatesFiltered){
                currentStates.add(newCurrentState)
            }
        }
        if(stringEvaluate.isEmpty()){
            val currentStatesFiltered = mutableListOf<State>()
            for (currState in currentStates)
            {
                for (transition in currState.Transitions) {
                    if ("e" == transition.Symbol)
                        currentStatesFiltered.add(getState(transition.DestinyState.Name))
                }
            }
            currentStates.clear()
            for(newCurrentState in currentStatesFiltered){
                currentStates.add(newCurrentState)
            }
        }
        for (state in currentStates)
        {
            if (state.AcceptanceState)
            {
                return true
            }
        }
        return false
    }

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        val originState = getState(originStateName)
        val destinyState = getState(destinyStateName)
        return originState.addTransition(Transition(destinyState, symbol))
    }

    fun toDFA(): DeterministicFiniteAutomaton{
        var returnDFA = DeterministicFiniteAutomaton(this.AutomatonName)
        returnDFA.Alphabet = this.Alphabet
        var matrizPrincipal: Array2D<String> = Array2D<String>(States.size, Alphabet.size) as Array2D<String>

        //llenar la matriz
        for(stateIndex in States.indices){
            for(symbolIndex in Alphabet.indices){
                var state:State = getState(stateIndex)
                var destinyStates = state.getDestinyStates(Alphabet.get(symbolIndex))
                var destinyStatesName = getNewName(destinyStates)
                matrizPrincipal.set(stateIndex, symbolIndex,destinyStatesName)
            }
        }

        //copiar estados del nfa al dfa
        for(state in States){
            returnDFA.States.add(State(state.Name, state.InitialState, state.AcceptanceState))
        }

        //meter los nuevos estados al dfa
        for(stateIndex in States.indices){
            for(symbolIndex in Alphabet.indices){
                var stateName:String = matrizPrincipal.get(stateIndex, symbolIndex)
                var stateSplit = stateName.split(",")
                if(stateName.isNotEmpty() && stateSplit.size > 1){
                    returnDFA.States.add(State(stateName, false, false))
                }
            }
        }

        //estado incial
        var initialState:State = getInitialState()
        for(state in returnDFA.States){
            if(state.Name.equals(initialState.Name)){
                state.InitialState = true
            }
        }

        //estados finales
        for(state in States){
            for(dfaState in returnDFA.States){
                if(state.AcceptanceState && dfaState.Name.contains(state.Name)){
                    dfaState.AcceptanceState = true
                }
            }
        }

        //meter transiciones copiar de matriz
        for(stateIndex in States.indices){
            for(symbolIndex in Alphabet.indices){
                var currentState = returnDFA.getState(stateIndex)
                var destinyStateName = matrizPrincipal.get(stateIndex, symbolIndex)
                if(destinyStateName.isNotEmpty()){
                    currentState.Transitions.add(Transition(returnDFA.getState(destinyStateName),returnDFA.Alphabet[symbolIndex]))
                }
            }
        }

        //faltan las transiciones de los nuevos estados

        var currentSize:Int = returnDFA.States.size
        var sizeChanged:Boolean = true

        while(sizeChanged){

            for(state in returnDFA.States){
                if(state.Name.contains(",")){
                    var stateNames = state.Name.split(",")
                    for(symbol in returnDFA.Alphabet){

                        var newDestinyName:String = ""
                        for(stateName in stateNames){
                            var myState:State = returnDFA.getState(stateName)
                            var destinyState = myState.getDestinyState(symbol).Name.split(",")//esto puede ser una combinacion de estados ie: q0, q1

                            for(dest in destinyState){
                                if(!newDestinyName.contains(dest)){
                                    if(newDestinyName.isEmpty()){
                                        newDestinyName += dest
                                    }
                                    else{
                                        newDestinyName += "," + dest
                                    }
                                }
                            }
                        }
                        if(!returnDFA.existsState(newDestinyName)){
                            returnDFA.addState(State(newDestinyName, false, false))
                        }
                        if(newDestinyName.isNotEmpty()){
                            returnDFA.addTransition(state.Name, newDestinyName, symbol)
                        }
                    }
                }
            }

            var newSize = returnDFA.States.size
            if(newSize > currentSize){
                sizeChanged = true
                currentSize = newSize
            }
            else if(newSize == currentSize){
                sizeChanged = false
            }
        }
        //remover lo estados basuras
        var toRemoveStates:MutableList<State> = mutableListOf()
        var removeStates:MutableList<State> = mutableListOf()
        for(state in returnDFA.States){
            if(state.Transitions.size == 0){
                removeStates.add(state)
            }
        }
        for(removeState in removeStates){
            var hasAnyTransition:Boolean = false
            for(originState in returnDFA.States){
                for(transition in originState.Transitions){
                    if(transition.DestinyState.Name.equals(removeState.Name)){
                        hasAnyTransition = true
                    }
                }
            }
            if(!hasAnyTransition){
                toRemoveStates.add(removeState)
            }
        }
        returnDFA.States.removeAll(toRemoveStates)
        return returnDFA
    }

    fun toDeterministicFiniteAutomaton(): DeterministicFiniteAutomaton{
        var automata2 = DeterministicFiniteAutomaton(this.AutomatonName)
        val listaDeAceptados: MutableList<String> = mutableListOf() //Para saber cuando un nuevo estado es de aceptacion
        for (t in States.indices){
            if(States[t].AcceptanceState){
                listaDeAceptados.add(States[t].Name)
            }
        }
        var nuevosVertex: MutableList<State> = mutableListOf() //Para ir haciendo pop
        val initVertex = getInitialState() as State //Tomar estado inicial
        nuevosVertex.add(State(initVertex.Name,true,initVertex.AcceptanceState)) //Agregar estado inicial
        var contadorIndices = 0
        val mapaIndices: MutableMap<String,Int> = mutableMapOf()
        mapaIndices.put(initVertex.Name,contadorIndices)
        contadorIndices++

        automata2.Alphabet = this.Alphabet //Extraer el alfabeto del automata AFN
        automata2.States.add(State(initVertex.Name,true,initVertex.AcceptanceState))
        while(nuevosVertex.count() > 0){
            val vertexActual = nuevosVertex[0]
            val mapaNombres: MutableMap<String,String> = mutableMapOf() //Valores con sus nodos
            for (p in automata2.Alphabet.indices){
                val separados = vertexActual.Name.split(",")
                for (w in separados.indices){
                    val subVertexActual = getState(separados[w]) as State
                    for (o in subVertexActual.Transitions.indices){
                        if(subVertexActual.Transitions[o].Symbol == automata2.Alphabet[p]){
                            if(mapaNombres.containsKey(automata2.Alphabet[p])){
                                val miStr = (mapaNombres.get(automata2.Alphabet[p])) as String
                                val cortado = miStr.split(",")
                                if(!cortado.contains(subVertexActual.Transitions[o].DestinyState.Name))
                                    mapaNombres.set(automata2.Alphabet[p],miStr + "," + subVertexActual.Transitions[o].DestinyState.Name)
                            }else{
                                mapaNombres.put(automata2.Alphabet[p], subVertexActual.Transitions[o].DestinyState.Name)
                            }
                        }
                    }
                }
            }
            val contador1 = mapaNombres.count()
            var contador2 = 0
            var misNombres = mapaNombres.entries
            while(contador1>contador2){
                val miStr = misNombres.elementAt(contador2).value
                val nombreV = yaExisteString(automata2,miStr)
                if(!yaExisteBoolean(automata2, miStr)){
                    val isAccept = esVertexAceptable(listaDeAceptados,nombreV)
                    mapaIndices.put(nombreV,contadorIndices)
                    contadorIndices++
                    automata2.States.add(State(nombreV,false,isAccept))
                    nuevosVertex.add(State(nombreV,false,isAccept))
                }
                var state:State? = automata2.getState(vertexActual.Name)
                state!!.Transitions.add(Transition(automata2.getState(nombreV), misNombres.elementAt(contador2).key.toString()))
                contador2++
            }
            nuevosVertex.removeAt(0)
        }
        return automata2
    }

    open fun yaExisteBoolean(miAFD:DeterministicFiniteAutomaton, miStr:String):Boolean{
        for (i in miAFD.States.indices){
            val strCompare = miAFD.States[i].Name
            val miArr = strCompare.split(",")
            val miArr2 = miStr.split(",")
            if(miArr.count() == miArr2.count()){
                val set1 = HashSet<String>()
                set1.addAll(miArr)
                val set2 = HashSet<String>()
                set2.addAll(miArr2)
                if(set1.equals(set2)){
                    return true
                }
            }
        }
        return false
    }

    open fun yaExisteString(miAFD:DeterministicFiniteAutomaton, miStr:String):String{
        for (i in miAFD.States.indices){
            val strCompare = miAFD.States[i].Name
            val miArr = strCompare.split(",")
            val miArr2 = miStr.split(",")
            if(miArr.count() == miArr2.count()){
                val set1 = HashSet<String>()
                set1.addAll(miArr)
                val set2 = HashSet<String>()
                set2.addAll(miArr2)
                if(set1.equals(set2)){
                    return strCompare
                }
            }
        }
        return miStr
    }

    open fun esVertexAceptable(misVertexAceptables:kotlin.collections.MutableList<String>, str:String): Boolean{
        val loQueComparo = str.split(",")
        for (i in misVertexAceptables.indices){
            if(loQueComparo.contains(misVertexAceptables[i])){
                return true
            }
        }
        return false
    }

    fun getNewName(destinyStates:MutableList<State>) : String{
        var returnString:String = ""
        if(destinyStates.size == 1){
            returnString = destinyStates[0].Name
        }
        else{
            for(destinyState in destinyStates){
                if(returnString.isEmpty()){
                    returnString += destinyState.Name
                }
                else{
                    returnString += "," + destinyState.Name
                }
            }
        }
        return returnString
    }
}