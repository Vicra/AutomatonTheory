package AutomatonTheory.JGraphKotlin.DialogBoxes

import javax.swing.*

class RemoveStateDialog(var States: Array<String>) {
    var removeStateComboBox = JComboBox<String>()
    var stateToRemove = ""

    fun displayGUI() {
        JOptionPane.showConfirmDialog(null,
                panel,
                "Remove State",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE)
        stateToRemove = removeStateComboBox.selectedItem.toString()
    }

    private val panel: JPanel
        get() {

            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            val originLabel = JLabel("Choose State to Remove: ")
            removeStateComboBox = JComboBox(States)

            panel.add(originLabel)
            panel.add(removeStateComboBox)

            return panel
        }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater { RemoveStateDialog(arrayOf("q0", "q1")).displayGUI() }
        }
    }
}
