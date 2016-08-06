package AutomatonTheory.Kotlin.Swing.DialogBoxes

import java.util.*
import javax.swing.*

class CreateAutomatonDialog() {
    var automatonNameTextField = JTextField()
    var alphabetTextField = JTextField()
    var automatonTypesComboBox = JComboBox<String>()
    var automatonType = ""
    var charactersList:MutableList<String> = ArrayList()

    var valor: Int = 0
    var automatonTypes = mutableListOf("DFA", "NFA", "NFAe").toTypedArray()

    fun displayGUI() {
        valor = JOptionPane.showConfirmDialog(null,
                panel,
                "Create New Automaton",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE)
        automatonType = automatonTypesComboBox.selectedItem.toString()
        var splitedCharacters:List<String> = alphabetTextField.text.split(',')
        for(character in splitedCharacters){
            if(character.length == 1){
                charactersList.add(character)
            }
        }
        println("alaffbeto")
    }

    private val panel: JPanel
        get() {

            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            val automatonNameLabel = JLabel("Automaton Name: ")
            val automatonTypeLabel = JLabel("Type: ")
            val alphabetLabel = JLabel("Alphabet, characters separated by (,)")


            automatonTypesComboBox = JComboBox(automatonTypes)
            automatonNameTextField = JTextField()
            alphabetTextField = JTextField()

            panel.add(automatonNameLabel)
            panel.add(automatonNameTextField)
            panel.add(Box.createVerticalStrut(20))
            panel.add(automatonTypeLabel)
            panel.add(automatonTypesComboBox)
            panel.add(Box.createVerticalStrut(20))
            panel.add(alphabetLabel)
            panel.add(alphabetTextField)

            return panel
        }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater { CreateAutomatonDialog().displayGUI() }
        }
    }
}