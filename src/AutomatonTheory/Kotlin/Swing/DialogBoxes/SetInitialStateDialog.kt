package AutomatonTheory.Kotlin.Swing.DialogBoxes

import javax.swing.*

class SetInitialStateDialog(var States: Array<String>) {

    var initialStateComboBox = JComboBox<String>()
    var newInitialState = ""
    var valor: Int = 0
    fun displayGUI() {
        valor = JOptionPane.showConfirmDialog(null,
                panel,
                "Set Initial State",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE)
        newInitialState = initialStateComboBox.selectedItem.toString()
    }

    private val panel: JPanel
        get() {

            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            val originLabel = JLabel("Choose New Initial State: ")
            initialStateComboBox = JComboBox(States)

            panel.add(originLabel)
            panel.add(initialStateComboBox)

            return panel
        }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater { SetInitialStateDialog(arrayOf("q0", "q1")).displayGUI() }
        }
    }
}
