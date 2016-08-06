package AutomatonTheory.Kotlin.Swing.DialogBoxes

import javax.swing.*

class ToggleAcceptanceStateDialog(var States: Array<String>) {

    var statesComboBox = JComboBox<String>()
    var toggleAcceptanceState = ""
    var valor: Int = 0
    fun displayGUI() {
        valor = JOptionPane.showConfirmDialog(null,
                panel,
                "Toggle Acceptance State",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE)
        toggleAcceptanceState = statesComboBox.selectedItem.toString()
    }

    private val panel: JPanel
        get() {

            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            val originLabel = JLabel("Toggle Acceptance State: ")
            statesComboBox = JComboBox(States)

            panel.add(originLabel)
            panel.add(statesComboBox)

            return panel
        }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater { ToggleAcceptanceStateDialog(arrayOf("q0", "q1")).displayGUI() }
        }
    }
}
