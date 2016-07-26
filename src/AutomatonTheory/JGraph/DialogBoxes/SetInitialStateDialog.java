package AutomatonTheory.JGraph.DialogBoxes;

import javax.swing.*;

public class SetInitialStateDialog {

    public String[] States;

    public JComboBox<String> initialStateComboBox = new JComboBox<String>();
    public String newInitialState = "";

    public SetInitialStateDialog(String[] states){
        States = states;
    }
    public void displayGUI() {
        JOptionPane.showConfirmDialog(null,
                getPanel(),
                "Set Initial State",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        newInitialState = initialStateComboBox.getSelectedItem().toString();
    }

    private JPanel getPanel(){

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel originLabel = new JLabel("Choose New Initial State: ");
        initialStateComboBox = new JComboBox<String>(States);

        panel.add(originLabel);
        panel.add(initialStateComboBox);

        return panel;
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SetInitialStateDialog(new String[]{"q0","q1"}).displayGUI();
            }
        });
    }
}
