package AutomatonTheory.Kotlin.Swing.DialogBoxes

import javax.swing.*

class CreateAutomatonDialog() {
    var automatonNameTextField = JTextField()
    var automatonTypesComboBox = JComboBox<String>()
    var automatonType = ""

    var valor: Int = 0
    var automatonTypes = mutableListOf("DFA", "NFA", "NFAe").toTypedArray()

    fun displayGUI() {
        valor = JOptionPane.showConfirmDialog(null,
                panel,
                "Create New Automaton",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE)
        automatonType = automatonTypesComboBox.selectedItem.toString()
    }

    private val panel: JPanel
        get() {

            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            val automatonNameLabel = JLabel("Automaton Name: ")
            val automatonTypeLabel = JLabel("Type: ")


            automatonTypesComboBox = JComboBox(automatonTypes)
            automatonNameTextField = JTextField()
            panel.add(automatonNameLabel)
            panel.add(automatonNameTextField)
            panel.add(Box.createVerticalStrut(20))
            panel.add(automatonTypeLabel)
            panel.add(automatonTypesComboBox)
            panel.add(Box.createVerticalStrut(20))

            return panel
        }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater { CreateAutomatonDialog().displayGUI() }
        }
    }
}