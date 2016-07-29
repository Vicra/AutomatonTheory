package AutomatonTheory.JGraph.AutomatonDrawerComponents;

import AutomatonTheory.JGraph.DialogBoxes.AddTransitionDialog;
import AutomatonTheory.JGraph.DialogBoxes.RemoveStateDialog;
import AutomatonTheory.JGraph.DialogBoxes.SetInitialStateDialog;
import com.mxgraph.model.mxCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JPanel implements ActionListener {
    private JSplitPane splitPane;


    AutomatonFrame iframe =  new AutomatonFrame();
    JScrollPane scrollPaneAutomatonLog;
    JScrollPane scrollPaneAutomatonInfo;
    JTextArea actionsTextArea = new JTextArea();
    JTextArea automatonInfoTextArea = new JTextArea();


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

        //remove StateDialog
        JButton removeStateButton = new JButton("Remove State");
        removeStateButton.setBounds(20,110, buttonsWidth, buttonsHeight);
        removeStateButton.setVisible(true);
        removeStateButton.setActionCommand("removeState");
        removeStateButton.addActionListener(this);
        leftBarOptions.add(removeStateButton);

        //set Initial State Button
        JButton setInitialStateButton = new JButton("Set Initial State");
        setInitialStateButton.setBounds(20, 160, buttonsWidth, buttonsHeight);
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

        actionsTextArea.setVisible(true);
        actionsTextArea.setText("Automaton" + "\n");
        actionsTextArea.setEditable(false);
        scrollPaneAutomatonLog = new JScrollPane(actionsTextArea);
        scrollPaneAutomatonLog.setBounds(200,10,600,250);

        automatonInfoTextArea.setVisible(true);
        automatonInfoTextArea.setEditable(false);

        scrollPaneAutomatonInfo =  new JScrollPane(automatonInfoTextArea);
        scrollPaneAutomatonInfo.setBounds(1000,10,600,250);

        JButton updateAutomatonButton = new JButton("Update Automaton");
        updateAutomatonButton.setBounds(1700,10,buttonsWidth, buttonsHeight);
        updateAutomatonButton.setVisible(true);
        updateAutomatonButton.setActionCommand("updateAutomaton");
        updateAutomatonButton.addActionListener(this);


        downBarOptions.add(evaluateAutomatonButton);
        downBarOptions.add(field);
        downBarOptions.add(scrollPaneAutomatonLog);
        downBarOptions.add(scrollPaneAutomatonInfo);
        downBarOptions.add(updateAutomatonButton);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftBarOptions, graphViewPane),downBarOptions);

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
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
        JFrame frame = new JFrame("SplitPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainWindow mainWindow = new MainWindow();
        frame.getContentPane().add(mainWindow.getSplitPane());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
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
            actionsTextArea.append("State Added\n");
            automatonInfoTextArea.setText(iframe.dfa.get_automaton().getAutomatonInfo());
            return;
        }
        if(e.getActionCommand().equals("addTransition")){
            AddTransitionDialog dialog = new AddTransitionDialog(iframe.dfa.get_automaton().getStatesNames(),
                                                                (iframe.dfa.get_automaton().getAllAlphabet()));
            dialog.displayGUI();
            if(dialog.valor == 0){
                if(iframe.AddTransition(dialog.originState, dialog.destinyState, dialog.symbol)){
                    actionsTextArea.append("Transition added origin:" + dialog.originState + ", destiny: " + dialog.destinyState + ", symbol: " + dialog.symbol + "\n");
                    automatonInfoTextArea.setText(iframe.dfa.get_automaton().getAutomatonInfo());
                }
                JOptionPane.showMessageDialog(this, "No se agrego la transicion");
            }
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
        if(e.getActionCommand().equals("removeState")){
            RemoveStateDialog dialog = new RemoveStateDialog(iframe.dfa.get_automaton().getStatesNames());
            dialog.displayGUI();
            if(dialog.valor == 0){
                String stateName = dialog.stateToRemove;

                if(iframe.RemoveState(stateName)){
                    actionsTextArea.append("State Removed: " + stateName + "\n");
                    automatonInfoTextArea.setText(iframe.dfa.get_automaton().getAutomatonInfo());
                }
            }
            return;
        }
        if(e.getActionCommand().equals("setInitialState")){//setInitialState
            SetInitialStateDialog dialog = new SetInitialStateDialog(iframe.dfa.get_automaton().getStatesNames());
            dialog.displayGUI();
            String state = dialog.newInitialState;

            if(dialog.valor == 0){
                if(iframe.SetInitialState(state)){
                    mxCell node = iframe.getNode(state);
                    //iframe.graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#9FF781", new Object[]{node});
                    actionsTextArea.append("State: " + state + " set as initial state \n");
                    automatonInfoTextArea.setText(iframe.dfa.get_automaton().getAutomatonInfo());
                }
            }
            return;
        }
        if(e.getActionCommand().equals("updateAutomaton")){
            automatonInfoTextArea.setText(iframe.dfa.get_automaton().getAutomatonInfo());
            return;
        }
    }
}
