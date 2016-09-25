package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonLogic.State

class ContextFreeGrammar(map:Map<String, String>) {
    var productionMap: MutableMap<Char,MutableList<String>> = mutableMapOf()
    var simboloInicial = '0'

    fun toPushDownAutomaton() : PushDownAutomaton{
        var returnPDA = PushDownAutomaton()
        val q0:State = State("q0", true, false)
        val q1:State = State("q1", false, false)
        val q2:State = State("q1", false, true)

        returnPDA.addState(q0)
        returnPDA.addState(q1)
        returnPDA.addState(q2)

        returnPDA.addTransition("q0", "q1", "e,Zo/eZo")
        returnPDA.addTransition("q1", "q2", "e,Zo/Zo")

        return returnPDA
    }

    open fun addBranch(key:Char, production:String){
        var strLista = mutableListOf<String>()
        strLista.add(production)
        if(productionMap.containsKey(key)){
            var strLista2 = productionMap.get(key) as MutableList<String>
            for(elem in strLista2){
                strLista.add(elem)
            }
            productionMap.remove(key)
            productionMap.put(key,strLista)
        }else{
            productionMap.put(key,strLista)
        }
    }

    open fun fillTerminales():MutableList<String>{
        var Terminales = mutableListOf<String>()
        for(elem in productionMap){
            var miLista = elem.value
            for(elem2 in miLista){
                var misChars = elem2.toCharArray().toMutableList()
                for(charact in misChars){
                    if(!Terminales.contains(charact.toString()) && !productionMap.containsKey(charact)){
                        Terminales.add(charact.toString())
                    }
                }
            }
        }
        return Terminales
    }

    open fun transformarPDA():PushDownAutomaton{
        var Terminales = fillTerminales()
        var automataPDA = PushDownAutomaton()
        automataPDA.Alphabet = Terminales
        automataPDA.addState(State("q0",true,false))
        automataPDA.addState(State("q1",false,false))
        automataPDA.addState(State("q2",false,false))
        var miStr = simboloInicial + "Z"
        //automataPDA.States[0].addTransition('E','Z',miStr,"q1")
        for(elem in productionMap){
            var miStrLista = elem.value
            for(str in miStrLista){
                //automataPDA.States[1].addTransition('E',elem.key,str,"q1")
            }
        }
        for(elem in Terminales){
            //automataPDA.States[1].addTransition(elem,elem,"","q1")
        }
        //automataPDA.States[1].addTransition('E','Z',"","q2")
        return automataPDA
    }
}