package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition

class ContextFreeGrammar() {

    var grammarMap: MutableMap<Char,MutableList<String>> = mutableMapOf()
    var simboloInicial = '0'

    open fun addBranch(indice:Char,producciones:String){
        var strLista = mutableListOf<String>()
        strLista.add(producciones)
        if(grammarMap.containsKey(indice)){
            var strLista2 = grammarMap.get(indice) as MutableList<String>
            for(elem in strLista2){
                strLista.add(elem)
            }
            grammarMap.remove(indice)
            grammarMap.put(indice,strLista)
        }else{
            grammarMap.put(indice,strLista)
        }
    }

    open fun setInicial(indice:Char){
        if(grammarMap.containsKey(indice)){
            simboloInicial = indice
        }
    }

    open fun fillNoTerminales():MutableList<Char>{
        var noTerminales = mutableListOf<Char>()
        for(elem in grammarMap){
            noTerminales.add(elem.key)
        }
        return noTerminales
    }

    open fun fillTerminales():MutableList<String>{
        var Terminales = mutableListOf<String>()
        for(elem in grammarMap){
            var miLista = elem.value
            for(elem2 in miLista){
                var misChars = elem2.toCharArray().toMutableList()
                for(charact in misChars){
                    if(!Terminales.contains(charact.toString()) && !grammarMap.containsKey(charact)){
                        Terminales.add(charact.toString())
                    }
                }
            }
        }
        return Terminales
    }

    open fun verifyEvaluar(evaluarEsto:String):Boolean{
        var miStrArray = evaluarEsto.toCharArray().toMutableList()
        for(elem in grammarMap){
            if(!miStrArray.contains(elem.key)){
                return false
            }
        }
        return true
    }

    open fun transformarPDA():PushDownAutomaton{
        setInicial('S')
        var Terminales = fillTerminales()
        var automataPDA = PushDownAutomaton()
        automataPDA.Alphabet = Terminales
        automataPDA.addState(State("q0",true,false))
        automataPDA.addState(State("q1",false,false))
        automataPDA.addState(State("q2",false,false))
        var miStr = simboloInicial + "Z"
        automataPDA.States[0].addTransition(Transition(automataPDA.getState("q1"),"(E,Z/$miStr)"))
        for(elem in grammarMap){
            var miStrLista = elem.value
            for(str in miStrLista){
                automataPDA.States[1].addTransition(Transition(automataPDA.getState("q1"),"(E,"+elem.key+"/"+str+")"))
            }
        }
        for(elem in Terminales){
            automataPDA.States[1].addTransition(Transition(automataPDA.getState("q1"),"($elem,$elem/E)"))
        }
        automataPDA.States[1].addTransition(Transition(automataPDA.getState("q2"),"(E,Z/E)"))
        return automataPDA
    }
}