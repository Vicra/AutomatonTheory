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

    fun toDeterministicFiniteAutomaton(): DeterministicFiniteAutomaton{
        var automata2 = DeterministicFiniteAutomaton(this.AutomatonName)
        val listaDeAceptados: MutableList<String> = mutableListOf()
        for (t in States.indices){
            if(States[t].AcceptanceState){
                listaDeAceptados.add(States[t].Name)
            }
        }
        var nuevosVertex: MutableList<State> = mutableListOf()
        val initVertex = getInitialState()
        nuevosVertex.add(State(initVertex.Name,true,initVertex.AcceptanceState))
        var contadorIndices = 0
        val mapaIndices: MutableMap<String,Int> = mutableMapOf()
        mapaIndices.put(initVertex.Name,contadorIndices)
        contadorIndices++

        automata2.Alphabet = this.Alphabet
        automata2.States.add(State(initVertex.Name,true,initVertex.AcceptanceState))
        while(nuevosVertex.count() > 0){
            val vertexActual = nuevosVertex[0]
            val mapaNombres: MutableMap<String,String> = mutableMapOf()
            for (p in automata2.Alphabet.indices){
                val separados = vertexActual.Name.split(",")
                for (w in separados.indices){
                    val subVertexActual = getState(separados[w])
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