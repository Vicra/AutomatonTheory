package AutomatonTheory.JGraphKotlin.AutomatonDrawerComponents


import AutomatonTheory.JGraphKotlin.DialogBoxes.AddTransitionDialog
import AutomatonTheory.JGraphKotlin.DialogBoxes.RemoveStateDialog
import AutomatonTheory.JGraphKotlin.DialogBoxes.SetInitialStateDialog
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class MainWindow : JPanel(), ActionListener {
    val splitPane: JSplitPane

    internal var iframe = AutomatonFrame()


    internal var field = JTextField()

    private val buttonsWidth = 150
    private val buttonsHeight = 40

    init {
        val leftBarOptions = JScrollPane()


        //add State Button
        val addStateButton = JButton("Add State")
        addStateButton.setBounds(20, 10, buttonsWidth, buttonsHeight)
        addStateButton.isVisible = true
        addStateButton.actionCommand = "addState"
        addStateButton.addActionListener(this)
        leftBarOptions.add(addStateButton)//add the button to the laft bar

        //add Transition Button
        val addTransitionButton = JButton("Add Transition")
        addTransitionButton.setBounds(20, 60, buttonsWidth, buttonsHeight)
        addTransitionButton.isVisible = true
        addTransitionButton.actionCommand = "addTransition"
        addTransitionButton.addActionListener(this)
        leftBarOptions.add(addTransitionButton)

        //set Initial State BUtton
        val setInitialStateButton = JButton("Set Initial State")
        setInitialStateButton.setBounds(20, 110, buttonsWidth, buttonsHeight)
        setInitialStateButton.isVisible = true
        setInitialStateButton.actionCommand = "setInitialState"
        setInitialStateButton.addActionListener(this)
        leftBarOptions.add(setInitialStateButton)

        iframe.isVisible = true
        val graphViewPane = JScrollPane(iframe)

        val downBarOptions = JScrollPane()
        val evaluateAutomatonButton = JButton("Evaluate")
        evaluateAutomatonButton.setBounds(20, 10, buttonsWidth, buttonsHeight)
        evaluateAutomatonButton.isVisible = true
        evaluateAutomatonButton.actionCommand = "evaluate"
        evaluateAutomatonButton.addActionListener(this)
        field.setBounds(20, 60, buttonsWidth, buttonsHeight)
        downBarOptions.add(evaluateAutomatonButton)
        downBarOptions.add(field)

        splitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT, JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftBarOptions, graphViewPane), downBarOptions)

        splitPane.isOneTouchExpandable = true
        splitPane.dividerLocation = 150

        //Provide minimum sizes for the two components in the split pane.
        graphViewPane.minimumSize = Dimension(1400, 700)

        leftBarOptions.minimumSize = Dimension(200, 500)
        leftBarOptions.maximumSize = Dimension(220, 520)

        downBarOptions.minimumSize = Dimension(200, 200)
        downBarOptions.maximumSize = Dimension(200, 200)

    }

    override fun actionPerformed(e: ActionEvent) {
        if (e.actionCommand == "addState") {
            iframe.AddState()
            return
        }
        if (e.actionCommand == "addTransition") {


            val dialog = AddTransitionDialog(iframe.dfa._automaton.getStatesNames(),
                    iframe.dfa._automaton.getAllAlphabet())
            dialog.displayGUI()
            iframe.AddTransition(dialog.originState, dialog.destinyState, dialog.symbol)
            return
        }
        if (e.actionCommand == "evaluate") {
            val evalutaCadena = field.text
            if (iframe.EvaluateAutomaton(evalutaCadena)) {
                JOptionPane.showMessageDialog(iframe, "Cadena paso")
            } else {
                JOptionPane.showMessageDialog(iframe, "La Cadena no paso", "Error", JOptionPane.ERROR_MESSAGE)
            }
            return
        }
        if (e.actionCommand == "setInitialState") {//setInitialState
            val dialog = SetInitialStateDialog(iframe.dfa._automaton.getStatesNames())
            dialog.displayGUI()
            var newInitialState:String = dialog.newInitialState
            return
        }
        if(e.actionCommand == "removeState"){
            val dialog = RemoveStateDialog(iframe.dfa._automaton.getStatesNames())
            dialog.displayGUI()
            var stateToRemove :String = dialog.stateToRemove
            return
        }
    }

    companion object {

        private fun createAndShowGUI() {

            //Create and set up the window.
            val frame = JFrame("SplitPaneDemo")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            val mainWindow = MainWindow()
            frame.contentPane.add(mainWindow.splitPane)

            //Display the window.
            frame.extendedState = JFrame.MAXIMIZED_BOTH
            frame.pack()
            frame.isVisible = true


        }

        @JvmStatic fun main(args: Array<String>) {
            //Schedule a job for the event-dispatching thread:
            //creating and showing this application's GUI.
            javax.swing.SwingUtilities.invokeLater { createAndShowGUI() }
        }
    }

}
