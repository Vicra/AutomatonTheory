package AutomatonTheory.JGraph.DialogBoxes;

import javax.swing.*;

public class AddTransitionDialog {

    public String[] States;
    public String[] Alphabet;

    public String originState;
    public String destinyState;
    public String symbol;

    public JComboBox<String> orginStateComboBox = new JComboBox<String>();
    public JComboBox<String> detinyStateComboBox = new JComboBox<String>();
    public JComboBox<String> symbolComboBox = new JComboBox<String>();


    public AddTransitionDialog(String[] states, String[] alphabet){
        States = states;
        Alphabet = alphabet;
    }

    public void displayGUI() {
        JOptionPane.showConfirmDialog(null,
                                getPanel(),
                                "AddTransition",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE);

        originState = orginStateComboBox.getSelectedItem().toString();
        destinyState = detinyStateComboBox.getSelectedItem().toString();
        symbol = symbolComboBox.getSelectedItem().toString();

    }

    private JPanel getPanel(){

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel originLabel = new JLabel("Choose Origin: ");
        orginStateComboBox = new JComboBox<String>(States);
        JLabel destinyLabel = new JLabel("Choose Destiny: ");
        detinyStateComboBox = new JComboBox<String>(States);
        JLabel symbolLabel = new JLabel("Choose Symbol: ");
        symbolComboBox = new JComboBox<String>(Alphabet);

        panel.add(originLabel);
        panel.add(orginStateComboBox);
        panel.add(destinyLabel);
        panel.add(detinyStateComboBox);
        panel.add(symbolLabel);
        panel.add(symbolComboBox);

        return panel;
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddTransitionDialog(new String[]{"q0","q1"}, new String[]{"0","1"}).displayGUI();
            }
        });
    }

}
