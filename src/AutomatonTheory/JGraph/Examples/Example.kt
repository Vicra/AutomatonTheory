package AutomatonTheory.JGraph.Examples

import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph
import javax.swing.JFrame

class Example {

}

class HelloWorld2 : JFrame("Hello, World!") {

    init {

        val graph = mxGraph()
        val parent = graph.defaultParent

        graph.model.beginUpdate()
        try {
            val v1 = graph.insertVertex(parent, null, "Hello", 20.0, 20.0, 80.0,
                    30.0)
            val v2 = graph.insertVertex(parent, null, "World!", 240.0, 150.0,
                    80.0, 30.0)
            graph.insertEdge(parent, null, "Edge", v1, v2)

        } finally {
            graph.model.endUpdate()
        }

        val graphComponent = mxGraphComponent(graph)
        contentPane.add(graphComponent)
    }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            val frame = HelloWorld2()
            frame.defaultCloseOperation = EXIT_ON_CLOSE
            frame.setSize(400, 320)
            frame.isVisible = true
        }
    }
}

