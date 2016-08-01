package AutomatonTheory.Kotlin.Swing.DialogBoxes

import javax.swing.*

class AddTransitionDialog(var States: Array<String>, var Alphabet: Array<String>) {

    var originState: String = ""
    var destinyState: String = ""
    var symbol: String = ""

    var orginStateComboBox = JComboBox<String>()
    var detinyStateComboBox = JComboBox<String>()
    var symbolComboBox = JComboBox<String>()

    var valor: Int = 0

    fun displayGUI() {
        valor = JOptionPane.showConfirmDialog(null,
                panel,
                "AddTransition",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE)

        originState = orginStateComboBox.selectedItem.toString()
        destinyState = detinyStateComboBox.selectedItem.toString()
        symbol = symbolComboBox.selectedItem.toString()

    }

    private val panel: JPanel
        get() {

            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            val originLabel = JLabel("Choose Origin: ")
            orginStateComboBox = JComboBox(States)
            val destinyLabel = JLabel("Choose Destiny: ")
            detinyStateComboBox = JComboBox(States)
            val symbolLabel = JLabel("Choose Symbol: ")
            symbolComboBox = JComboBox(Alphabet)

            panel.add(originLabel)
            panel.add(orginStateComboBox)
            panel.add(destinyLabel)
            panel.add(detinyStateComboBox)
            panel.add(symbolLabel)
            panel.add(symbolComboBox)

            return panel
        }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater { AddTransitionDialog(arrayOf("q0", "q1"), arrayOf("0", "1")).displayGUI() }
        }
    }

}
