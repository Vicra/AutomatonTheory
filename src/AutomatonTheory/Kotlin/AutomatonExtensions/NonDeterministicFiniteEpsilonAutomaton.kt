package AutomatonTheory.Kotlin.AutomatonExtensions

import AutomatonTheory.Kotlin.AutomatonLogic.State

open class NonDeterministicFiniteEpsilonAutomaton (automatonName:String) : NonDeterministicFiniteAutomaton(automatonName) {

    var estadosEncontrados:MutableList<State> = mutableListOf<State>()

    init{
        Alphabet.add("e")
    }

    constructor(automatonName: String, alphabet: MutableList<String>) : this(automatonName) {
        setAlphabet(alphabet)
    }

    fun recursion(currentState:State, cadena:String){
        var elementos:CharArray = cadena.toCharArray()
        var clausuraStates:MutableList<State> = getClausura(currentState)

        for(state in clausuraStates){
            for(transition in state.Transitions){
                if(elementos.size == 1){
                    estadosEncontrados.add(transition.DestinyState)
                }
                if(transition.Symbol == elementos[0].toString()){
                    //quitarle el primer elemento
                    var substring = cadena.substring(1,cadena.length)
                    recursion(state, substring)
                }
            }
        }
        println("yes")
    }

    fun getClausura(currentState:State) : MutableList<State> {
        var clausuraStates:MutableList<State> = mutableListOf()
        clausuraStates.add(currentState)
        for(state in States){
            for(transition in state.Transitions){
                if(transition.Symbol == "e"){
                    clausuraStates.add(transition.DestinyState)
                }
            }
        }
        return clausuraStates
    }
}