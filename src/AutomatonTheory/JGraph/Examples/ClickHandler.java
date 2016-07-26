package AutomatonTheory.JGraph.Examples;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickHandler extends JInternalFrame implements KeyListener
{
    public void AddTransition(){

    }
    @Override
    public void keyTyped(KeyEvent e)
    {
        System.out.println("keytype key:" + e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        System.out.println("pressed key:" + e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        System.out.print("released key:" + e.getKeyChar());
    }

    public ClickHandler() {

        super("Hello, World!");

        final mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try {
//		   Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80, 30);
//		   Object v2 = graph.insertVertex(parent, null, "World!", 240, 150, 80, 30);
//		   graph.insertEdge(parent, null, "Edge", v1, v2);
        } finally {
            graph.getModel().endUpdate();
        }

        final mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);

        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println(e.getX());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int buttonType = e.getButton();

                if (buttonType == 1) {
                    if (e.getClickCount() == 2) { // si hay doble click
                        Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                        if (cell != null) {
                            System.out.println("cell=" + graph.getLabel(cell)); // lo hago estado aceptacion o
                        } else {
                            Object v1 = graph.insertVertex(parent, null, "Nuevo", e.getX() - 25, e.getY() - 25, 50, 50,
                                    "shape=ellipse;perimeter=ellipsePerimeter"); // creo uno nuevo
                        }
                    }
                }
                if (buttonType == 3) {
                    System.out.println("click: " + buttonType);
                    Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                    if (cell != null) {

                    }
                }
            }


        });

    }


	public static void main(String[] args)
	{
		ClickHandler frame = new ClickHandler();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);
		frame.setVisible(true);



        JFrame jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(frame);
        jframe.add(new JInternalFrame());
        jframe.setSize(800, 700);
        jframe.setVisible(true);

        frame.setFocusable(true);


	}


}