package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons
import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition
import java.util.*

open class NonDeterministicFiniteEpsilonAutomaton (automatonName:String) : Automaton() {
    var evaluatedStates:MutableList<String> = ArrayList()
    var epsilonClosure:MutableList<State> = ArrayList()
    var clausuras: MutableMap<String,MutableList<String>> = mutableMapOf()

    override fun addTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        val originState = getState(originStateName)
        val destinyState = getState(destinyStateName)
        return originState.addTransition(Transition(destinyState, symbol))
    }

    init{
        AutomatonName = automatonName
        Type = Automatons.NFAe
    }

    constructor(automatonName: String, alphabet: MutableList<String>) : this(automatonName) {
        setAlphabet(alphabet)
        Alphabet.add("e")
    }

    override fun evaluateString(stringEvaluate:String):Boolean{
        return toDeterministicFiniteAutomaton().evaluateString(stringEvaluate)
    }

    open fun getReachableStates(closureStates:MutableList<State>, alphabetItem:String):MutableList<State>{
        var  reachableStates:MutableList<State> = mutableListOf()
        for(state in closureStates){
            for (transition in state.Transitions){
                if(transition.Symbol.equals(alphabetItem)){
                    reachableStates.add(transition.DestinyState)
                }
            }
        }
        closureStates.clear()
        evaluatedStates.clear()
        return reachableStates
    }

    open fun getAcceptanceState() : State?{
        for(state in States){
            if(state.AcceptanceState){
                return state
            }
        }
        return null
    }

    open fun toDeterministicFiniteAutomaton():DeterministicFiniteAutomaton{
        var returnDfa:DeterministicFiniteAutomaton = DeterministicFiniteAutomaton("nfae2dfa")
        generarClausuras()
        returnDfa.Alphabet = Alphabet
        var clausuraVertexInicial = getClausura((getInitialState()))
        returnDfa.addState(State(clausuraVertexInicial, true, contieneAceptado(clausuraVertexInicial)))
        var listaPop = mutableListOf<String>()
        listaPop.add(clausuraVertexInicial)
        while (listaPop.count() > 0) {
            var vertexActual = returnDfa.getState(listaPop.first())
            for (valor in returnDfa.Alphabet) {
                var nuevaSalida = getSalida(valor, vertexActual)
                if (nuevaSalida != "") {
                    var destinoName = yaExisteString(returnDfa, nuevaSalida)
                    var nuevoVertex = State(destinoName,false,contieneAceptado(destinoName))
                    if(returnDfa.addState(nuevoVertex)){
                        listaPop.add(destinoName)
                    }
                    returnDfa.addTransition(vertexActual.Name, nuevoVertex.Name, valor)
                }
            }
            listaPop.removeAt(0)
        }
        var numeroEstado = 0
        for(vertex in returnDfa.States){
            returnDfa.getState(vertex.Name).Name = "q"+numeroEstado
            numeroEstado++
        }

        return returnDfa
    }
    open fun getClausura(vertex:State):String{
        var clausura = ""
        var lista = mutableListOf<String>()
        for(elem in clausuras){
            if(elem.key == vertex.Name){
                for(valor in elem.value){
                    lista.add(valor)
                    lista.add(",")
                }
            }
        }
        lista.removeAt(lista.count()-1)
        for(elem in lista){
            clausura += elem.toString()
        }
        return clausura
    }

    open fun generarClausuras(){
        clausuras.clear()
        for(vertex in States){
            var lista = mutableListOf<String>()
            lista.add(vertex.Name)
            clausuras.put(vertex.Name,lista)
        }
        for(elem in clausuras){
            var vertexActual = getState(elem.key) as State
            for(transicion in vertexActual.Transitions){
                if(transicion.Symbol == 'e'.toString() && !(elem.value.contains(transicion.DestinyState.Name))){
                    elem.value.add(transicion.DestinyState.Name)
                }
            }
        }
        for(elem in clausuras){
            var listaPop = mutableListOf<String>()
            for(str in elem.value){
                listaPop.add(str)
            }
            listaPop.remove(elem.key)
            while(listaPop.isNotEmpty()){
                var vertexActual = getState(listaPop.first()) as State
                for(transicion in vertexActual.Transitions){
                    if(transicion.Symbol == 'e'.toString() && !(elem.value.contains(transicion.DestinyState.Name))){
                        listaPop.add(transicion.DestinyState.Name)
                        elem.value.add(transicion.DestinyState.Name)
                    }
                }
                listaPop.removeAt(0)
            }
        }
    }

    open fun contieneAceptado(estado:String):Boolean{
        var separado = estado.split(",")
        for(elem in separado){
            var vertexActual = getState(elem) as State
            if(vertexActual.AcceptanceState){
                return true
            }
        }
        return false
    }

    open fun getSalida(valor:String, estado:State):String{
        var lista = mutableListOf<String>()
        var strSplit = estado.Name.split(",")
        for(elem in strSplit){
            var vertexActual = getState(elem) as State
            for(transicion in vertexActual.Transitions){
                if(transicion.Symbol == valor.toString()){
                    var clausuraActual = getClausura(getState(transicion.DestinyState.Name) as State)
                    var splitClausura = clausuraActual.split(",")
                    for(elem2 in splitClausura){
                        if(!(lista.contains(elem2))){
                            lista.add(elem2)
                            lista.add(",")
                        }
                    }
                }
            }
        }
        var returnString = ""
        if(lista.isNotEmpty()){
            lista.removeAt(lista.count()-1)
        }
        for(elem in lista){
            returnString += elem.toString()
        }
        return returnString
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
}