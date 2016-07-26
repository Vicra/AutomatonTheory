package AutomatonTheory.JGraph.AutomatonDrawerComponents;

import AutomatonTheory.JGraph.DialogBoxes.AddTransitionDialog;
import AutomatonTheory.JGraph.DialogBoxes.SetInitialStateDialog;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JPanel implements ActionListener {
    private JSplitPane splitPane;

    AutomatonFrame iframe =  new AutomatonFrame();


    JTextField field = new JTextField();

    private int buttonsWidth = 150;
    private int buttonsHeight = 40;

    public MainWindow(){
        JScrollPane leftBarOptions = new JScrollPane();

        //add State Button
        JButton addStateButton = new JButton("Add State");
        addStateButton.setBounds(20,10,buttonsWidth,buttonsHeight);
        addStateButton.setVisible(true);
        addStateButton.setActionCommand("addState");
        addStateButton.addActionListener(this);
        leftBarOptions.add(addStateButton);//add the button to the laft bar

        //add Transition Button
        JButton addTransitionButton = new JButton("Add Transition");
        addTransitionButton.setBounds(20, 60, buttonsWidth, buttonsHeight);
        addTransitionButton.setVisible(true);
        addTransitionButton.setActionCommand("addTransition");
        addTransitionButton.addActionListener(this);
        leftBarOptions.add(addTransitionButton);

        //set Initial State BUtton
        JButton setInitialStateButton = new JButton("Set Initial State");
        setInitialStateButton.setBounds(20, 110, buttonsWidth, buttonsHeight);
        setInitialStateButton.setVisible(true);
        setInitialStateButton.setActionCommand("setInitialState");
        setInitialStateButton.addActionListener(this);
        leftBarOptions.add(setInitialStateButton);

        iframe.setVisible(true);
        JScrollPane graphViewPane = new JScrollPane(iframe);

        JScrollPane downBarOptions = new JScrollPane();
        JButton evaluateAutomatonButton = new JButton("Evaluate");
        evaluateAutomatonButton.setBounds(20,10,buttonsWidth, buttonsHeight);
        evaluateAutomatonButton.setVisible(true);
        evaluateAutomatonButton.setActionCommand("evaluate");
        evaluateAutomatonButton.addActionListener(this);
        field.setBounds(20,60,buttonsWidth,buttonsHeight);
        downBarOptions.add(evaluateAutomatonButton);
        downBarOptions.add(field);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftBarOptions, graphViewPane),downBarOptions);

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        //Provide minimum sizes for the two components in the split pane.
        graphViewPane.setMinimumSize(new Dimension(1400,700));

        leftBarOptions.setMinimumSize(new Dimension(200,500));
        leftBarOptions.setMaximumSize(new Dimension(220,520));

        downBarOptions.setMinimumSize(new Dimension(200,200));
        downBarOptions.setMaximumSize(new Dimension(200,200));

    }
    public JSplitPane getSplitPane() {
        return splitPane;
    }

    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("SplitPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainWindow mainWindow = new MainWindow();
        frame.getContentPane().add(mainWindow.getSplitPane());

        //Display the window.
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);


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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("addState")){
            iframe.AddState();
            return;
        }
        if(e.getActionCommand().equals("addTransition")){


            AddTransitionDialog dialog = new AddTransitionDialog(iframe.dfa.get_automaton().getStatesNames(),
                                                                (iframe.dfa.get_automaton().getAllAlphabet()));
            dialog.displayGUI();
            iframe.AddTransition(dialog.originState, dialog.destinyState, dialog.symbol);
            return;
        }
        if(e.getActionCommand().equals("evaluate")){
            String evalutaCadena = field.getText();
            if(iframe.EvaluateAutomaton(evalutaCadena)){
                JOptionPane.showMessageDialog(iframe, "Cadena paso");
            }
            else{
                JOptionPane.showMessageDialog(iframe, "La Cadena no paso", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        if(e.getActionCommand().equals("setInitialState")){//setInitialState
            SetInitialStateDialog dialog = new SetInitialStateDialog(iframe.dfa.get_automaton().getStatesNames());
            dialog.displayGUI();
            String state = dialog.newInitialState;

            if(iframe.SetInitialState(state)){
                mxCell node = iframe.getNode(state);
                iframe.graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#9FF781", new Object[]{node});
            }
            return;
        }
    }

}
