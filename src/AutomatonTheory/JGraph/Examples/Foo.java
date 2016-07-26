package AutomatonTheory.JGraph.Examples;

import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;

public class Foo {
    public static boolean RIGHT_TO_LEFT = false;


    public static void addComponentsToPane(Container pane) {

        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }

        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(
                    java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }

        JButton button = new JButton("Add Transition");
        button.setSize(new Dimension(200, 100));
        button.setMaximumSize(new Dimension(200, 100));

        JPanel pn = new JPanel();

        pane.add(pn, BorderLayout.PAGE_START);


        pn = new JPanel(){
            mxGraph graph = new mxGraph();
            Object parent = graph.getDefaultParent();

            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,30);
            Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,80, 30);
        };

        //pn.add(v1);

        pane.add(pn, BorderLayout.CENTER);


        ClickHandler frame =  new ClickHandler();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);


        pane.add(frame,BorderLayout.CENTER);

        pn = new JPanel();
        pn.setBackground(Color.lightGray);
        pn.add(button);
        pane.add(pn, BorderLayout.LINE_START);

        pn = new JPanel();
        pane.add(pn, BorderLayout.PAGE_END);

        pn = new JPanel();
        pn.setBackground(Color.lightGray);
        pane.add(pn, BorderLayout.LINE_END);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {

        JFrame frame = new JFrame("Automaton Theory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}