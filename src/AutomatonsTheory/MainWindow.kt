package AutomatonsTheory

import AutomatonsTheory.AutomatonExtensions.DeterministicFiniteAutomaton
import AutomatonsTheory.AutomatonExtensions.NonDeterministicFiniteAutomaton
import AutomatonsTheory.AutomatonExtensions.NonDeterministicFiniteEpsilonAutomaton
import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.Swing.AutomatonDrawerComponents.AutomatonFrame
import AutomatonsTheory.Swing.DialogBoxes.*
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import javax.swing.*

class MainWindow : JPanel(), ActionListener {
    val splitPane: JSplitPane

    internal var iframe = AutomatonFrame(DeterministicFiniteAutomaton("dfa", mutableListOf("0","1")))
    internal var scrollPaneAutomatonLog: JScrollPane
    internal var scrollPaneAutomatonInfo: JScrollPane
    internal var actionsTextArea = JTextArea()
    internal var automatonInfoTextArea = JTextArea()

    internal var field = JTextField()

    private val buttonsWidth = 150
    private val buttonsHeight = 40

    init {

        val dialog = CreateAutomatonDialog()
        dialog.displayGUI()

        if(dialog.valor == 0){
            if(dialog.automatonType == "DFA"){
                iframe = AutomatonFrame(DeterministicFiniteAutomaton(dialog.automatonNameTextField.text, dialog.charactersList))
            }
            if(dialog.automatonType == "NFA"){
                iframe = AutomatonFrame(NonDeterministicFiniteAutomaton(dialog.automatonNameTextField.text, dialog.charactersList))
            }
            if(dialog.automatonType == "NFAe") {
                iframe = AutomatonFrame(NonDeterministicFiniteEpsilonAutomaton(dialog.automatonNameTextField.text, dialog.charactersList))
            }
        }

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

        //remove StateDialog
        val removeStateButton = JButton("Remove State")
        removeStateButton.setBounds(20, 110, buttonsWidth, buttonsHeight)
        removeStateButton.isVisible = true
        removeStateButton.actionCommand = "removeState"
        removeStateButton.addActionListener(this)
        leftBarOptions.add(removeStateButton)

        //set Initial State Button
        val setInitialStateButton = JButton("Set Initial State")
        setInitialStateButton.setBounds(20, 160, buttonsWidth, buttonsHeight)
        setInitialStateButton.isVisible = true
        setInitialStateButton.actionCommand = "setInitialState"
        setInitialStateButton.addActionListener(this)
        leftBarOptions.add(setInitialStateButton)

        //remove a transition
        val removeTransitionButton = JButton("Remove Transition")
        removeTransitionButton.setBounds(20, 210, buttonsWidth, buttonsHeight)
        removeTransitionButton.isVisible = true
        removeTransitionButton.actionCommand = "removeTransition"
        removeTransitionButton.addActionListener(this)
        leftBarOptions.add(removeTransitionButton)

        if(iframe.automaton.Type.toString() == "NFA"){
            val convertDFAButton = JButton("Convert to DFA")
            convertDFAButton.setBounds(20, 600, buttonsWidth, buttonsHeight)
            convertDFAButton.isVisible = true
            convertDFAButton.actionCommand = "toDFA"
            convertDFAButton.addActionListener(this)
            leftBarOptions.add(convertDFAButton)
        }


        iframe.setVisible(true)
        val graphViewPane = JScrollPane(iframe)

        val downBarOptions = JScrollPane()
        val evaluateAutomatonButton = JButton("Evaluate")
        evaluateAutomatonButton.setBounds(20, 10, buttonsWidth, buttonsHeight)
        evaluateAutomatonButton.isVisible = true
        evaluateAutomatonButton.actionCommand = "evaluate"
        evaluateAutomatonButton.addActionListener(this)

        field.setBounds(20, 60, buttonsWidth, buttonsHeight)

        actionsTextArea.isVisible = true
        actionsTextArea.text = "Automaton" + "\n"
        actionsTextArea.isEditable = false
        scrollPaneAutomatonLog = JScrollPane(actionsTextArea)
        scrollPaneAutomatonLog.setBounds(200, 10, 600, 250)

        automatonInfoTextArea.isVisible = true
        automatonInfoTextArea.isEditable = false

        scrollPaneAutomatonInfo = JScrollPane(automatonInfoTextArea)
        scrollPaneAutomatonInfo.setBounds(1000, 10, 600, 250)

        val updateAutomatonButton = JButton("Update Automaton")
        updateAutomatonButton.setBounds(1700, 10, buttonsWidth, buttonsHeight)
        updateAutomatonButton.isVisible = true
        updateAutomatonButton.actionCommand = "updateAutomaton"
        updateAutomatonButton.addActionListener(this)


        downBarOptions.add(evaluateAutomatonButton)
        downBarOptions.add(field)
        downBarOptions.add(scrollPaneAutomatonLog)
        downBarOptions.add(scrollPaneAutomatonInfo)
        downBarOptions.add(updateAutomatonButton)

        splitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT, JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftBarOptions, graphViewPane), downBarOptions)

        splitPane.isOneTouchExpandable = true
        splitPane.dividerLocation = 150
        graphViewPane.minimumSize = Dimension(1400, 700)
        leftBarOptions.minimumSize = Dimension(200, 500)
        leftBarOptions.maximumSize = Dimension(220, 520)
        downBarOptions.minimumSize = Dimension(200, 200)
        downBarOptions.maximumSize = Dimension(200, 200)

    }

    override fun actionPerformed(e: ActionEvent) {
        if (e.actionCommand == "addState") {
            iframe.AddState()
            actionsTextArea.append("State Added\n")
            automatonInfoTextArea.text = iframe.automaton.getAutomatonInfo()
            return
        }
        if (e.actionCommand == "addTransition") {
            AddTransitionToFrame()
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
        if (e.actionCommand == "removeState") {
            val dialog = RemoveStateDialog(iframe.automaton.getStatesNames())
            dialog.displayGUI()
            if (dialog.valor == 0) {
                val stateName = dialog.stateToRemove

                if (iframe.RemoveState(stateName)) {
                    actionsTextArea.append("State Removed: " + stateName + "\n")
                    automatonInfoTextArea.text = iframe.automaton.getAutomatonInfo()
                }
            }
            return
        }
        if (e.actionCommand == "setInitialState") {//setInitialState
            val dialog = SetInitialStateDialog(iframe.automaton.getStatesNames())
            dialog.displayGUI()
            val state = dialog.newInitialState

            if (dialog.valor == 0) {
                if (iframe.SetInitialState(state)) {
                    val node = iframe.getNode(state)
                    //iframe.graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#9FF781", new Object[]{node});
                    actionsTextArea.append("State: $state set as initial state \n")
                    automatonInfoTextArea.text = iframe.automaton.getAutomatonInfo()
                }
            }
            return
        }
        if (e.actionCommand == "updateAutomaton") {
            automatonInfoTextArea.text = iframe.automaton.getAutomatonInfo()
            return
        }
        if (e.actionCommand == "removeTransition"){
            val dialog = RemoveTransitionDialog(iframe.automaton.getStatesNames(),
                    iframe.automaton.getAllAlphabet())
            dialog.displayGUI()
            if (dialog.valor == 0) {
                if(iframe.RemoveTransition(dialog.originState, dialog.destinyState, dialog.symbol)){
                    actionsTextArea.append("Transition REMOVED origin:" + dialog.originState + ", destiny: " + dialog.destinyState + ", symbol: " + dialog.symbol + "\n")
                    automatonInfoTextArea.text = iframe.automaton.getAutomatonInfo()
                }
                else {
                    JOptionPane.showMessageDialog(this, "Transition not removed")
                }
            }
            return
        }
        if (e.actionCommand == "toDFA") {
            var dfa: DeterministicFiniteAutomaton = (iframe.automaton as NonDeterministicFiniteAutomaton).toDeterministicFiniteAutomaton()
            //clean current


            for(state in iframe.Nodes){
                val node = iframe.getNode(state.getValue().toString())
                iframe.graph.removeCells(arrayOf<Any>(node))
            }
            iframe.Nodes.clear()
            iframe.Transitions.clear()
            iframe.graph.refresh()
            //iframe = AutomatonFrame(dfa)
            iframe.automaton = dfa

            //display states
            for(state in dfa.States){
                var rangeMinx:Double = 20.0
                var rangeMaxx:Double = 900.0
                val r = Random()
                val randomValuex= rangeMinx + (rangeMaxx - rangeMinx) * r.nextDouble()

                var rangeMiny:Double = 20.0
                var rangeMaxy:Double = 600.0
                val ry = Random()
                val randomValuey= rangeMiny + (rangeMaxy - rangeMiny) * ry.nextDouble()

                iframe.AddState(state.Name, state.InitialState, state.AcceptanceState, randomValuex, randomValuey)
            }
            iframe.graph.refresh()
            for(state in dfa.States){
                for(transition in state.Transitions){
                    AddTransitionToFrame(state.Name, transition.DestinyState.Name, transition.Symbol)
                }
            }
            iframe.graph.refresh()


            //display
            println("vamo a calmarno")
        }
    }

    private fun AddTransitionToFrame() {
        val dialog = AddTransitionDialog(iframe.automaton.getStatesNames(),
                iframe.automaton.getAllAlphabet())
        dialog.displayGUI()
        if (dialog.valor == 0) {
            if (iframe.AddTransition(dialog.originState, dialog.destinyState, dialog.symbol)) {
                actionsTextArea.append("Transition ADDED origin:" + dialog.originState + ", destiny: " + dialog.destinyState + ", symbol: " + dialog.symbol + "\n")
                automatonInfoTextArea.text = iframe.automaton.getAutomatonInfo()
            } else {
                JOptionPane.showMessageDialog(this, "No se agrego la transicion")
            }
        }
    }

    private fun AddTransitionToFrame(originStateName:String, destinyStateName:String, symbol:String) {
        iframe.AddTransition(originStateName, destinyStateName, symbol)
        actionsTextArea.append("Transition ADDED origin:" + originStateName + ", destiny: " + destinyStateName + ", symbol: " + symbol + "\n")
        automatonInfoTextArea.text = iframe.automaton.getAutomatonInfo()
    }

    companion object {

        private fun createAndShowGUI() {
            val frame = JFrame("SplitPaneDemo");
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            val mainWindow = MainWindow()
            frame.contentPane.add(mainWindow.splitPane)
            frame.extendedState = JFrame.MAXIMIZED_BOTH
            frame.pack()
            frame.setVisible(true)

            var menuBar: JMenuBar = JMenuBar()
            var menu: JMenu = JMenu("menu")
            menu.getAccessibleContext().setAccessibleDescription("Actions Menu")
            menuBar.add(menu)

            var createAutomatonItem = JMenuItem("Create Automaton")
            createAutomatonItem.getAccessibleContext().setAccessibleDescription("Create New Automaton")
            createAutomatonItem.addActionListener(ActionListener {
                println("create automaton")
            })
            menu.add(createAutomatonItem)

            var saveAutomatonItem = JMenuItem("Save Automaton")
            saveAutomatonItem.getAccessibleContext().setAccessibleDescription("Save Automaton")
            saveAutomatonItem.addActionListener(ActionListener{
                var filename:String = ""
                var dir:String = ""
                val saveFileChooser = JFileChooser()
                // Demonstrate "Open" dialog:
                val rVal = saveFileChooser.showSaveDialog(frame)
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    filename = saveFileChooser.getSelectedFile().getName()
                    dir = saveFileChooser.getCurrentDirectory().toString()

                    //
                    val fileOut = FileOutputStream(dir + "/" +filename + ".ser")
                    val out = ObjectOutputStream(fileOut)
                    out.writeObject(mainWindow.iframe.automaton)
                    out.close()
                    fileOut.close()

                    //*************

                    //
                    println("dembow")
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                    filename = "You pressed cancel"
                }
            })
            menu.add(saveAutomatonItem)

            var openAutomatonItem = JMenuItem("Open Automaton")
            openAutomatonItem.getAccessibleContext().setAccessibleDescription("Open Automaton")
            openAutomatonItem.addActionListener(ActionListener{
                var filename:String = ""
                var dir:String = ""
                val openFileChooser = JFileChooser()
                // Demonstrate "Open" dialog:
                val rVal = openFileChooser.showOpenDialog(frame)
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    filename = openFileChooser.getSelectedFile().getName()
                    dir = openFileChooser.getCurrentDirectory().toString()

                    val fileIn = FileInputStream(dir + "/" +filename)
                    val input = ObjectInputStream(fileIn)
                    val e: Automaton = input.readObject() as Automaton
                    input.close()
                    fileIn.close()

                    println("dembow")
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                    filename = "You pressed cancel"
                }
            })
            menu.add(openAutomatonItem)

            frame.setJMenuBar(menuBar)
        }

        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater { createAndShowGUI() }
        }
    }
}
