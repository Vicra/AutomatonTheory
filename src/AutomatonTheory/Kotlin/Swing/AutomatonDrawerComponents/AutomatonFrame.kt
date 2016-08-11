package AutomatonTheory.Kotlin.Swing.AutomatonDrawerComponents

import AutomatonTheory.Kotlin.AutomatonLogic.Automaton
import AutomatonTheory.Kotlin.AutomatonLogic.State
import com.mxgraph.model.mxCell
import com.mxgraph.swing.handler.mxRubberband
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxConstants
import com.mxgraph.view.mxGraph
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.Serializable
import java.util.*
import javax.swing.JInternalFrame

class AutomatonFrame(automaton:Automaton) : JInternalFrame("Automaton Grapher!") , Serializable {

    var automaton: Automaton = automaton
    var Nodes: MutableList<mxCell> = ArrayList()
    var Transitions: MutableList<mxCell> = ArrayList()

    private val circleRadius = 75

    internal val graph = mxGraph()
    internal var parent = graph.defaultParent


    init {

        graph.model.beginUpdate()
        graph.model.endUpdate()

        val graphComponent = mxGraphComponent(graph)
        graph.isMultigraph = false
        graph.isAllowDanglingEdges = false
        graphComponent.isConnectable = true
        graphComponent.setToolTips(true)

        // Enables rubberband selection
        mxRubberband(graphComponent)
        contentPane.add(graphComponent)

        graphComponent.graphControl.addMouseListener(object : MouseAdapter() {

            override fun mouseClicked(e: MouseEvent) {
                val buttonType = e.button

                if (buttonType == 1) {
                    if (e.clickCount == 2) {
                        val cell = graphComponent.getCellAt(e.x, e.y)
                        if (cell != null) {


                        } else {
                            AddState(e)
                        }
                    }
                }
                if (buttonType == 3) {
                    val cell = graphComponent.getCellAt(e.x, e.y)

                    if (cell != null) {
                        val node = cell as mxCell
                        val style = node.style

                        if (style.contains("shape=doubleEllipse")) {
                            graph.setCellStyles(mxConstants.STYLE_SHAPE, "ellipse", arrayOf<Any>(node))
                        } else if (style.contains("shape=ellipse")) {
                            graph.setCellStyles(mxConstants.STYLE_SHAPE, "doubleEllipse", arrayOf<Any>(node))
                        }

                        graphComponent.refresh()
                        if (toggleAcceptanceState(graph.getLabel(cell))) {
                            println("es aceptado" + graph.getLabel(cell))
                        }
                    }
                }
            }
        })
    }

    fun AddState(e: MouseEvent) {
        val nombre = nameForNewState
        if (automaton.addState(State(nombre, false, false))) {
            val v1 = graph.insertVertex(parent, null, nombre, (e.x - 25).toDouble(), (e.y - 25).toDouble(), circleRadius.toDouble(), circleRadius.toDouble(), "shape=ellipse;perimeter=ellipsePerimeter") as mxCell
            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#81BEF7", arrayOf<Any>(v1))
            Nodes.add(v1)
        }

    }

    fun AddState(stateName: String, initialState:Boolean, acceptanceState:Boolean, x:Double, y:Double): Boolean {
        val nombre = stateName
        automaton.addState(State(nombre, initialState, acceptanceState))
        val v1 = graph.insertVertex(parent, null, nombre, x, y, circleRadius.toDouble(), circleRadius.toDouble(), "shape=ellipse;perimeter=ellipsePerimeter;editable=0") as mxCell
        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#81BEF7", arrayOf<Any>(v1))
        Nodes.add(v1)
        if(initialState){
            SetInitialState(stateName)
        }
        if(acceptanceState){
            var node = getNode(stateName)
            graph.setCellStyles(mxConstants.STYLE_SHAPE, "doubleEllipse", arrayOf<Any>(node))
        }
        return true
    }

    fun AddState(): Boolean {
        val nombre = nameForNewState
        if (automaton.addState(State(nombre, false, false))) {
            val v1 = graph.insertVertex(parent, null, nombre, 450.0, 450.0, circleRadius.toDouble(), circleRadius.toDouble(), "shape=ellipse;perimeter=ellipsePerimeter;editable=0") as mxCell
            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#81BEF7", arrayOf<Any>(v1))
            Nodes.add(v1)
            return true
        }
        return false
    }

    fun AddTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        if(automaton.addTransition(originStateName,destinyStateName,symbol)){
            val nodeOrigin = getNode(originStateName)
            val nodeDestiny = getNode(destinyStateName)
            val trans: mxCell

            var hasTwoPaths: Boolean = false
            for (transition in Transitions) {
                if (transition.source.value.toString() == nodeOrigin.value.toString() && transition.target.value.toString() == nodeDestiny.value.toString()) {
                    //get transition and change the value shown
                    hasTwoPaths = true
                    val currentValue = transition.value.toString()
                    transition.value = currentValue + " / " + symbol
                    graph.refresh()
                }
            }
            if ((!hasTwoPaths)) {
                if (nodeOrigin.value.toString() == nodeDestiny.value.toString()) {
                    trans = graph.insertEdge(parent, null, symbol, nodeOrigin, nodeDestiny) as mxCell
                } else {
                    trans = graph.insertEdge(parent, null, symbol, nodeOrigin, nodeDestiny, "edgeStyle=elbowEdgeStyle;elbow=horizontal;orthogonal=0.5;" + "entryX=0.5;entryY=0;entryPerimeter=1;rounded=true;arcsize=12") as mxCell
                }
                Transitions.add(trans)
            }
            return true;
        }
        else{
            return false;
        }
    }

    fun RemoveTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        if(automaton.removeTransition(originStateName, destinyStateName, symbol)){
            for(item in Transitions){
                if(item.source.value.toString() == originStateName && item.target.value.toString() == destinyStateName && item.value.toString() == symbol){
                    graph.removeCells(arrayOf<Any>(item))
                    Transitions.remove(item)
                    return true
                }
            }
        }
        return false
    }

    fun RemoveState(stateName: String): Boolean {
        if (automaton.removeState(stateName)) {
            val node = getNode(stateName)

            //remove transitions
            for (item in Transitions) {
                if (item.source.value.toString() == stateName || item.target.value.toString() == stateName) {
                    graph.removeCells(arrayOf<Any>(item))
                    Transitions.remove(item)
                }
            }

            //remove state
            graph.removeCells(arrayOf<Any>(node))
            Nodes.remove(node)

            return true
        }
        return false
    }

    fun EvaluateAutomaton(evaluationString :String) : Boolean{
        println("llegue")
        return automaton.evaluateString(evaluationString);
    }

    fun SetInitialState(stateName: String): Boolean {
        if (automaton.setInitialState(stateName)) {
            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#81BEF7")
            for (node in Nodes) {
                if (node.value.toString() == stateName) {
                    graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#9FF781", arrayOf<Any>(node))
                } else {
                    graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#81BEF7", arrayOf<Any>(node))
                }
            }
            return true
        }
        return false
    }

    fun toggleAcceptanceState(stateName: String): Boolean {
        return automaton.toggleAcceptanceState(stateName)
    }

    fun getNode(nodeName: String): mxCell {
        for (item in Nodes) {
            if (item.value == nodeName) {
                return item
            }
        }
        return mxCell()
    }


    val nameForNewState: String
        get() = "q" + Nodes.size
}