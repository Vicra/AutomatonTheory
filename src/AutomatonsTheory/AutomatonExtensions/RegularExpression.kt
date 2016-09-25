package AutomatonsTheory.AutomatonExtensions

import AutomatonsTheory.AutomatonExtensions.RegularExpressionParserDirectory.RegularExpressionParser
import AutomatonsTheory.AutomatonExtensions.RegularExpressionParserDirectory.tree.*
import AutomatonsTheory.AutomatonLogic.State
import AutomatonsTheory.AutomatonLogic.Transition

class RegularExpression {
    var EPSILON = "e"
    var currentStateIndex = 0

    open fun regexToNfae(regex: String):NonDeterministicFiniteEpsilonAutomaton {
        var rootNode: Node = RegularExpressionParser().Parse(regex)
        return generateNfae(rootNode)
    }
    private fun generateNfae(node:Node):NonDeterministicFiniteEpsilonAutomaton{
        when(node){
            is CharNode -> {
                val nfae = NonDeterministicFiniteEpsilonAutomaton("")
                val initial: State = State("q"+(currentStateIndex++), true, false)
                val final: State = State("q"+(currentStateIndex++), false, true)
                nfae.States.add(initial)
                nfae.States.add(final)
                initial.Transitions.add(Transition(final, node.value))

                for(state in nfae.States){
                    for(transition in state.Transitions){
                        if(!nfae.Alphabet.contains(transition.Symbol)){
                            nfae.Alphabet.add(transition.Symbol)
                        }
                    }
                }
                return nfae
            }
            is ANDNode -> {
                var leftNode = generateNfae(node.leftNode)
                var rightNode = generateNfae(node.rightNode)
                var leftAcceptance = leftNode.getAcceptanceState()
                var rightInitial = rightNode.getInitialState()!!
                leftAcceptance!!.AcceptanceState = false
                rightInitial.InitialState = false
                leftAcceptance!!.addTransition(Transition(rightInitial, EPSILON))
                rightNode.Alphabet.forEach { symbol -> leftNode.Alphabet.add(symbol) }
                leftNode.Alphabet = leftNode.Alphabet.distinct().toMutableList()
                rightNode.States.forEach { state -> leftNode.States.add(state) }
                return leftNode
            }
            is ORNode -> {
                var leftNode = generateNfae(node.leftNode)
                var rightNode = generateNfae(node.rightNode)

                var leftAcceptanceState=leftNode.getAcceptanceState()
                var rightAcceptanceState=rightNode.getAcceptanceState()
                var leftInitial = leftNode.getInitialState()
                var rightInitial = rightNode.getInitialState()
                leftInitial!!.InitialState = false
                rightInitial!!.InitialState = false
                leftAcceptanceState!!.AcceptanceState = false
                rightAcceptanceState!!.AcceptanceState = false

                val initial: State = State("q"+(currentStateIndex++), true, false)
                val final: State = State("q"+(currentStateIndex++), false, true)

                initial.addTransition(Transition(leftInitial, EPSILON))
                initial.addTransition(Transition(rightInitial, EPSILON))
                leftAcceptanceState.addTransition(Transition(final, EPSILON))
                rightAcceptanceState.addTransition(Transition(final, EPSILON))

                leftNode.States.add(initial)
                leftNode.States.add(final)

                for (state in rightNode.States){
                    leftNode.States.add(state)
                }

                rightNode.Alphabet.forEach { symbol -> leftNode.Alphabet.add(symbol) }
                leftNode.Alphabet = leftNode.Alphabet.distinct().toMutableList()
                return leftNode
            }
            is RepeatNode -> {
                var returnNode = generateNfae(node.node)
                var initialState = returnNode.getInitialState()!!
                var finalState = returnNode.getAcceptanceState()!!

                initialState.InitialState = false
                finalState.AcceptanceState = false

                val newInitial: State = State("q"+(currentStateIndex++), true, false)
                val newFinal: State = State("q"+(currentStateIndex++), false, true)

                returnNode.States.add(newInitial)
                returnNode.States.add(newFinal)

                returnNode.addTransition(newInitial.Name, initialState.Name, EPSILON)
                returnNode.addTransition(newInitial.Name, newFinal.Name,EPSILON)

                returnNode.addTransition(finalState.Name, initialState.Name, EPSILON)
                returnNode.addTransition(finalState.Name, newFinal.Name, EPSILON)

                return returnNode
            }
            else -> {
                throw Exception("not implemented")
            }
        }
    }
}