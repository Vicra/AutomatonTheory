package AutomatonTheory.JGraph.DialogBoxes;

import javax.swing.*;

public class RemoveStateDialog {
    public String[] States;
    public JComboBox<String> removeStateComboBox = new JComboBox<String>();
    public String stateToRemove = "";

    public int valor;
    public RemoveStateDialog(String[] states){
        States = states;
    }

    public void displayGUI() {
        valor = JOptionPane.showConfirmDialog(null,
                getPanel(),
                "Set Initial State",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        stateToRemove = removeStateComboBox.getSelectedItem().toString();
    }

    private JPanel getPanel(){

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel originLabel = new JLabel("Choose State to Remove: ");
        removeStateComboBox = new JComboBox<String>(States);

        panel.add(originLabel);
        panel.add(removeStateComboBox);

        return panel;
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RemoveStateDialog(new String[]{"q0","q1"}).displayGUI();
            }
        });
    }
}
