package AutomatonsTheory.Swing.DialogBoxes

import javax.swing.*

class AddBranchGrammarFreeDialog() {

    var symbol: String = ""
    var produccion:String = ""

    var symbolTextField = JTextField()
    var produccionTextField = JTextField()

    var valor: Int = 0

    fun displayGUI() {
        valor = JOptionPane.showConfirmDialog(null,
                panel,
                "Add Branch",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE)

        symbol = symbolTextField.text.toString()
        produccion = produccionTextField.text.toString()

    }

    private val panel: JPanel
        get() {

            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            val originLabel = JLabel("Choose Symbol: ")
            symbolTextField = JTextField()
            val destinyLabel = JLabel("Choose Productions: ")
            produccionTextField = JTextField()

            panel.add(originLabel)
            panel.add(symbolTextField)
            panel.add(destinyLabel)
            panel.add(produccionTextField)

            return panel
        }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater { AddBranchGrammarFreeDialog().displayGUI() }
        }
    }

}
