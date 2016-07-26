package AutomatonTheory.JGraphKotlin.AutomatonDrawerComponents

import AutomatonTheory.Logic.DeterministicFiniteAutomaton
import AutomatonTheory.Logic.State
import AutomatonTheory.Logic.Transition
import com.mxgraph.model.mxCell
import com.mxgraph.swing.handler.mxKeyboardHandler
import com.mxgraph.swing.handler.mxRubberband
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.JInternalFrame

class AutomatonFrame : JInternalFrame("Automaton Grapher!") {
    var dfa = DeterministicFiniteAutomaton()
    var Nodes: MutableList<mxCell> = ArrayList()
    var Transitions: MutableList<mxCell> = ArrayList()

    private val circleRadius = 100

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
        mxKeyboardHandler(graphComponent)
        contentPane.add(graphComponent)

        graphComponent.graphControl.addMouseListener(object : MouseAdapter() {

            override fun mouseClicked(e: MouseEvent?) {
                val buttonType = e!!.button

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
                            node.style = "shape=ellipse"
                        } else if (style.contains("shape=ellipse")) {
                            node.style = "shape=doubleEllipse"
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

    private fun AddState(e: MouseEvent) {
        val nombre = nameForNewState
        if (dfa.addState(State(nombre, false, false))) {
            val v1 = graph.insertVertex(parent, null, nombre, (e.x - 25).toDouble(), (e.y - 25).toDouble(), circleRadius.toDouble(), circleRadius.toDouble(), "shape=ellipse;perimeter=ellipsePerimeter") as mxCell
            Nodes.add(v1)
        }
    }

    fun AddState(): Boolean {
        val nombre = nameForNewState

        if (nombre == "q0") {
            if (dfa.addState(State(nombre, true, false))) {
                val v1 = graph.insertVertex(parent, null, nombre, 450.0, 450.0, circleRadius.toDouble(), circleRadius.toDouble(), "shape=ellipse;perimeter=ellipsePerimeter") as mxCell
                Nodes.add(v1)
                return true
            }
        } else {
            if (dfa.addState(State(nombre, false, false))) {
                val v1 = graph.insertVertex(parent, null, nombre, 450.0, 450.0, circleRadius.toDouble(), circleRadius.toDouble(), "shape=ellipse;perimeter=ellipsePerimeter") as mxCell
                Nodes.add(v1)
                return true
            }
        }

        return false
    }

    fun AddTransition(originStateName: String, destinyStateName: String, symbol: String): Boolean {
        val originState = dfa._automaton.getState(originStateName)
        val destinyState = dfa._automaton.getState(destinyStateName)
        if (originState.addTransition(Transition(destinyState, symbol))) {
            val nodeOrigin = getNode(originStateName)
            val nodeDestiny = getNode(destinyStateName)

            val trans: mxCell

            if (nodeOrigin.value.toString() == nodeDestiny.value.toString()) {
                trans = graph.insertEdge(parent, null, symbol, nodeOrigin, nodeDestiny) as mxCell
            } else {
                trans = graph.insertEdge(parent, null, symbol, nodeOrigin, nodeDestiny, "edgeStyle=elbowEdgeStyle;elbow=horizontal;orthogonal=0.5;" + "entryX=0.5;entryY=0;entryPerimeter=1;rounded=true;arcsize=12") as mxCell
            }

            Transitions.add(trans)
            return false
        } else {
            return false
        }
    }

    fun setAcceptanceState(stateName: String): Boolean {
        return dfa._automaton.setAcceptanceState(stateName)
    }

    fun toggleAcceptanceState(stateName: String): Boolean {
        return dfa._automaton.toggleAcceptanceState(stateName)
    }

    fun EvaluateAutomaton(evaluateString: String): Boolean {
        return dfa.evaluateString(evaluateString)
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