package AutomatonsTheory.TabDemo

import javax.swing.*

class ComplementAutomatonDialog(automatonList: MutableList<String>) {
    var automatonList:Array<String> = automatonList.toTypedArray()
    var automatonA: String = ""

    var automatonAComboBox = JComboBox<String>()

    var valor: Int = 0

    fun displayGUI() {
        valor = JOptionPane.showConfirmDialog(null,
                panel,
                "Complement",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE)

        automatonA = automatonAComboBox.selectedItem.toString()
    }

    private val panel: JPanel
        get() {

            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            val originLabel = JLabel("Choose Automaton : ")
            automatonAComboBox = JComboBox(automatonList)

            panel.add(originLabel)
            panel.add(automatonAComboBox)

            return panel
        }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater { ComplementAutomatonDialog(mutableListOf("dfa1", "dfa2")).displayGUI() }
        }
    }

}
