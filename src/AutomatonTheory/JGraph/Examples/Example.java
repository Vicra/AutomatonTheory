package AutomatonTheory.JGraph.Examples;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

//public class Example2 extends JFrame
//{
//
//    /**
//     *
//     */
//    private static final long serialVersionUID = -2707712944901661771L;
//
//    @SuppressWarnings("unused")
//    public Example2()
//    {
//        super("Hello, World!");
//
//        mxGraph graph = new mxGraph();
//        Object parent = graph.getDefaultParent();
//
//        graph.getModel().beginUpdate();
//        try
//        {
//            Object v1 = graph.insertVertex(parent, null, "Hello,", 20, 20, 80,
//                    60, "shape=triangle;perimeter=trianglePerimeter");
//            Object v2 = graph.insertVertex(parent, null, "World!", 200, 150,
//                    80, 60, "shape=ellipse;perimeter=ellipsePerimeter");
//            Object v3 = graph.insertVertex(parent, null, "Hello,", 200, 20, 80,
//                    30);
//            Object e1 = graph
//                    .insertEdge(
//                            parent,
//                            null,
//                            "",
//                            v1,
//                            v2,
//                            "edgeStyle=elbowEdgeStyle;elbow=horizontal;"
//                                    + "exitX=0.5;exitY=1;exitPerimeter=1;entryX=0;entryY=0;entryPerimeter=1;");
//            Object e2 = graph.insertEdge(parent, null, "", v3, v2,
//                    "edgeStyle=elbowEdgeStyle;elbow=horizontal;orthogonal=0;"
//                            + "entryX=0;entryY=0;entryPerimeter=1;");
//        }
//        finally
//        {
//            graph.getModel().endUpdate();
//        }
//
//        mxGraphComponent graphComponent = new mxGraphComponent(graph);
//        getContentPane().add(graphComponent);
//    }
//
//    public static void main(String[] args)
//    {
//        Example2 frame = new Example2();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 320);
//        frame.setVisible(true);
//    }
//
//}
