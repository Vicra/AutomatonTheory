package AutomatonTheory.JGraph.Examples;

import javax.swing.*;
import java.awt.*;

public class CreateReadOnlyJTextField extends JFrame {

    private static final long serialVersionUID = 1L;

    public CreateReadOnlyJTextField() {

        // set flow layout for the frame
        this.getContentPane().setLayout(new FlowLayout());

        // create JTextField
        JTextArea field = new JTextArea();

        field.setText("Java Code Geeks");
        field.setText(field.getText() + "mas texto");

        // Sets the specified boolean to indicate whether or not
        // this textfield should be editable.
        field.setEditable(false);
        field.setBounds(0,0,400,300);
        String current = field.getText();
        field.setText(current + "\nEstados");

        // add textfield to frame
        add(field);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    private static void createAndShowGUI() {

        //Create and set up the window.

        JFrame frame = new CreateReadOnlyJTextField();

        //Display the window.

        frame.pack();

        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {

        //Schedule a job for the event-dispatching thread:

        //creating and showing this application's GUI.

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                createAndShowGUI();

            }

        });
    }

}