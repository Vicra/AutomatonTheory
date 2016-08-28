package AutomatonsTheory.TabDemo

import javax.swing.*

class CombineAutomatonsDialog(automatonList: MutableList<String>, combineAction:String) {
    var automatonList:Array<String> = automatonList.toTypedArray()
    var combineAction:String = combineAction
    var automatonA: String = ""
    var automatonB: String = ""

    var automatonAComboBox = JComboBox<String>()
    var automatonBComboBox = JComboBox<String>()

    var valor: Int = 0

    fun displayGUI() {
        valor = JOptionPane.showConfirmDialog(null,
                panel,
                combineAction,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE)

        automatonA = automatonAComboBox.selectedItem.toString()
        automatonB = automatonBComboBox.selectedItem.toString()

    }

    private val panel: JPanel
        get() {

            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            val originLabel = JLabel("Choose Automaton A: ")
            automatonAComboBox = JComboBox(automatonList)
            val destinyLabel = JLabel("Choose Automaton B: ")
            automatonBComboBox = JComboBox(automatonList)

            panel.add(originLabel)
            panel.add(automatonAComboBox)
            panel.add(destinyLabel)
            panel.add(automatonBComboBox)

            return panel
        }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater { CombineAutomatonsDialog(mutableListOf("dfa1", "dfa2"), "union").displayGUI() }
        }
    }

}
